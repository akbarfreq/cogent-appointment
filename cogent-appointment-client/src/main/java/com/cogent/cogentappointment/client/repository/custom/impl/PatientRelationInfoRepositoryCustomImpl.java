package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.repository.custom.PatientRelationInfoRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.PatientRelationInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import static com.cogent.cogentappointment.client.constants.QueryConstants.PatientQueryConstants.CHILD_PATIENT_ID;
import static com.cogent.cogentappointment.client.constants.QueryConstants.PatientQueryConstants.PARENT_PATIENT_ID;
import static com.cogent.cogentappointment.client.query.PatientRelationInfoQuery.QUERY_TO_FETCH_PATIENT_RELATION_INFO;

/**
 * @author smriti on 28/02/20
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class PatientRelationInfoRepositoryCustomImpl implements PatientRelationInfoRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public PatientRelationInfo fetchPatientRelationInfo(Long parentPatientId, Long childPatientId) {
        try {
            return entityManager.createQuery(QUERY_TO_FETCH_PATIENT_RELATION_INFO, PatientRelationInfo.class)
                    .setParameter(PARENT_PATIENT_ID, parentPatientId)
                    .setParameter(CHILD_PATIENT_ID, childPatientId)
                    .getSingleResult();
        } catch (NoResultException ex) {
            log.error(ex.getLocalizedMessage());
            return null;
        }
    }
}
