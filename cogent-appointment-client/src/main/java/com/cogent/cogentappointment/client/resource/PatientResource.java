package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.dto.request.patient.PatientMinSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.patient.PatientSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.patient.PatientUpdateRequestDTO;
import com.cogent.cogentappointment.client.service.PatientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.PatientConstant.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.PatientConstant.*;
import static com.cogent.cogentappointment.client.utils.commons.PageableUtils.getPageable;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti on 2019-10-22
 */
@RestController
@RequestMapping(API_V1 + BASE_PATIENT)
@Api(value = BASE_PATIENT_API_VALUE)
public class PatientResource {

    private final PatientService patientService;

    public PatientResource(PatientService patientService) {
        this.patientService = patientService;
    }

    @PutMapping(SEARCH + SELF)
    @ApiOperation(SEARCH_PATIENT_WITH_SELF_TYPE_OPERATION)
    public ResponseEntity<?> searchForSelf(@Valid @RequestBody PatientMinSearchRequestDTO searchRequestDTO) {
        return ok(patientService.searchForSelf(searchRequestDTO));
    }

    @PutMapping(SEARCH + OTHERS)
    @ApiOperation(SEARCH_PATIENT_WITH_OTHERS_TYPE_OPERATION)
    public ResponseEntity<?> search(@Valid @RequestBody PatientMinSearchRequestDTO searchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        return ok(patientService.fetchMinimalPatientInfo(searchRequestDTO, getPageable(page, size)));
    }

    @GetMapping(DETAIL + ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_DETAILS_BY_ID)
    public ResponseEntity<?> fetchDetailsById(@PathVariable("id") Long id) {
        return ok(patientService.fetchDetailsById(id));
    }

    @PutMapping(SEARCH)
    @ApiOperation(SEARCH_OPERATION)
    public ResponseEntity<?> searchPatient(@Valid @RequestBody PatientSearchRequestDTO searchRequestDTO,
                                           @RequestParam("page") int page,
                                           @RequestParam("size") int size) {
        return ok(patientService.search(searchRequestDTO, getPageable(page, size)));
    }

    @PutMapping
    @ApiOperation(UPDATE_PATIENT_INFO_OPERATION)
    public ResponseEntity<?> update(@Valid @RequestBody PatientUpdateRequestDTO updateRequestDTO) {
        patientService.update(updateRequestDTO);
        return ok().build();
    }

    @GetMapping(META_INFO + ACTIVE + MIN)
    @ApiOperation(FETCH_ACTIVE_MIN_PATIENT_META_INFO)
    public ResponseEntity<?> fetchActiveMinPatientMetaInfo() {
        return ok(patientService.fetchActiveMinPatientMetaInfo());
    }

    @GetMapping(META_INFO + MIN)
    @ApiOperation(FETCH_MIN_PATIENT_META_INFO)
    public ResponseEntity<?> fetchPatientMetaInfoForDropdown() {
        return ok(patientService.fetchMinPatientMetaInfo());
    }

}
