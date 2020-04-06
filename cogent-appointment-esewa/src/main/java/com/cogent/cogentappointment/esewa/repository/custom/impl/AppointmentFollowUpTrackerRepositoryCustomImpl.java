package com.cogent.cogentappointment.esewa.repository.custom.impl;

import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.repository.custom.AppointmentFollowUpTrackerRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AppointmentFollowUpTracker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import static com.cogent.cogentappointment.esewa.constants.QueryConstants.AppointmentFollowUpTrackerConstants.PARENT_APPOINTMENT_ID;
import static com.cogent.cogentappointment.esewa.constants.QueryConstants.*;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.esewa.log.constants.AppointmentFollowUpTrackerLog.APPOINTMENT_FOLLOW_UP_TRACKER;
import static com.cogent.cogentappointment.esewa.query.AppointmentFollowUpTrackerQuery.QUERY_TO_FETCH_APPOINTMENT_FOLLOW_UP_TRACKER;
import static com.cogent.cogentappointment.esewa.query.AppointmentFollowUpTrackerQuery.QUERY_TO_FETCH_LATEST_APPOINTMENT_FOLLOW_UP_TRACKER;

/**
 * @author smriti on 18/11/2019
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class AppointmentFollowUpTrackerRepositoryCustomImpl implements AppointmentFollowUpTrackerRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public AppointmentFollowUpTracker fetchAppointmentFollowUpTracker(Long patientId,
                                                                      Long doctorId,
                                                                      Long specializationId,
                                                                      Long hospitalId) {
        try {
            return entityManager.createQuery(
                    QUERY_TO_FETCH_APPOINTMENT_FOLLOW_UP_TRACKER, AppointmentFollowUpTracker.class)
                    .setParameter(PATIENT_ID, patientId)
                    .setParameter(DOCTOR_ID, doctorId)
                    .setParameter(SPECIALIZATION_ID, specializationId)
                    .setParameter(HOSPITAL_ID, hospitalId)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public AppointmentFollowUpTracker fetchLatestAppointmentFollowUpTracker(Long parentAppointmentId) {
        try {
            return (AppointmentFollowUpTracker) entityManager.createNativeQuery(
                    QUERY_TO_FETCH_LATEST_APPOINTMENT_FOLLOW_UP_TRACKER, AppointmentFollowUpTracker.class)
                    .setParameter(PARENT_APPOINTMENT_ID, parentAppointmentId)
                    .getSingleResult();

        } catch (NoResultException e) {
            log.error(CONTENT_NOT_FOUND,APPOINTMENT_FOLLOW_UP_TRACKER);
            throw new NoContentFoundException(AppointmentFollowUpTracker.class);
        }
    }

}
