package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.response.integrationClient.clientIntegrationUpdate.ApiRequestHeaderUpdateResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Qualifier("adminModeRequestHeaderRepositoryCustom")
public interface AdminModeRequestHeaderRepositoryCustom {

    Map<String,String> findAdminModeApiRequestHeaders(Long featureId);

    List<ApiRequestHeaderUpdateResponseDTO> findAdminModeApiRequestHeaderForUpdate(Long featureId);
}
