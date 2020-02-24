package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.request.patient.PatientMinSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.patient.PatientSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.patient.PatientUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.patient.PatientDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.patient.PatientMinimalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.patient.PatientResponseDTO;
import com.cogent.cogentappointment.client.dto.response.patient.PatientSearchResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author smriti ON 16/01/2020
 */
@Repository
@Qualifier("patientRepositoryCustom")
public interface PatientRepositoryCustom {

    Long validatePatientDuplicity(String name, String mobileNumber,
                                  Date dateOfBirth, Long hospitalId);

    Long validatePatientDuplicity(PatientUpdateRequestDTO updateRequestDTO, Long hospitalId);

    PatientDetailResponseDTO searchForSelf(PatientMinSearchRequestDTO searchRequestDTO);

    List<PatientMinimalResponseDTO> searchForOthers(PatientMinSearchRequestDTO searchRequestDTO,
                                                    Pageable pageable);

    PatientResponseDTO fetchPatientDetailsById(Long id, Long hospitalId);

    List<PatientSearchResponseDTO> search(PatientSearchRequestDTO searchRequestDTO,
                                          Pageable pageable, Long hospitalId);

    Long countOverallRegisteredPatients(Long HospitalId);

    String fetchLatestRegistrationNumber(Long hospitalId);
}
