package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Qualifier("integrationRequestBodyParametersRepositoryCustom")
public interface IntegrationRequestBodyParametersRepositoryCustom {

    List<DropDownResponseDTO> fetchActiveRequestBodyParameters();
}
