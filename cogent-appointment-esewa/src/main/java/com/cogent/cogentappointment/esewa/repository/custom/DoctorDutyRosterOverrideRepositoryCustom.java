package com.cogent.cogentappointment.esewa.repository.custom;

import com.cogent.cogentappointment.esewa.dto.response.appointmentDetails.AvailableDoctorWithSpecialization;
import com.cogent.cogentappointment.esewa.dto.response.appointmentDetails.DoctorAvailabilityStatusResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointmentDetails.DutyRosterOverrideAppointmentDate;
import com.cogent.cogentappointment.esewa.dto.request.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.doctorDutyRoster.DoctorDutyRosterOverrideUpdateRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.eSewa.AppointmentDetailRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.appoinmentDateAndTime.DoctorDutyRosterOverrideAppointmentDate;
import com.cogent.cogentappointment.esewa.dto.response.doctorDutyRoster.DoctorDutyRosterStatusResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.doctorDutyRoster.DoctorDutyRosterTimeResponseDTO;
import com.cogent.cogentappointment.persistence.model.DoctorDutyRosterOverride;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@Qualifier("doctorDutyRosterOverrideRepositoryCustom")
public interface DoctorDutyRosterOverrideRepositoryCustom {

    Long fetchOverrideCount(Long doctorId, Long specializationId, Date fromDate, Date toDate);

    Long fetchOverrideCount(Long doctorDutyRosterOverrideId, Long doctorId,
                            Long specializationId, Date fromDate, Date toDate);

    DoctorDutyRosterTimeResponseDTO fetchDoctorDutyRosterOverrideTime(Date date,
                                                                      Long doctorId,
                                                                      Long specializationId);

    List<DoctorDutyRosterOverride> fetchDoctorDutyRosterOverrides(
            List<DoctorDutyRosterOverrideUpdateRequestDTO> updateRequestDTOS);

    List<DoctorDutyRosterStatusResponseDTO> fetchDoctorDutyRosterOverrideStatus
            (AppointmentStatusRequestDTO requestDTO, Long hospitalId);

    List<DoctorDutyRosterOverrideAppointmentDate> getRosterOverrideByRosterId(Long doctorDutyRosterId);

    List<DoctorDutyRosterOverrideAppointmentDate> getRosterOverrideByDoctorAndSpecializationId(Long doctorId,
                                                                                               Long specializationId);

    DoctorAvailabilityStatusResponseDTO fetchDoctorDutyRosterOverrideStatus(AppointmentDetailRequestDTO requestDTO);

    List<DutyRosterOverrideAppointmentDate> fetchDayOffRosterOverridebyRosterId(Long rosterId);

    List<AvailableDoctorWithSpecialization> fetchAvailableDoctor(AppointmentDetailRequestDTO requestDTO);

}
