package com.cogent.cogentappointment.admin.query;

import com.cogent.cogentappointment.admin.dto.request.integrationAdminMode.AdminModeApiIntegrationSearchRequestDTO;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author rupak ON 2020/06/03-10:42 AM
 */
public class IntegrationAdminModeQuery {

    public static final String ADMIN_MODE_INTEGRATION_DETAILS_API_QUERY =
            "SELECT" +
                    " f.id as featureId," +
                    " am.id as appointmentModeId,"+
                    " am.name as appointmentModeName," +
                    " f.code as featureCode," +
                    " hrm.id as requestMethodId," +
                    " hrm.name as requestMethodName," +
                    " aif.url as url," +
                    " ic.id as integrationChannelId," +
                    " ic.name as integrationChannel," +
                    " ait.id as integrationTypeId," +
                    " ait.name as integrationType," +
                    ADMIN_MODE_API_INTEGRATION_AUDITABLE_QUERY() +
                    " FROM AdminModeFeatureIntegration amfi" +
                    " LEFT JOIN AdminModeApiFeatureIntegration amafi ON amafi.adminModeFeatureIntegrationId.id =amfi.id " +
                    " LEFT JOIN AppointmentMode am ON am.id=amfi.appointmentModeId.id" +
                    " LEFT JOIN Feature f ON f.id=amfi.featureId" +
                    " LEFT JOIN ApiIntegrationFormat aif ON aif.id=amafi.apiIntegrationFormatId.id" +
                    " LEFT JOIN HttpRequestMethod hrm ON hrm.id =aif.httpRequestMethodId" +
                    " LEFT JOIN IntegrationChannel ic ON ic.id=amfi.integrationChannelId.id" +
                    " LEFT JOIN ApiIntegrationType ait ON ait.id=f.apiIntegrationTypeId.id" +
                    " WHERE amfi.id= :adminModeFeatureIntegrationId" +
                    " AND aif.status='Y'" +
                    " AND hrm.status='Y'" +
                    " AND amfi.status='Y'" +
                    " AND amafi.status='Y'" +
                    " AND amafi.status='Y'" +
                    " AND f.status='Y'" +
                    " AND ic.status='Y'" +
                    " AND ait.status='Y'";

    public static final String ADMIN_MODE_FEATURES_HEADERS_QUERY =
            " SELECT " +
                    " amrh.id as id," +
                    " amrh.keyName as keyParam," +
                    " amrh.value as valueParam," +
                    " amrh.description as description," +
                    " amrh.status as status" +
                    " FROM AdminModeFeatureIntegration amfi" +
                    " LEFT JOIN AdminModeApiFeatureIntegration amafi ON amafi.adminModeFeatureIntegrationId.id =amfi.id " +
                    " LEFT JOIN Feature f ON f.id=amfi.featureId" +
                    " LEFT JOIN ApiIntegrationFormat aif ON aif.id=amafi.apiIntegrationFormatId.id" +
                    " LEFT JOIN AdminModeRequestHeader amrh ON amrh.apiIntegrationFormatId=aif.id" +
                    " WHERE f.id=:featureId" +
                    " AND amfi.status='Y'" +
                    " AND amafi.status='Y'" +
                    " AND aif.status='Y'" +
                    " AND amrh.status='Y'" +
                    " AND f.status='Y'";

