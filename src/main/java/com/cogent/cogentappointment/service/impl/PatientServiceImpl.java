package com.cogent.cogentappointment.service.impl;

import com.cogent.cogentappointment.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.dto.request.patient.PatientRequestDTO;
import com.cogent.cogentappointment.dto.request.patient.PatientSearchRequestDTO;
import com.cogent.cogentappointment.dto.request.patient.PatientUpdateRequestDTO;
import com.cogent.cogentappointment.dto.response.patient.PatientMinimalResponseDTO;
import com.cogent.cogentappointment.dto.response.patient.PatientResponseDTO;
import com.cogent.cogentappointment.enums.Gender;
import com.cogent.cogentappointment.enums.Title;
import com.cogent.cogentappointment.exception.DataDuplicationException;
import com.cogent.cogentappointment.exception.NoContentFoundException;
import com.cogent.cogentappointment.model.Hospital;
import com.cogent.cogentappointment.model.Patient;
import com.cogent.cogentappointment.repository.PatientRepository;
import com.cogent.cogentappointment.service.HospitalService;
import com.cogent.cogentappointment.service.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import static com.cogent.cogentappointment.constants.ErrorMessageConstants.PatientServiceMessages.DUPLICATE_PATIENT_MESSAGE;
import static com.cogent.cogentappointment.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.log.constants.PatientLog.PATIENT;
import static com.cogent.cogentappointment.utils.AgeConverterUtils.ageConverter;
import static com.cogent.cogentappointment.utils.GenderUtils.fetchGenderByCode;
import static com.cogent.cogentappointment.utils.PatientUtils.deletePatient;
import static com.cogent.cogentappointment.utils.PatientUtils.parseToPatient;
import static com.cogent.cogentappointment.utils.TitleUtils.fetchTitleByCode;
import static com.cogent.cogentappointment.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author smriti ON 16/01/2020
 */
@Service
@Transactional
@Slf4j
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    private final HospitalService hospitalService;

    public PatientServiceImpl(PatientRepository patientRepository, HospitalService hospitalService) {
        this.patientRepository = patientRepository;
        this.hospitalService = hospitalService;
    }

    @Override
    public Patient save(PatientRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, PATIENT);

        Long patientCount = patientRepository.fetchPatientForValidation(requestDTO.getName(),
                requestDTO.getMobileNumber(), requestDTO.getDateOfBirth());

        validatePatientDuplicity(patientCount, requestDTO.getName(),
                requestDTO.getMobileNumber(), requestDTO.getDateOfBirth());

        Patient patient = savePatient(requestDTO);

        log.info(SAVING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));

        return patient;
    }

    @Override
    public Patient fetchPatient(Long id) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, PATIENT);

        Patient patient = patientRepository.fetchActivePatientById(id)
                .orElseThrow(() -> PATIENT_WITH_GIVEN_ID_NOT_FOUND.apply(id));

        log.info(FETCHING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));

        return patient;
    }

    @Override
    public void deletePatient(DeleteRequestDTO deleteRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, PATIENT);

        deletePatient.apply(fetchPatientById(deleteRequestDTO.getId()), deleteRequestDTO);

        log.info(DELETING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void updatePatient(PatientUpdateRequestDTO updateRequestDTO) {
//        Long startTime = getTimeInMillisecondsFromLocalDate();
//
//        log.info(UPDATING_PROCESS_STARTED, PATIENT);
//
//        Patient patientToUpdate = fetchPatientById(updateRequestDTO.getId());
//
//        validatePatientByCode(updateRequestDTO.getId(), updateRequestDTO.getCode());
//
//        parseToUpdate(patientToUpdate, updateRequestDTO);
//
//        log.info(UPDATING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));
    }


    @Override
    public List<PatientMinimalResponseDTO> searchPatient(PatientSearchRequestDTO searchDTO, Pageable pageable) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, PATIENT);

        List<PatientMinimalResponseDTO> minimalResponseDTOS = patientRepository
                .searchPatient(searchDTO, pageable);

        minimalResponseDTOS.forEach(patientMinimalResponseDTO -> {
            getAge(patientMinimalResponseDTO);
        });

        log.info(SEARCHING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));

        return minimalResponseDTOS;
    }

    @Override
    public PatientResponseDTO fetchPatientDetails(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, PATIENT);

        PatientResponseDTO responseDTO = patientRepository.fetchPatientDetailsById(id);

        responseDTO.setAge(convertDateToAge(responseDTO.getDateOfBirth()));

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public List<DropDownResponseDTO> dropDownList() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, PATIENT);

        List<DropDownResponseDTO> dropDownResponseDTOS = patientRepository.fetchDropDownList()
                .orElseThrow(() -> new NoContentFoundException(Patient.class));

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));

        return dropDownResponseDTOS;
    }

    @Override
    public List<DropDownResponseDTO> activeDropDownList() {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, PATIENT);

        List<DropDownResponseDTO> dropDownResponseDTOS = patientRepository.fetchActiveDropDownList()
                .orElseThrow(() -> new NoContentFoundException(Patient.class));

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));

        return dropDownResponseDTOS;
    }

    private void validatePatientDuplicity(Long patientCount, String name, String mobileNumber,
                                          Date dateOfBirth) {

        if (patientCount.intValue() > 0)
            throw new DataDuplicationException(
                    String.format(DUPLICATE_PATIENT_MESSAGE, name, mobileNumber, dateOfBirth));
    }

    private Gender fetchGender(Character genderCode) {
        return fetchGenderByCode(genderCode);
    }

    private Title fetchTitle(String titleCode) {
        return fetchTitleByCode(titleCode);
    }

    private Hospital fetchHospital(Long hospitalId) {
        return hospitalService.fetchActiveHospital(hospitalId);
    }

    public Patient savePatient(PatientRequestDTO requestDTO) {

        Gender gender = fetchGender(requestDTO.getGender());
        Title title = fetchTitle(requestDTO.getTitle());
        Hospital hospital = fetchHospital(requestDTO.getHospitalId());

        return save(parseToPatient(requestDTO, gender, hospital, title));
    }

    private Patient save(Patient patient) {
        return patientRepository.save(patient);
    }

    private Function<Long, NoContentFoundException> PATIENT_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(Patient.class, "id", id.toString());
    };

    public Patient fetchPatientById(Long id) {
        return patientRepository.fetchPatientById(id).orElseThrow(() ->
                new NoContentFoundException(Patient.class));
    }

    public PatientMinimalResponseDTO getAge(PatientMinimalResponseDTO minimalResponseDTO) {
        String age = convertDateToAge(minimalResponseDTO.getDateOfBirth());
        minimalResponseDTO.setAge(age);
        return minimalResponseDTO;
    }

    public String convertDateToAge(LocalDate birthdayDate) {
        return ageConverter(birthdayDate);
    }
}

