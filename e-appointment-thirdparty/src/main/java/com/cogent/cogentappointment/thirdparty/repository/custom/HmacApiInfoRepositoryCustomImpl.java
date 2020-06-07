package com.cogent.cogentappointment.thirdparty.repository.custom;

import com.cogent.cogentappointment.thirdparty.dto.request.login.ThirdPartyDetail;
import com.cogent.cogentappointment.thirdparty.exception.NoContentFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import static com.cogent.cogentappointment.thirdparty.constants.ErrorMessageConstants.INVALID_COMPANY_CODE;
import static com.cogent.cogentappointment.thirdparty.constants.QueryConstants.API_KEY;
import static com.cogent.cogentappointment.thirdparty.constants.QueryConstants.COMPANY_CODE;
import static com.cogent.cogentappointment.thirdparty.query.HmacApiInfoQuery.QUERY_TO_FETCH_THIRD_PARTY_INFO_FOR_AUTHENTICATION;
import static com.cogent.cogentappointment.thirdparty.query.HmacApiInfoQuery.QUERY_TO_FETCH_THIRD_PARTY_INFO_FOR_HMAC_GENERATION;
import static com.cogent.cogentappointment.thirdparty.utils.common.QueryUtils.createQuery;
import static com.cogent.cogentappointment.thirdparty.utils.common.QueryUtils.transformQueryToSingleResult;

/**
 * @author Sauravi Thapa २०/२/२
 */

@Repository
@Transactional(readOnly = true)
@Slf4j
public class HmacApiInfoRepositoryCustomImpl implements HmacApiInfoRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public ThirdPartyDetail getDetailForAuthentication(String companyCode, String apiKey) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_THIRD_PARTY_INFO_FOR_AUTHENTICATION)
                .setParameter(API_KEY, apiKey)
                .setParameter(COMPANY_CODE, companyCode);

        try {
            return transformQueryToSingleResult(query, ThirdPartyDetail.class);
        } catch (NoResultException e) {
            log.error(INVALID_COMPANY_CODE);
            throw new NoContentFoundException (INVALID_COMPANY_CODE);
        }
    }

    @Override
    public ThirdPartyDetail getDetailsByHospitalCode(String companyCode) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_THIRD_PARTY_INFO_FOR_HMAC_GENERATION)
                .setParameter(COMPANY_CODE, companyCode);
        try {
            return transformQueryToSingleResult(query, ThirdPartyDetail.class);
        } catch (NoResultException e) {
            log.error(INVALID_COMPANY_CODE);
            throw new NoContentFoundException(INVALID_COMPANY_CODE);
        }
    }

}