    public static final String ADMIN_MODE_QUERY_PARAMETERS_QUERY =
            " SELECT " +
                    " amqp.id as id," +
                    " amqp.param as keyParam," +
                    " amqp.value as valueParam," +
                    " amqp.status as status," +
                    " amqp.description as description" +
                    " FROM AdminModeFeatureIntegration amfi" +
                    " LEFT JOIN AdminModeApiFeatureIntegration amafi ON amafi.adminModeFeatureIntegrationId.id =amfi.id " +
                    " LEFT JOIN ApiIntegrationFormat aif ON aif.id=amafi.apiIntegrationFormatId.id" +
                    " LEFT JOIN AdminModeQueryParameters amqp ON amqp.apiIntegrationFormatId =aif.id" +
                    " LEFT JOIN Feature f ON f.id=amfi.featureId" +
                    " WHERE f.id=:featureId" +
                    " AND amfi.status='Y'" +
                    " AND amafi.status='Y'" +
                    " AND amqp.status='Y'" +
                    " AND f.status='Y'";

//    public static final String ADMIN_MODE_INTEGRATION_FEATURES_REQUEST_HEADERS_QUERY =
//            " SELECT " +
//                    " amrh.id as id," +
//                    " amrh.keyName as keyParam," +
//                    " amrh.value as valueParam," +
//                    " amrh.description as description," +
//                    " amrh.status as status" +
//                    " FROM AdminModeFeatureIntegration amfi" +
//                    " LEFT JOIN Feature f ON f.id=amfi.featureId" +
//                    " LEFT JOIN AdminModeApiFeatureIntegration amafi ON amafi.adminModeFeatureIntegrationId.id =amfi.id " +
//                    " LEFT JOIN ApiIntegrationFormat aif ON aif.id=amafi.apiIntegrationFormatId.id" +
//                    " LEFT JOIN AdminModeRequestHeader amrh ON amrh.apiIntegrationFormatId=aif.id" +
//                    " WHERE f.id=:featureId" +
//                    " AND aif.status='Y'" +
//                    " AND amrh.status='Y'" +
//                    " AND amfi.status='Y'" +
//                    " AND amafi.status='Y'" +
//                    " AND f.status='Y'";


    public static Function<AdminModeApiIntegrationSearchRequestDTO, String> ADMIN_MODE_API_INTEGRATION_SEARCH_QUERY =
            (searchRequestDTO) ->
                    " SELECT" +
                            " amfi.id as id," +
                            " am.name as appointmentMode," +
                            " f.name as featureName," +
                            " f.code as featureCode," +
                            " hrm.name as requestMethod," +
                            " aif.url as url" +
                            " FROM AdminModeFeatureIntegration amfi" +
                            " LEFT JOIN AdminModeApiFeatureIntegration amafi ON amafi.adminModeFeatureIntegrationId.id =amfi.id " +
                            " LEFT JOIN AppointmentMode am ON am.id=amfi.appointmentModeId.id" +
                            " LEFT JOIN Feature f ON f.id=amfi.featureId" +
                            " LEFT JOIN ApiIntegrationType ait ON ait.id=f.apiIntegrationTypeId.id" +
                            " LEFT JOIN ApiIntegrationFormat aif ON aif.id=amafi.apiIntegrationFormatId.id" +
                            " LEFT JOIN HttpRequestMethod hrm ON hrm.id =aif.httpRequestMethodId"
                            + GET_WHERE_CLAUSE_TO_SEARCH_ADMIN_MODE_API_INTEGRATION(searchRequestDTO);

    private static String GET_WHERE_CLAUSE_TO_SEARCH_ADMIN_MODE_API_INTEGRATION(
            AdminModeApiIntegrationSearchRequestDTO requestSearchDTO) {

        String whereClause = " WHERE" +
                " aif.status='Y'" +
                " AND hrm.status='Y'" +
                " AND amafi.status='Y'" +
                " AND f.status='Y'" +
                " AND amfi.status='Y'";

//        if (!Objects.isNull(requestSearchDTO.getCompanyId()))
//            whereClause += " AND cfi.hospitalId=" + requestSearchDTO.getCompanyId();

        if (!Objects.isNull(requestSearchDTO.getAppointmentModeId()))
            whereClause += " AND amfi.appointmentModeId.id=" + requestSearchDTO.getAppointmentModeId();

        if (!Objects.isNull(requestSearchDTO.getFeatureTypeId()))
            whereClause += " AND amfi.featureId=" + requestSearchDTO.getFeatureTypeId();

        if (!Objects.isNull(requestSearchDTO.getRequestMethodId()))
            whereClause += " AND hrm.id=" + requestSearchDTO.getRequestMethodId();

        if (!Objects.isNull(requestSearchDTO.getApiIntegrationTypeId()))
            whereClause += " AND ait.id=" + requestSearchDTO.getApiIntegrationTypeId();

        if (!ObjectUtils.isEmpty(requestSearchDTO.getUrl()))
            whereClause += " AND aif.url like %'" + requestSearchDTO.getUrl() + "'%";


        return whereClause;
    }

    public static String ADMIN_MODE_API_INTEGRATION_AUDITABLE_QUERY() {
        return " amfi.createdBy as createdBy," +
                " amfi.createdDate as createdDate," +
                " amfi.lastModifiedBy as lastModifiedBy," +
                " amfi.lastModifiedDate as lastModifiedDate";
    }

}
