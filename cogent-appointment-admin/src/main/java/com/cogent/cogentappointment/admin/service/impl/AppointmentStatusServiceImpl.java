package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.AppointmentStatusResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.DoctorTimeSlotResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.doctorDutyRoster.DoctorDutyRosterStatusResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.DoctorDutyRosterOverrideRepository;
import com.cogent.cogentappointment.admin.repository.DoctorDutyRosterRepository;
import com.cogent.cogentappointment.admin.service.AppointmentService;
import com.cogent.cogentappointment.admin.service.AppointmentStatusService;
import com.cogent.cogentappointment.persistence.model.DoctorDutyRoster;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.NO;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.FETCHING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.FETCHING_PROCESS_STARTED;
import static com.cogent.cogentappointment.admin.log.constants.AppointmentLog.APPOINTMENT_STATUS;
import static com.cogent.cogentappointment.admin.utils.AppointmentStatusUtils.calculateTimeSlotsForAllAppointmentStatus;
import static com.cogent.cogentappointment.admin.utils.AppointmentStatusUtils.calculateTimeSlotsForSelectedAppointmentStatus;
import static com.cogent.cogentappointment.admin.utils.DoctorDutyRosterUtils.mergeOverrideAndActualDoctorDutyRoster;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author smriti ON 16/12/2019
 */
@Service
@Transactional
@Slf4j
public class AppointmentStatusServiceImpl implements AppointmentStatusService {

    private final DoctorDutyRosterRepository doctorDutyRosterRepository;

    private final DoctorDutyRosterOverrideRepository doctorDutyRosterOverrideRepository;

    private final AppointmentService appointmentService;

    public AppointmentStatusServiceImpl(DoctorDutyRosterRepository doctorDutyRosterRepository,
                                        DoctorDutyRosterOverrideRepository doctorDutyRosterOverrideRepository,
                                        AppointmentService appointmentService) {
        this.doctorDutyRosterRepository = doctorDutyRosterRepository;
        this.doctorDutyRosterOverrideRepository = doctorDutyRosterOverrideRepository;
        this.appointmentService = appointmentService;
    }

    @Override
    public List<DoctorDutyRosterStatusResponseDTO> fetchAppointmentStatusResponseDTO
            (AppointmentStatusRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, APPOINTMENT_STATUS);

        List<DoctorDutyRosterStatusResponseDTO> doctorDutyRosterStatus = fetchDoctorStatus(requestDTO);

        validateDoctorDutyRosterStatus(doctorDutyRosterStatus);

        List<AppointmentStatusResponseDTO> appointments = fetchAppointmentStatus(requestDTO);

        if (ObjectUtils.isEmpty(requestDTO.getStatus()))
            setDoctorTimeSlotForAllAppointmentStatus(doctorDutyRosterStatus, appointments, requestDTO.getStatus());
        else
            setDoctorTimeSlotForSelectedAppointmentStatus(doctorDutyRosterStatus, appointments);

        log.info(FETCHING_PROCESS_COMPLETED, APPOINTMENT_STATUS, getDifferenceBetweenTwoTime(startTime));

