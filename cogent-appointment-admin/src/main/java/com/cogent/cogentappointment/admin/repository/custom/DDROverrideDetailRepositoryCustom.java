package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.DDRTimeResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability.DDROverrideDetailResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author smriti on 13/05/20
 */
@Repository
@Qualifier("ddrOverrideRepositoryCustom")
public interface DDROverrideDetailRepositoryCustom {

    List<DDROverrideDetailResponseDTO> fetchDDROverrideDetail(Long ddrId);

    List<DDRTimeResponseDTO> fetchDDROverrideTimeDetails(Date date, Long doctorId, Long specializationId);

    List<DDRTimeResponseDTO> fetchDDROverrideTimeDetailsExceptCurrentId(Long ddrOverrideId,
                                                                        Date date, Long doctorId,
                                                                        Long specializationId);
}
