package com.cogent.cogentappointment.service.impl;

import com.cogent.cogentappointment.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.dto.request.hospital.HospitalContactNumberUpdateRequestDTO;
import com.cogent.cogentappointment.dto.request.hospital.HospitalRequestDTO;
import com.cogent.cogentappointment.dto.request.hospital.HospitalSearchRequestDTO;
import com.cogent.cogentappointment.dto.request.hospital.HospitalUpdateRequestDTO;
import com.cogent.cogentappointment.dto.response.files.FileUploadResponseDTO;
import com.cogent.cogentappointment.dto.response.hospital.HospitalMinimalResponseDTO;
import com.cogent.cogentappointment.dto.response.hospital.HospitalResponseDTO;
import com.cogent.cogentappointment.exception.DataDuplicationException;
import com.cogent.cogentappointment.exception.NoContentFoundException;
import com.cogent.cogentappointment.model.Hospital;
import com.cogent.cogentappointment.model.HospitalContactNumber;
import com.cogent.cogentappointment.model.HospitalLogo;
import com.cogent.cogentappointment.repository.HospitalContactNumberRepository;
import com.cogent.cogentappointment.repository.HospitalLogoRepository;
import com.cogent.cogentappointment.repository.HospitalRepository;
import com.cogent.cogentappointment.service.FileService;
import com.cogent.cogentappointment.service.HospitalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.Validator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.constants.ErrorMessageConstants.NAME_DUPLICATION_MESSAGE;
import static com.cogent.cogentappointment.constants.StatusConstants.INACTIVE;
import static com.cogent.cogentappointment.constants.StringConstant.FORWARD_SLASH;
import static com.cogent.cogentappointment.exception.utils.ValidationUtils.validateConstraintViolation;
import static com.cogent.cogentappointment.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.log.constants.HospitalLog.HOSPITAL;
import static com.cogent.cogentappointment.utils.HospitalUtils.*;
import static com.cogent.cogentappointment.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author smriti ON 12/01/2020
 */
@Service
@Transactional
@Slf4j
public class HospitalServiceImpl implements HospitalService {

    private final HospitalRepository hospitalRepository;

    private final HospitalContactNumberRepository hospitalContactNumberRepository;

    private final HospitalLogoRepository hospitalLogoRepository;

    private final FileService fileService;

    private final Validator validator;

    public HospitalServiceImpl(HospitalRepository hospitalRepository,
                               HospitalContactNumberRepository hospitalContactNumberRepository,
                               HospitalLogoRepository hospitalLogoRepository,
                               FileService fileService,
                               Validator validator) {
        this.hospitalRepository = hospitalRepository;
        this.hospitalContactNumberRepository = hospitalContactNumberRepository;
        this.hospitalLogoRepository = hospitalLogoRepository;
        this.fileService = fileService;
        this.validator = validator;
    }

