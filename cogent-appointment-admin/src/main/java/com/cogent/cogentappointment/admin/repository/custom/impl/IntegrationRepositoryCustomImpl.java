package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.request.IntegrationClient.ClientApiIntegrationSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.adminModeIntegration.ApiQueryParametersResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.adminModeIntegration.ApiRequestHeaderResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.adminModeIntegration.FeatureIntegrationResponse;
import com.cogent.cogentappointment.admin.dto.response.clientIntegration.ClientApiIntegrationResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.clientIntegration.ClientApiIntegrationSearchDTO;
import com.cogent.cogentappointment.admin.dto.response.clientIntegration.ClientApiIntegrationSearchResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.clientIntegration.clientIntegrationUpdate.ApiQueryParametersUpdateResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.clientIntegration.clientIntegrationUpdate.ApiRequestHeaderUpdateResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.query.IntegrationQuery;
import com.cogent.cogentappointment.admin.repository.custom.IntegrationRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.ApiQueryParameters;
import com.cogent.cogentappointment.persistence.model.ApiRequestHeader;
import com.cogent.cogentappointment.persistence.model.ClientFeatureIntegration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.admin.constants.QueryConstants.*;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.admin.log.constants.IntegrationLog.*;
import static com.cogent.cogentappointment.admin.query.IntegrationQuery.*;
import static com.cogent.cogentappointment.admin.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.*;

