package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.request.clientIntegration.ApiIntegrationFormatRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.clientIntegration.ClientApiHeadersRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.clientIntegration.ClientApiIntegrationRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.clientIntegration.ClientApiQueryParametersRequestDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.*;
import com.cogent.cogentappointment.admin.service.IntegrationService;
import com.cogent.cogentappointment.persistence.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.ACTIVE;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.SAVING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.SAVING_PROCESS_STARTED;
import static com.cogent.cogentappointment.admin.log.constants.IntegrationLog.API_INTEGRATIONS;
import static com.cogent.cogentappointment.admin.utils.IntegrationUtils.parseToClientApiIntegrationFormat;
import static com.cogent.cogentappointment.admin.utils.IntegrationUtils.parseToClientFeatureIntegration;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author rupak on 2020-05-19
 */
@Service
@Transactional
@Slf4j
public class IntegrationServiceImpl implements IntegrationService {

    private final IntegrationRepository integrationRepository;
    private final ClientFeatureIntegrationRepository clientFeatureIntegrationRepository;
    private final ClientApiIntegrationFormatRespository clientApiIntegrationFormatRespository;
    private final HttpRequestMethodRepository httpRequestMethodRepository;
    private final ApiQueryParametersRepository apiQueryParametersRepository;
    private final ApiRequestHeaderRepository apiRequestHeaderRepository;

    public IntegrationServiceImpl(IntegrationRepository integrationRepository, ClientFeatureIntegrationRepository clientFeatureIntegrationRepository, ClientApiIntegrationFormatRespository clientApiIntegrationFormatRespository, HttpRequestMethodRepository httpRequestMethodRepository, ApiQueryParametersRepository apiQueryParametersRepository, ApiRequestHeaderRepository apiRequestHeaderRepository) {
        this.integrationRepository = integrationRepository;
        this.clientFeatureIntegrationRepository = clientFeatureIntegrationRepository;
        this.clientApiIntegrationFormatRespository = clientApiIntegrationFormatRespository;
        this.httpRequestMethodRepository = httpRequestMethodRepository;
        this.apiQueryParametersRepository = apiQueryParametersRepository;
        this.apiRequestHeaderRepository = apiRequestHeaderRepository;
    }

    @Override
    public void save(ClientApiIntegrationRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, API_INTEGRATIONS);

        ClientFeatureIntegration clientFeatureIntegration = parseToClientFeatureIntegration(requestDTO.getHospitalId(), requestDTO.getFeatureTypeId());
        saveClientFeatureIntegration(clientFeatureIntegration);

        HttpRequestMethod httpRequestMethod = httpRequestMethodRepository.httpRequestMethodById(requestDTO.getRequestMethodId())
                .orElseThrow(() -> HTTP_REQUEST_METHOD_WITH_GIVEN_ID_NOT_FOUND.apply(requestDTO.getRequestMethodId()));


        ApiIntegrationFormatRequestDTO apiIntegrationFormatRequestDTO = ApiIntegrationFormatRequestDTO.builder()
                .apiUrl(requestDTO.getApiUrl())
                .requestMethodId(requestDTO.getRequestMethodId())
                .requestBodyAttrribute(requestDTO.getRequestBodyAttrribute())
                .build();

        ApiIntegrationFormat apiIntegrationFormat =
                parseToClientApiIntegrationFormat(apiIntegrationFormatRequestDTO, httpRequestMethod);

        saveApiIntegrationFormat(apiIntegrationFormat);

        saveApiFeatureIntegration(clientFeatureIntegration.getId(), apiIntegrationFormat.getId());

        saveApiQueryParameters(requestDTO.getParametersRequestDTOS(), apiIntegrationFormat);

        saveApiRequestHeaders(requestDTO.getClientApiRequestHeaders(), apiIntegrationFormat);

        log.info(SAVING_PROCESS_COMPLETED, API_INTEGRATIONS, getDifferenceBetweenTwoTime(startTime));
    }

    private void saveApiRequestHeaders(List<ClientApiHeadersRequestDTO> clientApiRequestHeaders, ApiIntegrationFormat apiIntegrationFormat) {

        List<ApiRequestHeader> requestHeaderList = new ArrayList<>();

        clientApiRequestHeaders.forEach(request -> {
            ApiRequestHeader requestHeader = new ApiRequestHeader();
            requestHeader.setApiIntegrationFormatId(apiIntegrationFormat);
            requestHeader.setKey(request.getKey());
            requestHeader.setValue(request.getValue());
            requestHeader.setDescription(request.getDescription());
            requestHeader.setStatus(ACTIVE);

            requestHeaderList.add(requestHeader);
        });

        apiRequestHeaderRepository.saveAll(requestHeaderList);

    }

    private void saveApiQueryParameters(List<ClientApiQueryParametersRequestDTO> parametersRequestDTOS, ApiIntegrationFormat apiIntegrationFormat) {

        List<ApiQueryParameters> apiQueryParametersList = new ArrayList<>();

        parametersRequestDTOS.forEach(request -> {
            ApiQueryParameters parameter = new ApiQueryParameters();
            parameter.setApiIntegrationFormatId(apiIntegrationFormat);
            parameter.setParam(request.getKey());
            parameter.setValue(request.getValue());
            parameter.setDescription(request.getDescription());
            parameter.setStatus(ACTIVE);

            apiQueryParametersList.add(parameter);
        });

        apiQueryParametersRepository.saveAll(apiQueryParametersList);

    }

    private void saveApiFeatureIntegration(Long clientFeatureIntegrationId, Long apiIntegrationFormatId) {
        ApiFeatureIntegration apiFeatureIntegration = new ApiFeatureIntegration();
        apiFeatureIntegration.setApiIntegrationFormatId(apiIntegrationFormatId);
        apiFeatureIntegration.setClientFeatureIntegrationId(apiIntegrationFormatId);

    }

    private void saveApiIntegrationFormat(ApiIntegrationFormat apiIntegrationFormat) {

        clientApiIntegrationFormatRespository.save(apiIntegrationFormat);
    }

    private void saveClientFeatureIntegration(ClientFeatureIntegration clientFeatureIntegration) {

        clientFeatureIntegrationRepository.save(clientFeatureIntegration);
    }

    private Function<Long, NoContentFoundException> HTTP_REQUEST_METHOD_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(HttpRequestMethod.class);
    };
}
