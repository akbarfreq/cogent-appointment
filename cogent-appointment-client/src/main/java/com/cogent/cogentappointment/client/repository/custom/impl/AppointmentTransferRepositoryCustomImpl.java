package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.request.appointmentTransfer.AppointmentTransferSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.AppointmentTransferLog.AppointmentTransferLogDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.AppointmentTransferLog.PreviousAppointmentDetails;
import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.availableDates.DoctorDatesResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.availableTime.ActualDateAndTimeResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.availableTime.OverrideDateAndTimeResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.availableTime.WeekDayAndTimeDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.charge.AppointmentChargeResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.custom.AppointmentTransferRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.DoctorDutyRoster;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.client.constants.QueryConstants.AppointmentConstants.APPOINTMENT_ID;
import static com.cogent.cogentappointment.client.constants.QueryConstants.*;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.client.log.constants.DoctorDutyRosterLog.DOCTOR_DUTY_ROSTER;
import static com.cogent.cogentappointment.client.query.AppointmentTransferQuery.*;
import static com.cogent.cogentappointment.client.utils.AppointmentTransferUtils.parsePreviousData;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.*;

/**
 * @author Sauravi Thapa ON 5/6/20
 */

@Repository
@Slf4j
@Transactional(readOnly = true)
public class AppointmentTransferRepositoryCustomImpl implements AppointmentTransferRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<DoctorDatesResponseDTO> getDatesByDoctorId(Long doctorId, Long specializationId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DATES_BY_DOCTOR_ID)
                .setParameter(DOCTOR_ID, doctorId)
                .setParameter(SPECIALIZATION_ID, specializationId);

        List<DoctorDatesResponseDTO> response = transformQueryToResultList(query, DoctorDatesResponseDTO.class);

        return response;
    }

    @Override
    public List<String> getDayOffDaysByRosterId(Long doctorDutyRosterId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_GET_DAY_OFF_WEEKS_BY_DUTY_ROSTER_ID)
                .setParameter(DOCTOR_DUTY_ROSTER_ID, doctorDutyRosterId);

        List<String> response = query.getResultList();

        return response;
    }

    @Override
    public WeekDayAndTimeDTO getWeekDaysByCode(Long doctorId, String code) {
        Query query = createQuery.apply(entityManager, QUERY_TO_GET_WEEKS_BY_DUTY_ROSTER_ID)
                .setParameter(DOCTOR_ID, doctorId)
                .setParameter(CODE, code);

        try {
            return transformQueryToSingleResult(query, WeekDayAndTimeDTO.class);
        } catch (NoResultException e) {
            throw DOCTOR_DUTY_ROSTER_NOT_FOUND.get();
        }

    }

    @Override
    public List<DoctorDatesResponseDTO> getOverrideDatesByDoctorId(Long doctorId, Long specializationId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_GET_OVERRIDE_DATES_BY_DOCTOR_ID)
                .setParameter(DOCTOR_ID, doctorId)
                .setParameter(SPECIALIZATION_ID, specializationId);

        List<DoctorDatesResponseDTO> response = transformQueryToResultList(query, DoctorDatesResponseDTO.class);

        return response;
    }

    @Override
    public List<ActualDateAndTimeResponseDTO> getActualTimeByDoctorId(Long doctorId, Long specializationId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DATE_AND_TIME_BY_DOCTOR_ID)
                .setParameter(DOCTOR_ID, doctorId)
                .setParameter(SPECIALIZATION_ID, specializationId);

        List<ActualDateAndTimeResponseDTO> response = transformQueryToResultList(query, ActualDateAndTimeResponseDTO.class);

        if (response.isEmpty()) {
            throw DOCTOR_DUTY_ROSTER_NOT_FOUND.get();
        }

        return response;
    }

    @Override
    public List<String> getUnavailableTimeByDateAndDoctorId(Long doctorId, Long specializationId, Date date) {
        Query query = createQuery.apply(entityManager, QUERY_TO_GET_UNAVAILABLE_TIME)
                .setParameter(DOCTOR_ID, doctorId)
                .setParameter(SPECIALIZATION_ID, specializationId)
                .setParameter(DATE, date);

        List<String> response = query.getResultList();

        return response;
    }

    @Override
    public AppointmentChargeResponseDTO getAppointmentChargeByDoctorId(Long doctorId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_GET_DOCTOR_CHARGE_BY_DOCTOR_ID)
                .setParameter(DOCTOR_ID, doctorId);

        AppointmentChargeResponseDTO response = transformQueryToSingleResult(query, AppointmentChargeResponseDTO.class);

        return response;
    }

    @Override
    public List<OverrideDateAndTimeResponseDTO> getOverideRosterDateAndTime(Long doctorId, Long specializationId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_OVERRIDE_DATE_AND_TIME_BY_DOCTOR_ID)
                .setParameter(DOCTOR_ID, doctorId)
                .setParameter(SPECIALIZATION_ID, specializationId);

        List<OverrideDateAndTimeResponseDTO> response = transformQueryToResultList(query, OverrideDateAndTimeResponseDTO.class);

        return response;

    }

    @Override
    public List<AppointmentTransferLogDTO> getFinalAppTransferredInfo(AppointmentTransferSearchRequestDTO requestDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_GET_CURRENT_TRANSFERRED_DETAIL(requestDTO));

        List<AppointmentTransferLogDTO> finalList=new ArrayList<>();

        List<AppointmentTransferLogDTO> responses = transformQueryToResultList(query, AppointmentTransferLogDTO.class);

        responses.forEach(response -> {
            Query queryForpreviousInfos = createQuery.apply(entityManager, QUERY_TO_GET_PREVIOUS_INFOS)
                    .setParameter(APPOINTMENT_ID, response.getAppointmentId());
            finalList.add(response);
            if (queryForpreviousInfos.getResultList().size() > 1) {
                List<PreviousAppointmentDetails> previousInfo = transformQueryToResultList(queryForpreviousInfos,
                        PreviousAppointmentDetails.class);
                finalList.addAll(parsePreviousData(previousInfo,response));
            }
        });


        return finalList;
    }

    private Supplier<NoContentFoundException> DOCTOR_DUTY_ROSTER_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, DOCTOR_DUTY_ROSTER);
        throw new NoContentFoundException(DoctorDutyRoster.class);
    };
}
