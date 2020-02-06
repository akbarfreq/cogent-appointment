package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.department.DepartmentRequestDTO;
import com.cogent.cogentappointment.client.dto.request.department.DepartmentSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.department.DepartmentUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.department.DepartmentMinimalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.department.DepartmentResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.DepartmentRepository;
import com.cogent.cogentappointment.client.service.DepartmentService;
import com.cogent.cogentappointment.client.service.HospitalService;
import com.cogent.cogentappointment.persistence.model.Department;
import com.cogent.cogentappointment.persistence.model.Hospital;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.client.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.client.log.constants.DepartmentLog.DEPARTMENT;
import static com.cogent.cogentappointment.client.utils.DepartmentUtils.*;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;
import static com.cogent.cogentappointment.client.utils.commons.NameAndCodeValidationUtils.validateDuplicity;

/**
 * @author smriti ON 25/01/2020
 */
@Slf4j
@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    private final HospitalService hospitalService;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository,
                                 HospitalService hospitalService) {
        this.departmentRepository = departmentRepository;
        this.hospitalService = hospitalService;
    }

    @Override
    public void save(DepartmentRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, DEPARTMENT);

        List<Object[]> departments = departmentRepository.validateDuplicity(requestDTO);

        validateDuplicity(departments, requestDTO.getName(), requestDTO.getDepartmentCode(),
                Department.class.getSimpleName());

        Hospital hospital = fetchHospital(requestDTO.getHospitalId());

        save(parseToDepartment(requestDTO, hospital));

        log.info(SAVING_PROCESS_COMPLETED, DEPARTMENT, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void update(DepartmentUpdateRequestDTO updateRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, DEPARTMENT);

        Department department = fetchDepartmentById(updateRequestDTO.getId());

        List<Object[]> departments = departmentRepository.validateDuplicity(updateRequestDTO);

        validateDuplicity(departments, updateRequestDTO.getName(), updateRequestDTO.getDepartmentCode(),
                Department.class.getSimpleName());

        parseToUpdatedDepartment(updateRequestDTO, department);

        log.info(UPDATING_PROCESS_COMPLETED, DEPARTMENT, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void delete(DeleteRequestDTO deleteRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, DEPARTMENT);

        Department department = fetchDepartmentById(deleteRequestDTO.getId());

        parseDepartmentStatus(deleteRequestDTO.getStatus(), deleteRequestDTO.getRemarks(), department);

        log.info(DELETING_PROCESS_COMPLETED, DEPARTMENT, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<DepartmentMinimalResponseDTO> search(DepartmentSearchRequestDTO searchRequestDTO,
                                                     Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, DEPARTMENT);

        List<DepartmentMinimalResponseDTO> responseDTO = departmentRepository.search
                (searchRequestDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, DEPARTMENT, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public DepartmentResponseDTO fetchDetails(Long id) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, DEPARTMENT);

        DepartmentResponseDTO departmentResponseDTO = departmentRepository
                .fetchDetails(id);

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, DEPARTMENT, getDifferenceBetweenTwoTime(startTime));

        return departmentResponseDTO;
    }

    @Override
    public List<DropDownResponseDTO> fetchDepartmentForDropdown() {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, DEPARTMENT);

        List<DropDownResponseDTO> departmentDropDownDTOS = departmentRepository.fetchDepartmentForDropdown()
                .orElseThrow(() -> DEPARTMENT_NOT_FOUND.get());

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, DEPARTMENT, getDifferenceBetweenTwoTime(startTime));

        return departmentDropDownDTOS;
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveDropDownList() {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, DEPARTMENT);

        List<DropDownResponseDTO> departmentDropDownDTOS = departmentRepository.fetchActiveDropDownList()
                .orElseThrow(() -> DEPARTMENT_NOT_FOUND.get());

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, DEPARTMENT, getDifferenceBetweenTwoTime(startTime));

        return departmentDropDownDTOS;
    }

    @Override
    public List<DropDownResponseDTO> fetchDepartmentByHospitalId(Long hospitalId) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, DEPARTMENT);

        List<DropDownResponseDTO> responseDTOS = departmentRepository.fetchDepartmentByHospitalId(hospitalId)
                .orElseThrow(() -> DEPARTMENT_NOT_FOUND.get());

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, DEPARTMENT, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    private Hospital fetchHospital(Long hospitalId) {
        return hospitalService.fetchActiveHospital(hospitalId);
    }

    public Department fetchDepartmentById(Long id) {
        return departmentRepository.findDepartmentById(id).orElseThrow(() ->
                new NoContentFoundException(Department.class, "id", id.toString()));
    }

    public void save(Department department) {
        departmentRepository.save(department);
    }

    private Supplier<NoContentFoundException> DEPARTMENT_NOT_FOUND = () ->
            new NoContentFoundException(Department.class);
}