    @Override
    public void save(@Valid HospitalRequestDTO requestDTO, MultipartFile multipartFile) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, HOSPITAL);

        validateConstraintViolation(validator.validate(requestDTO));

        validateName(hospitalRepository.fetchHospitalByName(requestDTO.getName()),
                requestDTO.getName());

        Hospital hospital = save(convertDTOToHospital(requestDTO));

        saveHospitalContactNumber(hospital.getId(), requestDTO.getContactNumber());

        saveHospitalLogo(hospital, multipartFile);

        log.info(SAVING_PROCESS_COMPLETED, HOSPITAL, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void update(HospitalUpdateRequestDTO updateRequestDTO, MultipartFile multipartFile) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, HOSPITAL);

        Hospital hospital = findById(updateRequestDTO.getId());

        validateName(hospitalRepository.findHospitalByIdAndName(updateRequestDTO.getId(), updateRequestDTO.getName()),
                updateRequestDTO.getName());

        parseToUpdatedHospital(updateRequestDTO, hospital);

        updateHospitalContactNumber(hospital.getId(), updateRequestDTO.getContactNumberUpdateRequestDTOS());

        updateHospitalLogo(hospital, multipartFile);

        log.info(UPDATING_PROCESS_COMPLETED, HOSPITAL, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<HospitalMinimalResponseDTO> search(HospitalSearchRequestDTO hospitalSearchRequestDTO, Pageable pageable) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, HOSPITAL);

        List<HospitalMinimalResponseDTO> responseDTOS = hospitalRepository.search(hospitalSearchRequestDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, HOSPITAL, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public void delete(DeleteRequestDTO deleteRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, HOSPITAL);

        Hospital hospital = findById(deleteRequestDTO.getId());

        parseToDeletedHospital(hospital, deleteRequestDTO);

        log.info(DELETING_PROCESS_COMPLETED, HOSPITAL, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public Hospital fetchActiveHospital(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, HOSPITAL);

        Hospital hospital = hospitalRepository.findActiveHospitalById(id)
                .orElseThrow(() -> new NoContentFoundException(Hospital.class));

        log.info(FETCHING_PROCESS_COMPLETED, HOSPITAL, getDifferenceBetweenTwoTime(startTime));

        return hospital;
    }

    @Override
    public List<DropDownResponseDTO> fetchHospitalForDropDown() {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, HOSPITAL);

        List<DropDownResponseDTO> responseDTOS = hospitalRepository.fetchActiveHospitalForDropDown();

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, HOSPITAL, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public HospitalResponseDTO fetchDetailsById(Long hospitalId) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, HOSPITAL);

        HospitalResponseDTO responseDTO = hospitalRepository.fetchDetailsById(hospitalId);

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, HOSPITAL, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    private void validateName(Long hospital, String name) {
        if (hospital.intValue() > 0)
            throw new DataDuplicationException(String.format(NAME_DUPLICATION_MESSAGE, Hospital.class.getSimpleName(), name));
    }

    private Hospital save(Hospital hospital) {
        return hospitalRepository.save(hospital);
    }

    private void saveHospitalContactNumber(Long hospitalId, List<String> contactNumbers) {
        List<HospitalContactNumber> hospitalContactNumbers = contactNumbers.stream()
                .map(contactNumber -> parseToHospitalContactNumber(hospitalId, contactNumber))
                .collect(Collectors.toList());

        saveHospitalContactNumber(hospitalContactNumbers);
    }

    private void saveHospitalContactNumber(List<HospitalContactNumber> hospitalContactNumbers) {
        hospitalContactNumberRepository.saveAll(hospitalContactNumbers);
    }

    private void saveHospitalLogo(Hospital hospital, MultipartFile files) {
        if (!Objects.isNull(files)) {
            List<FileUploadResponseDTO> responseList = uploadFiles(hospital, new MultipartFile[]{files});
            saveHospitalLogo(convertFileToHospitalLogo(responseList.get(0), hospital));
        }
    }

    private List<FileUploadResponseDTO> uploadFiles(Hospital hospital, MultipartFile[] files) {
        String subDirectory = hospital.getClass().getSimpleName() + FORWARD_SLASH + hospital.getName();
        return fileService.uploadFiles(files, subDirectory);
    }

    private void saveHospitalLogo(HospitalLogo hospitalLogo) {
        hospitalLogoRepository.save(hospitalLogo);
    }

    private Hospital findById(Long id) {
        return hospitalRepository.findHospitalById(id).orElseThrow(() -> HOSPITAL_WITH_GIVEN_ID_NOT_FOUND.apply(id));
    }

    private void updateHospitalContactNumber(Long hospitalId,
                                             List<HospitalContactNumberUpdateRequestDTO> updateRequestDTOS) {

        List<HospitalContactNumber> hospitalContactNumbers = updateRequestDTOS.stream()
                .map(requestDTO -> parseToUpdatedHospitalContactNumber(hospitalId, requestDTO))
                .collect(Collectors.toList());

        saveHospitalContactNumber(hospitalContactNumbers);
    }

    public void updateHospitalLogo(Hospital hospital, MultipartFile files) {
        HospitalLogo hospitalLogo = hospitalLogoRepository.findHospitalLogoByHospitalId(hospital.getId());

        if (Objects.isNull(hospitalLogo)) saveHospitalLogo(hospital, files);
        else updateHospitalLogo(hospital, hospitalLogo, files);
    }

    private void updateHospitalLogo(Hospital hospital, HospitalLogo hospitalLogo, MultipartFile files) {

        if (!Objects.isNull(files)) {
            List<FileUploadResponseDTO> responseList = uploadFiles(hospital, new MultipartFile[]{files});
            setFileProperties(responseList.get(0), hospitalLogo);
        } else
            hospitalLogo.setStatus(INACTIVE);
    }

    private Function<Long, NoContentFoundException> HOSPITAL_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(Hospital.class, "id", id.toString());
    };

}