/**
 * @author rupak on 2020-05-20
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class IntegrationRepositoryCustomImpl implements IntegrationRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<FeatureIntegrationResponse> fetchAdminModeIntegrationResponseDTO(Long appointmentModeId) {
        Query query = createQuery.apply(entityManager, ADMIN_MODE_FEATURES_INTEGRATION_API_QUERY)
                .setParameter(APPOINTMENT_MODE_ID, appointmentModeId);

        List<FeatureIntegrationResponse> responseDTOList =
                transformQueryToResultList(query, FeatureIntegrationResponse.class);

        if (responseDTOList.isEmpty())
            throw CLIENT_API_INTEGRATION_NOT_FOUND.get();

        else {
            return responseDTOList;
        }


    }

    @Override
    public Map<String, String> findAdminModeApiRequestHeaders(Long apiIntegrationFormatId) {
        Query query = createQuery.apply(entityManager, IntegrationQuery.ADMIN_MODE_API_FEAUTRES_HEADERS_QUERY)
                .setParameter(CLIENT_API_INTEGRATION_FORMAT_ID, apiIntegrationFormatId);

        List<ApiRequestHeaderResponseDTO> requestHeaderResponseDTO =
                transformQueryToResultList(query, ApiRequestHeaderResponseDTO.class);

        Map<String, String> map = new HashMap<>();
        requestHeaderResponseDTO.forEach(response -> {
            map.put(response.getKeyParam(), response.getValueParam());
        });

        return map;
    }

    @Override
    public Map<String, String> findAdminModeApiQueryParameters(Long apiIntegrationFormatId) {
        Query query = createQuery.apply(entityManager, ADMIN_MODE_API_PARAMETERS_QUERY)
                .setParameter(CLIENT_API_INTEGRATION_FORMAT_ID, apiIntegrationFormatId);

        List<ApiQueryParametersResponseDTO> parametersResponseDTO =
                transformQueryToResultList(query, ApiQueryParametersResponseDTO.class);

        Map<String, String> map = new HashMap<>();
        parametersResponseDTO.forEach(response -> {
            map.put(response.getKeyParam(), response.getValueParam());
        });

        return map;
    }

    @Override
    public ClientApiIntegrationSearchDTO searchClientApiIntegration(ClientApiIntegrationSearchRequestDTO searchRequestDTO, Pageable pageable) {

        Query query = createQuery.apply(entityManager, CLIENT_API_INTEGRATION_SEARCH_QUERY.apply(searchRequestDTO));

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<ClientApiIntegrationSearchResponseDTO> apiIntegrationSearchResponseDTOList =
                transformQueryToResultList(query, ClientApiIntegrationSearchResponseDTO.class);

        ClientApiIntegrationSearchDTO integrationSearchDTO = new ClientApiIntegrationSearchDTO();
        if (apiIntegrationSearchResponseDTOList.isEmpty())
            throw CLIENT_API_INTEGRATION_NOT_FOUND.get();

        else {
            integrationSearchDTO.setSearchResponseDTOS(apiIntegrationSearchResponseDTOList);
            integrationSearchDTO.setTotalItems(totalItems);
            return integrationSearchDTO;
        }

    }

    @Override
    public ClientApiIntegrationResponseDTO findClientApiIntegration(Long id) {
        Query query = createQuery.apply(entityManager, CLIENT_FEATURES_INTEGRATION_DETAILS_API_QUERY)
                .setParameter(CLIENT_FEATURE_INTEGRATION_ID, id);


        try {
            return transformQueryToSingleResult(query, ClientApiIntegrationResponseDTO.class);
        } catch (NoResultException e) {
            throw CLIENT_API_FEATURE_INTEGRATION.apply(id);
        }

    }

    @Override
    public List<ApiRequestHeaderUpdateResponseDTO> findApiRequestHeadersForUpdate(Long featureId) {

        Query query = createQuery.apply(entityManager, IntegrationQuery.CLIENT_API_FEATURES_HEADERS_QUERY)
                .setParameter(API_FEATURE__ID, featureId);

        List<ApiRequestHeaderUpdateResponseDTO> apiRequestHeaderUpdateResponseDTOS =
                transformQueryToResultList(query, ApiRequestHeaderUpdateResponseDTO.class);

        if (apiRequestHeaderUpdateResponseDTOS.isEmpty())
            throw CLIENT_API_REQUEST_HEADER_NOT_FOUND.get();

        else {
            return apiRequestHeaderUpdateResponseDTOS;
        }


    }

    @Override
    public List<ApiQueryParametersUpdateResponseDTO> findApiQueryParametersForUpdate(Long featureId) {
        Query query = createQuery.apply(entityManager, IntegrationQuery.CLIENT_API_PARAMETERS_QUERY)
                .setParameter(API_FEATURE__ID, featureId);

        List<ApiQueryParametersUpdateResponseDTO> apiQueryParametersUpdateResponseDTOS =
                transformQueryToResultList(query, ApiQueryParametersUpdateResponseDTO.class);

        if (apiQueryParametersUpdateResponseDTOS.isEmpty())
            throw CLIENT_API_QUERY_PARAMETERS_NOT_FOUND.get();

        else {
            return apiQueryParametersUpdateResponseDTOS;
        }

    }

    @Override
    public Map<String, String> findApiRequestHeaders(Long featureId) {
        Query query = createQuery.apply(entityManager, IntegrationQuery.CLIENT_API_FEATURES_HEADERS_QUERY)
                .setParameter(API_FEATURE__ID, featureId);

        List<ApiRequestHeaderResponseDTO> requestHeaderResponseDTO =
                transformQueryToResultList(query, ApiRequestHeaderResponseDTO.class);

        Map<String, String> map = new HashMap<>();
        requestHeaderResponseDTO.forEach(response -> {
            map.put(response.getKeyParam(), response.getValueParam());
        });

        return map;
    }

    @Override
    public Map<String, String> findApiQueryParameters(Long featureId) {
        Query query = createQuery.apply(entityManager, IntegrationQuery.CLIENT_API_PARAMETERS_QUERY)
                .setParameter(API_FEATURE__ID, featureId);

        List<ApiQueryParametersResponseDTO> parametersResponseDTO =
                transformQueryToResultList(query, ApiQueryParametersResponseDTO.class);

        Map<String, String> map = new HashMap<>();
        parametersResponseDTO.forEach(response -> {
            map.put(response.getKeyParam(), response.getValueParam());
        });

        return map;
    }


    private Function<Long, NoContentFoundException> CLIENT_API_FEATURE_INTEGRATION = (id) -> {
        log.error(CONTENT_NOT_FOUND, CLIENT_FEATURE_INTEGRATION);
        throw new NoContentFoundException
                (ClientFeatureIntegration.class, "id", id.toString());
    };

    private Supplier<NoContentFoundException> CLIENT_API_INTEGRATION_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, CLIENT_FEATURE_INTEGRATION);
        throw new NoContentFoundException(ClientFeatureIntegration.class);
    };

    private Supplier<NoContentFoundException> CLIENT_API_REQUEST_HEADER_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, API_REQUEST_HEADER);
        throw new NoContentFoundException(ApiRequestHeader.class);
    };

    private Supplier<NoContentFoundException> CLIENT_API_QUERY_PARAMETERS_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, API_QUERY_PARAMETERS);
        throw new NoContentFoundException(ApiQueryParameters.class);
    };


}
