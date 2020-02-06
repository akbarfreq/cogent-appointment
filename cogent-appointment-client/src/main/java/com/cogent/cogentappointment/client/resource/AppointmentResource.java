package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentCancelRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentCheckAvailabilityRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentPendingSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentRequestDTO;
import com.cogent.cogentappointment.client.service.AppointmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.AppointmentConstant.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.AppointmentConstants.*;
import static java.net.URI.create;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti on 2019-10-22
 */
@RequestMapping(API_V1 + BASE_APPOINTMENT)
@RestController
@Api(BASE_API_VALUE)
public class AppointmentResource {

    private final AppointmentService appointmentService;

    public AppointmentResource(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

//    @PutMapping(CHECK_AVAILABILITY)
//    @ApiOperation(CHECK_APPOINTMENT_AVAILABILITY)
//    public ResponseEntity<?> checkAvailability(@Valid @RequestBody AppointmentCheckAvailabilityRequestDTO requestDTO) {
//        return ok(appointmentService.checkAvailability(requestDTO));
//    }

    @PutMapping(CHECK_AVAILABILITY)
    @ApiOperation(CHECK_APPOINTMENT_AVAILABILITY)
    public ResponseEntity<?> checkAvailability(@Valid @RequestBody AppointmentCheckAvailabilityRequestDTO requestDTO) {
        return ok(appointmentService.checkAvailability(requestDTO));
    }

    @PostMapping
    @ApiOperation(SAVE_OPERATION)
    public ResponseEntity<?> save(@Valid @RequestBody AppointmentRequestDTO requestDTO) {
        return created(create(API_V1 + BASE_APPOINTMENT)).body(appointmentService.save(requestDTO));
    }

    @PutMapping(PENDING_APPOINTMENT)
    @ApiOperation((FETCH_PENDING_APPOINTMENT))
    public ResponseEntity<?> fetchPendingAppointments(@RequestBody AppointmentPendingSearchDTO searchDTO) {
        return ok(appointmentService.fetchPendingAppointments(searchDTO));
    }

    @PutMapping(CANCEL_APPOINTMENT)
    @ApiOperation(CANCEL_APPOINTMENT_OPERATION)
    public ResponseEntity<?> cancelAppointment(@Valid @RequestBody AppointmentCancelRequestDTO cancelRequestDTO) {
        appointmentService.cancelAppointment(cancelRequestDTO);
        return ok().build();
    }
//

//    @PutMapping
//    @ApiOperation(UPDATE_OPERATION)
//    public ResponseEntity<?> update(@Valid @RequestBody AppointmentUpdateRequestDTO requestDTO) {
//        appointmentService.update(requestDTO);
//        return ok().build();
//    }
//

//    @PutMapping(SEARCH)
//    @ApiOperation(SEARCH_OPERATION)
//    public ResponseEntity<?> search(@RequestBody AppointmentSearchRequestDTO searchRequestDTO,
//                                    @RequestParam("page") int page,
//                                    @RequestParam("size") int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        return ok().body(appointmentService.search(searchRequestDTO, pageable));
//    }
//
//    @GetMapping(DETAILS + ID_PATH_VARIABLE_BASE)
//    @ApiOperation(DETAILS_OPERATION)
//    public ResponseEntity<?> fetchDetailsById(@PathVariable("id") Long id) {
//        return ok(appointmentService.fetchDetailsById(id));
//    }
//
//    @PutMapping(RESCHEDULE)
//    @ApiOperation(RESCHEDULE_OPERATION)
//    public ResponseEntity<?> rescheduleAppointment(@Valid @RequestBody AppointmentRescheduleRequestDTO requestDTO) {
//        appointmentService.rescheduleAppointment(requestDTO);
//        return ok().build();
//    }
}
