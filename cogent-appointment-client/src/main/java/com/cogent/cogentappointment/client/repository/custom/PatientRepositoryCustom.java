package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.request.patient.PatientSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.patient.PatientUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.patient.PatientDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.patient.PatientMinimalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.patient.PatientResponseDTO;
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

    Long fetchPatientForValidation(String name, String mobileNumber,
                                   Date dateOfBirth, Long hospitalId);

    Long fetchPatientForValidationToUpdate(PatientUpdateRequestDTO patientUpdateRequestDTO);

    PatientDetailResponseDTO searchForSelf(PatientSearchRequestDTO searchRequestDTO);



    List<PatientMinimalResponseDTO> fetchMinimalPatientInfo(PatientSearchRequestDTO searchRequestDTO,
                                                            Pageable pageable);

    PatientDetailResponseDTO fetchDetailsById(Long id);

    List<PatientResponseDTO> search(PatientSearchRequestDTO searchRequestDTO,Pageable pageable);

//    List<PatientMinimalResponseDTO> searchPatient(
//            PatientSearchRequestDTO searchRequestDTO, Pageable pageable);
//
//    PatientDetailResponseDTO fetchPatientDetailsById(Long id);
//
//    Optional<List<DropDownResponseDTO>> fetchActiveDropDownList();
//
//    Optional<List<DropDownResponseDTO>> fetchDropDownList();
//
//    List<Object[]> getPatient();
//
//    String fetchLatestPatientHisNumber();
}
