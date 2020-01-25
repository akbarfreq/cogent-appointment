package com.cogent.cogentappointment.utils;

import com.cogent.cogentappointment.dto.request.patient.PatientRequestDTO;
import com.cogent.cogentappointment.dto.response.patient.PatientMinimalResponseDTO;
import com.cogent.cogentappointment.enums.Gender;
import com.cogent.cogentappointment.model.Hospital;
import com.cogent.cogentappointment.model.Patient;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.constants.StatusConstants.NO;
import static com.cogent.cogentappointment.utils.commons.StringUtil.toUpperCase;

/**
 * @author smriti ON 16/01/2020
 */
public class PatientUtils {

    public static Patient parseToPatient(PatientRequestDTO requestDTO,
                                         Gender gender,
                                         Hospital hospital) {
        Patient patient = new Patient();
        patient.setName(toUpperCase(requestDTO.getName()));
        patient.setMobileNumber(requestDTO.getMobileNumber());
        patient.setDateOfBirth(requestDTO.getDateOfBirth());
        patient.setEmail(requestDTO.getEmail());
        patient.setIsSelf(requestDTO.getIsSelf());
        patient.setIsRegistered(NO);
        patient.setESewaId(requestDTO.getESewaId());
        patient.setAddress(requestDTO.getAddress());
        patient.setGender(gender);
        patient.setHospitalId(hospital);
        patient.setStatus(requestDTO.getStatus());
        return patient;
    }

    public static List<PatientMinimalResponseDTO> parseToPatientMinimalResponseDTO(List<Object[]> results) {
        return results.stream()
                .map(parseToPatientMinimalResponseDTO)
                .collect(Collectors.toList());
    }

    public static Function<Object[], PatientMinimalResponseDTO> parseToPatientMinimalResponseDTO = object -> {

        final int PATIENT_ID_INDEX = 0;
        final int NAME_INDEX = 1;
        final int MOBILE_NUMBER_INDEX = 2;
        final int GENDER_INDEX = 3;

        //TODO: calculate age
        return PatientMinimalResponseDTO.builder()
                .patientId(Long.parseLong(object[PATIENT_ID_INDEX].toString()))
                .name(object[NAME_INDEX].toString())
                .mobileNumber(object[MOBILE_NUMBER_INDEX].toString())
                .gender((Gender) (object[GENDER_INDEX]))
                .build();
    };
}
