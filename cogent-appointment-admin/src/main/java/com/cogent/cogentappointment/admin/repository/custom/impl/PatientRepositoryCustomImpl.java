package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.repository.custom.PatientRepositoryCustom;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.List;

import static com.cogent.cogentappointment.admin.query.PatientQuery.QUERY_TO_FETCH_LATEST_REGISTRATION_NUMBER;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.createNativeQuery;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.createQuery;

/**
 * @author smriti ON 16/01/2020
 */
@Repository
@Transactional
public class PatientRepositoryCustomImpl implements PatientRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public String fetchLatestRegistrationNumber() {
        Query query = createNativeQuery.apply(entityManager, QUERY_TO_FETCH_LATEST_REGISTRATION_NUMBER);

        List results = query.getResultList();

        return results.isEmpty() ? null : results.get(0).toString();
    }
}
