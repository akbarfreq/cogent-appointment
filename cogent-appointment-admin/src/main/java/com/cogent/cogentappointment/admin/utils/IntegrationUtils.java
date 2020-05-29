package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.clientIntegration.ApiIntegrationFormatRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.clientIntegration.ClientApiHeadersRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.clientIntegration.ClientApiQueryParametersRequestDTO;
import com.cogent.cogentappointment.persistence.model.*;

import java.util.ArrayList;
import java.util.List;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.ACTIVE;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.DELETED;

/**
 * @author rupak on 2020-05-19
 */
public class IntegrationUtils {
    public static ClientFeatureIntegration parseToClientFeatureIntegration(Long hospitalId,
                                                                           Long featureTypeId) {

        ClientFeatureIntegration clientFeatureIntegration = new ClientFeatureIntegration();
        clientFeatureIntegration.setFeatureId(featureTypeId);
        clientFeatureIntegration.setHospitalId(hospitalId);
        clientFeatureIntegration.setStatus(ACTIVE);

        return clientFeatureIntegration;
    }

    public static ApiIntegrationFormat parseToClientApiIntegrationFormat(ApiIntegrationFormatRequestDTO requestDTO) {

        ApiIntegrationFormat apiIntegrationFormat = new ApiIntegrationFormat();
        apiIntegrationFormat.setHttpRequestBodyAttributes(requestDTO.getRequestBodyAttrribute());
        apiIntegrationFormat.setHttpRequestMethodId(requestDTO.getRequestMethodId());
        apiIntegrationFormat.setUrl(requestDTO.getApiUrl());
        apiIntegrationFormat.setStatus(ACTIVE);

        return apiIntegrationFormat;
    }

    public static List<ApiQueryParameters> parseToClientApiQueryParameters(List<ClientApiQueryParametersRequestDTO> parametersRequestDTOS, Long apiIntegrationFormatId) {
        List<ApiQueryParameters> apiQueryParametersList = new ArrayList<>();

        parametersRequestDTOS.forEach(request -> {
            ApiQueryParameters parameter = new ApiQueryParameters();
            parameter.setApiIntegrationFormatId(apiIntegrationFormatId);
            parameter.setParam(request.getKeyParam());
            parameter.setValue(request.getValueParam());
            parameter.setDescription(request.getDescription());
            parameter.setStatus(ACTIVE);

            apiQueryParametersList.add(parameter);
        });

        return apiQueryParametersList;
    }

    public static ApiFeatureIntegration parseToClientApiFeatureIntegration(Long clientFeatureIntegrationId,
                                                                           Long apiIntegrationFormatId,
                                                                           Long integrationChannelId) {

        ApiFeatureIntegration apiFeatureIntegration = new ApiFeatureIntegration();
        apiFeatureIntegration.setApiIntegrationFormatId(apiIntegrationFormatId);
        apiFeatureIntegration.setClientFeatureIntegrationId(apiIntegrationFormatId);
        apiFeatureIntegration.setIntegrationChannelId(integrationChannelId);
        apiFeatureIntegration.setStatus(ACTIVE);

        return apiFeatureIntegration;
    }

    public static List<ApiRequestHeader> parseToClientApiRequestHeaders(List<ClientApiHeadersRequestDTO> clientApiRequestHeaders,
                                                                        Long id) {

        List<ApiRequestHeader> requestHeaderList = new ArrayList<>();

        clientApiRequestHeaders.forEach(requestDTO -> {
            ApiRequestHeader requestHeader = new ApiRequestHeader();
            requestHeader.setApiIntegrationFormatId(id);
            requestHeader.setKeyName(requestDTO.getKeyParam());
            requestHeader.setValue(requestDTO.getValueParam());
            requestHeader.setDescription(requestDTO.getDescription());
            requestHeader.setStatus(ACTIVE);

            requestHeaderList.add(requestHeader);
        });

        return requestHeaderList;

    }

    public static AdminModeFeatureIntegration parseToAdminModeFeatureIntegration(AppointmentMode appointmentMode,
                                                                                 Long featureTypeId) {

        AdminModeFeatureIntegration adminModeFeatureIntegration = new AdminModeFeatureIntegration();
        adminModeFeatureIntegration.setFeatureId(featureTypeId);
        adminModeFeatureIntegration.setAppointmentModeId(appointmentMode);
        adminModeFeatureIntegration.setStatus(ACTIVE);

        return adminModeFeatureIntegration;
    }

    public static AdminModeApiFeatureIntegration parseToAdminModeApiFeatureIntegration(
            AdminModeFeatureIntegration adminModeApiFeatureIntegration
            , ApiIntegrationFormat apiIntegrationFormat) {

        AdminModeApiFeatureIntegration modeApiFeatureIntegration = new AdminModeApiFeatureIntegration();
        modeApiFeatureIntegration.setAdminModeFeatureIntegrationId(adminModeApiFeatureIntegration);
        modeApiFeatureIntegration.setApiIntegrationFormatId(apiIntegrationFormat);
        modeApiFeatureIntegration.setStatus(ACTIVE);

        return modeApiFeatureIntegration;

    }

    public static void parseToDeletedClientFeatureIntegration(ClientFeatureIntegration clientFeatureIntegration,
                                                              DeleteRequestDTO deleteRequestDTO) {

        clientFeatureIntegration.setStatus(deleteRequestDTO.getStatus());
        clientFeatureIntegration.setRemarks(deleteRequestDTO.getRemarks());

    }

    public static void parseToDeletedApiFeatureIntegration(List<ApiFeatureIntegration> apiFeatureIntegrationList) {

        apiFeatureIntegrationList.forEach(apiFeatureIntegration -> {
            apiFeatureIntegration.setStatus(DELETED);
        });
    }

    public static List<ApiFeatureIntegrationRequestBodyParameters>
    parseToClientApiIntegrationRequestBodyAttributes(ApiIntegrationFormat apiIntegrationFormat,
                                                     List<ApiIntegrationRequestBodyParameters> requestBodyParameters) {


        List<ApiFeatureIntegrationRequestBodyParameters> featureIntegrationRequestBodyParameters
                = new ArrayList<>();

        requestBodyParameters.forEach(requestBody -> {
            ApiFeatureIntegrationRequestBodyParameters featureBodyParameters
                    = new ApiFeatureIntegrationRequestBodyParameters();
            featureBodyParameters.setApiIntegrationFormatId(apiIntegrationFormat);
            featureBodyParameters.setRequestBodyParametersId(requestBody);
            featureBodyParameters.setStatus(ACTIVE);

            featureIntegrationRequestBodyParameters.add(featureBodyParameters);
        });

        return featureIntegrationRequestBodyParameters;

    }
}
