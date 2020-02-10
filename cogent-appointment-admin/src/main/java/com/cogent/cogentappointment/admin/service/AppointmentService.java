package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.request.appointment.AppointmentLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.AppointmentPendingApprovalSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.refund.AppointmentRefundRejectDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.refund.AppointmentRefundSearchDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.AppointmentLogSearchResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.AppointmentPendingApprovalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.refund.AppointmentRefundResponseDTO;
import org.springframework.data.domain.Pageable;

/**
 * @author smriti on 2019-10-22
 */
public interface AppointmentService {

    AppointmentRefundResponseDTO fetchRefundAppointments(AppointmentRefundSearchDTO searchDTO,
                                                         Pageable pageable);

    void approveRefundAppointment(Long appointmentId);

    void rejectRefundAppointment(AppointmentRefundRejectDTO refundRejectDTO);


    AppointmentPendingApprovalResponseDTO fetchPendingApprovals(AppointmentPendingApprovalSearchDTO searchRequestDTO, Pageable pageable);

    AppointmentLogSearchResponseDTO fetchAppointmentLogs(AppointmentLogSearchDTO searchRequestDTO, Pageable pageable);

//    List<AppointmentStatusResponseDTO> fetchAppointmentForAppointmentStatus(AppointmentStatusRequestDTO requestDTO);
//

}