        return doctorDutyRosterStatus;
    }

    /*FETCH DOCTOR DUTY ROSTER FROM DOCTOR_DUTY_ROSTER_OVERRIDE
      AND THEN DOCTOR_DUTY ROSTER. THEN MERGE BOTH ROSTERS BASED ON THE REQUESTED SEARCH DATE, DOCTOR AND SPECIALIZATION*/
    private List<DoctorDutyRosterStatusResponseDTO> fetchDoctorStatus(AppointmentStatusRequestDTO requestDTO) {

        List<DoctorDutyRosterStatusResponseDTO> doctorDutyRosterOverrideStatus =
                doctorDutyRosterOverrideRepository.fetchDoctorDutyRosterOverrideStatus(requestDTO);

        List<DoctorDutyRosterStatusResponseDTO> doctorDutyRosterStatus =
                doctorDutyRosterRepository.fetchDoctorDutyRosterStatus(requestDTO);

        return mergeOverrideAndActualDoctorDutyRoster(doctorDutyRosterOverrideStatus, doctorDutyRosterStatus);
    }

    /*FETCH APPOINTMENT DETAILS*/
    private List<AppointmentStatusResponseDTO> fetchAppointmentStatus(AppointmentStatusRequestDTO requestDTO) {
        return appointmentService.fetchAppointmentForAppointmentStatus(requestDTO);
    }

    /*IF DOCTOR DAY OFF STATUS= 'Y', THEN THERE ARE NO ANY TIME SLOTS
     * IF DAY OFF STATUS = 'N' :
     * IF APPOINTMENT EXISTS WITHIN SELECTED DOCTOR DUTY ROSTER RANGE, THEN FILTER APPOINTMENT STATUS ACCORDINGLY
     * ELSE APPOINTMENT STATUS IS VACANT FOR ALL TIME SLOTS*/
    private void setDoctorTimeSlotForAllAppointmentStatus(List<DoctorDutyRosterStatusResponseDTO> doctorDutyRosterStatusResponseDTOS,
                                                          List<AppointmentStatusResponseDTO> appointments,
                                                          String searchAppointmentStatus) {

        for (DoctorDutyRosterStatusResponseDTO doctorDutyRosterStatusResponseDTO : doctorDutyRosterStatusResponseDTOS) {

            doctorDutyRosterStatusResponseDTO.setWeekDayName(
                    doctorDutyRosterStatusResponseDTO.getDate().getDayOfWeek().toString());

            if (doctorDutyRosterStatusResponseDTO.getDayOffStatus().equals(NO)) {
                if (!ObjectUtils.isEmpty(appointments)) {
                    for (AppointmentStatusResponseDTO appointment : appointments) {
                        if (hasAppointment(appointment, doctorDutyRosterStatusResponseDTO)) {
                            setTimeSlotForAllAppointmentStatus(doctorDutyRosterStatusResponseDTO,
                                    appointment.getAppointmentTimeDetails(), searchAppointmentStatus);
                            break;
                        } else {
                            setTimeSlotForAllAppointmentStatus(doctorDutyRosterStatusResponseDTO,
                                    null, searchAppointmentStatus);
                        }
                    }
                } else {
                    setTimeSlotForAllAppointmentStatus(doctorDutyRosterStatusResponseDTO,
                            null, searchAppointmentStatus);
                }
            }
        }
    }

    /*IF STATUS IN SEARCH DTO IS NOT EMPTY, THEN RETURN ONLY APPOINTMENT DETAILS WITH RESPECTIVE STATUS.
        NO NEED TO FILTER WITH DOCTOR DUTY ROSTER RANGE*/
    private void setDoctorTimeSlotForSelectedAppointmentStatus(List<DoctorDutyRosterStatusResponseDTO> doctorDutyRosterStatusResponseDTOS,
                                                               List<AppointmentStatusResponseDTO> appointments) {

        for (DoctorDutyRosterStatusResponseDTO doctorDutyRosterStatusResponseDTO : doctorDutyRosterStatusResponseDTOS) {

            doctorDutyRosterStatusResponseDTO.setWeekDayName(
                    doctorDutyRosterStatusResponseDTO.getDate().getDayOfWeek().toString());

            if (doctorDutyRosterStatusResponseDTO.getDayOffStatus().equals(NO)) {
                List<DoctorTimeSlotResponseDTO> doctorTimeSlots = calculateTimeSlotsForSelectedAppointmentStatus(appointments);
                doctorDutyRosterStatusResponseDTO.setDoctorTimeSlots(doctorTimeSlots);
            }
        }
    }

    private boolean hasAppointment(AppointmentStatusResponseDTO appointment,
                                   DoctorDutyRosterStatusResponseDTO doctorDutyRosterStatusResponseDTO) {

        return appointment.getDate().equals(doctorDutyRosterStatusResponseDTO.getDate())
                && (appointment.getDoctorId().equals(doctorDutyRosterStatusResponseDTO.getDoctorId()))
                && (appointment.getSpecializationId().equals(doctorDutyRosterStatusResponseDTO.getSpecializationId()));
    }

    private static void setTimeSlotForAllAppointmentStatus
            (DoctorDutyRosterStatusResponseDTO doctorDutyRosterStatusResponseDTO,
             String appointmentTimeDetails,
             String searchAppointmentStatus) {

        List<DoctorTimeSlotResponseDTO> doctorTimeSlots =
                calculateTimeSlotsForAllAppointmentStatus(doctorDutyRosterStatusResponseDTO.getStartTime(),
                        doctorDutyRosterStatusResponseDTO.getEndTime(),
                        doctorDutyRosterStatusResponseDTO.getRosterGapDuration(),
                        appointmentTimeDetails,
                        searchAppointmentStatus);

        doctorDutyRosterStatusResponseDTO.setDoctorTimeSlots(doctorTimeSlots);
    }

    /*IF DOCTOR DUTY ROSTERS IS NOT SET UP, THEN THROW EXCEPTION*/
    private void validateDoctorDutyRosterStatus(List<DoctorDutyRosterStatusResponseDTO> doctorDutyRosterStatus) {
        if (doctorDutyRosterStatus.isEmpty())
            throw new NoContentFoundException(DoctorDutyRoster.class);
    }
}
