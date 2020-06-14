package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.persistence.model.Salutation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Qualifier("salutationRepositoryCustom")
public interface SalutationRepositoryCustom {
    List<DropDownResponseDTO> fetchActiveMinSalutation();

    List<Salutation> validateSalutationCount(String ids);
}
