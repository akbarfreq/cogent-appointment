package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentPendingApproval.AppointmentRejectDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.refund.AppointmentRefundRejectDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentLog.AppointmentLogDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentLog.AppointmentLogResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentPendingApproval.AppointmentPendingApprovalDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentPendingApproval.AppointmentPendingApprovalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.AppointmentStatusResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.dashboard.AppointmentCountResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.reschedule.AppointmentRescheduleLogDTO;
import com.cogent.cogentappointment.admin.dto.response.reschedule.AppointmentRescheduleLogResponseDTO;
import com.cogent.cogentappointment.persistence.enums.Gender;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.AppointmentRefundDetail;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.AppointmentStatusConstants.REJECTED;
import static com.cogent.cogentappointment.admin.utils.commons.AgeConverterUtils.calculateAge;

/**
 * @author smriti on 2019-10-24
 */
public class AppointmentUtils {

    public static void parseRefundRejectDetails(AppointmentRefundRejectDTO refundRejectDTO,
                                                AppointmentRefundDetail refundDetail) {
        refundDetail.setStatus(REJECTED);
        refundDetail.setRemarks(refundRejectDTO.getRemarks());
    }

    public static AppointmentCountResponseDTO parseToAppointmentCountResponseDTO(Long overAllAppointment, Long newPatient,
                                                                                 Long registeredPatient,
                                                                                 Character pillType) {
        AppointmentCountResponseDTO countResponseDTO = new AppointmentCountResponseDTO();
        countResponseDTO.setTotalAppointment(overAllAppointment);
        countResponseDTO.setNewPatient(newPatient);
        countResponseDTO.setRegisteredPatient(registeredPatient);
        countResponseDTO.setPillType(pillType);

        return countResponseDTO;
    }

    public static void parseAppointmentRejectDetails(AppointmentRejectDTO rejectDTO,
                                                     Appointment appointment) {
        appointment.setStatus(REJECTED);
        appointment.setRemarks(rejectDTO.getRemarks());
    }

    public static List<AppointmentStatusResponseDTO> parseQueryResultToAppointmentStatusResponseDTOS
            (List<Object[]> results) {

        List<AppointmentStatusResponseDTO> appointmentStatusResponseDTOS = new ArrayList<>();

        results.forEach(result -> {
            final int APPOINTMENT_DATE_INDEX = 0;
            final int TIME_WITH_STATUS_DETAILS_INDEX = 1;
            final int DOCTOR_ID_INDEX = 2;
            final int SPECIALIZATION_ID_INDEX = 3;
            final int APPOINTMENT_NUMBER_INDEX = 4;
            final int PATIENT_NAME_INDEX = 5;
            final int GENDER_INDEX = 6;
            final int MOBILE_NUMBER_INDEX = 7;
            final int AGE_INDEX = 8;

            Date appointmentDate = (Date) result[APPOINTMENT_DATE_INDEX];

            LocalDate appointmentLocalDate = new java.sql.Date(appointmentDate.getTime()).toLocalDate();

            AppointmentStatusResponseDTO appointmentStatusResponseDTO = AppointmentStatusResponseDTO.builder()
                    .date(appointmentLocalDate)
                    .appointmentTimeDetails(result[TIME_WITH_STATUS_DETAILS_INDEX].toString())
                    .doctorId(Long.parseLong(result[DOCTOR_ID_INDEX].toString()))
                    .specializationId(Long.parseLong(result[SPECIALIZATION_ID_INDEX].toString()))
                    .appointmentNumber(result[APPOINTMENT_NUMBER_INDEX].toString())
                    .patientName(result[PATIENT_NAME_INDEX].toString())
                    .mobileNumber(result[MOBILE_NUMBER_INDEX].toString())
                    .age(result[AGE_INDEX].toString())
                    .gender(result[GENDER_INDEX].toString())
                    .build();

            appointmentStatusResponseDTOS.add(appointmentStatusResponseDTO);
        });

        return appointmentStatusResponseDTOS;
    }

    public static AppointmentPendingApprovalResponseDTO parseQueryResultToAppointmentApprovalResponse
            (List<Object[]> results) {

        AppointmentPendingApprovalResponseDTO appointmentPendingApprovalResponseDTO = new AppointmentPendingApprovalResponseDTO();

        List<AppointmentPendingApprovalDTO> appointmentPendingApprovalDTOS = new ArrayList<>();

        AtomicReference<Double> totalAmount = new AtomicReference<>(0D);

        results.forEach(result -> {
            final int HOSPITAL_NAME_INDEX = 0;
            final int APPOINTMENT_DATE_INDEX = 1;
            final int APPOINTMENT_NUMBER_INDEX = 2;
            final int APPOINTMENT_TIME_INDEX = 3;
            final int ESEWA_ID_INDEX = 4;
            final int REGISTRATION_NUMBER_INDEX = 5;
            final int PATIENT_NAME_INDEX = 6;
            final int PATIENT_GENDER_INDEX = 7;
            final int PATIENT_DOB_INDEX = 8;
            final int IS_REGISTERED_INDEX = 9;
            final int IS_SELF_INDEX = 10;
            final int PATIENT_MOBILE_NUMBER_INDEX = 11;
            final int SPECIALIZATION_NAME_INDEX = 12;
            final int TRANSACTION_NUMBER_INDEX = 13;
            final int APPOINTMENT_AMOUNT_INDEX = 14;
            final int DOCTOR_NAME_INDEX = 15;
            final int REFUND_AMOUNT_INDEX = 16;
            final int APPOINTMENT_ID_INDEX = 17;

            Date appointmentDate = (Date) result[APPOINTMENT_DATE_INDEX];
            Date patientDob = (Date) result[PATIENT_DOB_INDEX];

            Double appointmentAmount = Objects.isNull(result[APPOINTMENT_AMOUNT_INDEX]) ?
                    0D : Double.parseDouble(result[APPOINTMENT_AMOUNT_INDEX].toString());

            Double refundAmount = Objects.isNull(result[REFUND_AMOUNT_INDEX]) ?
                    0D : Double.parseDouble(result[REFUND_AMOUNT_INDEX].toString());

            String registrationNumber = Objects.isNull(result[REGISTRATION_NUMBER_INDEX]) ?
                    null : result[REGISTRATION_NUMBER_INDEX].toString();

            AppointmentPendingApprovalDTO appointmentStatusResponseDTO =
                    AppointmentPendingApprovalDTO.builder()
                            .hospitalName(result[HOSPITAL_NAME_INDEX].toString())
                            .appointmentDate(appointmentDate)
                            .appointmentNumber(result[APPOINTMENT_NUMBER_INDEX].toString())
                            .appointmentTime(result[APPOINTMENT_TIME_INDEX].toString())
                            .esewaId(result[ESEWA_ID_INDEX].toString())
                            .registrationNumber(registrationNumber)
                            .patientName(result[PATIENT_NAME_INDEX].toString())
                            .patientGender((Gender) result[PATIENT_GENDER_INDEX])
                            .patientDob(patientDob)
                            .patientAge(calculateAge(patientDob))
                            .isRegistered((Character) result[IS_REGISTERED_INDEX])
                            .isSelf((Character) result[IS_SELF_INDEX])
                            .mobileNumber(result[PATIENT_MOBILE_NUMBER_INDEX].toString())
                            .specializationName(result[SPECIALIZATION_NAME_INDEX].toString())
                            .transactionNumber(result[TRANSACTION_NUMBER_INDEX].toString())
                            .appointmentAmount(appointmentAmount)
                            .specializationName(result[SPECIALIZATION_NAME_INDEX].toString())
                            .doctorName(result[DOCTOR_NAME_INDEX].toString())
                            .refundAmount(refundAmount)
                            .appointmentId(Long.parseLong(result[APPOINTMENT_ID_INDEX].toString()))
                            .build();

            appointmentPendingApprovalDTOS.add(appointmentStatusResponseDTO);

            totalAmount.updateAndGet(v -> v + appointmentAmount);
        });

        appointmentPendingApprovalResponseDTO.setPendingAppointmentApprovals(appointmentPendingApprovalDTOS);
        appointmentPendingApprovalResponseDTO.setTotalAmount(totalAmount.get());

        return appointmentPendingApprovalResponseDTO;
    }

    public static AppointmentRescheduleLogResponseDTO parseQueryResultToAppointmentRescheduleLogResponse(List<Object[]> results) {

        AppointmentRescheduleLogResponseDTO rescheduleLogResponseDTO = new AppointmentRescheduleLogResponseDTO();

        List<AppointmentRescheduleLogDTO> appointmentLogSearchDTOS = new ArrayList<>();

        AtomicReference<Double> totalAmount = new AtomicReference<>(0D);

        results.forEach(result -> {
            final int HOSPITAL_NAME_INDEX = 0;
            final int ESEWA_ID_INDEX = 1;
            final int PREVIOUS_APPOINTMENT_DATE_INDEX = 2;
            final int APPOINTMENT_RESCHEDULED_DATE_INDEX = 3;
            final int APPOINTMENT_NUMBER_INDEX = 4;
            final int REGISTRATION_NUMBER_INDEX = 5;
            final int PATIENT_NAME_INDEX = 6;
            final int PATIENT_DOB_INDEX = 7;
            final int PATIENT_GENDER_INDEX = 8;
            final int PATIENT_MOBILE_NUMBER_INDEX = 9;
            final int SPECIALIZATION_NAME_INDEX = 10;
            final int DOCTOR_NAME_INDEX = 11;
            final int TRANSACTION_NUMBER_INDEX = 12;
            final int APPOINTMENT_AMOUNT_INDEX = 13;
            final int REMARKS_INDEX = 14;


            Date previosAppointmentDate = (Date) result[PREVIOUS_APPOINTMENT_DATE_INDEX];
            Date rescheduledAppointmentDate = (Date) result[APPOINTMENT_RESCHEDULED_DATE_INDEX];
            Date patientDob = (Date) result[PATIENT_DOB_INDEX];

            Double appointmentAmount = Objects.isNull(result[APPOINTMENT_AMOUNT_INDEX]) ?
                    0D : Double.parseDouble(result[APPOINTMENT_AMOUNT_INDEX].toString());

            String registrationNumber = Objects.isNull(result[REGISTRATION_NUMBER_INDEX]) ?
                    "" : result[REGISTRATION_NUMBER_INDEX].toString();

            String remarks = Objects.isNull(result[REMARKS_INDEX]) ?
                    null : result[REMARKS_INDEX].toString();

            AppointmentRescheduleLogDTO appointmentRescheduleLogDTO =
                    AppointmentRescheduleLogDTO.builder()
                            .hospitalName(result[HOSPITAL_NAME_INDEX].toString())
                            .previousAppointmentDate(previosAppointmentDate)
                            .rescheduleAppointmentDate(rescheduledAppointmentDate)
                            .appointmentNumber(result[APPOINTMENT_NUMBER_INDEX].toString())
                            .esewaId(result[ESEWA_ID_INDEX].toString())
                            .registrationNumber(registrationNumber)
                            .patientName(result[PATIENT_NAME_INDEX].toString())
                            .patientGender((Gender) result[PATIENT_GENDER_INDEX])
                            .patientAge(calculateAge(patientDob))
                            .mobileNumber(result[PATIENT_MOBILE_NUMBER_INDEX].toString())
                            .specializationName(result[SPECIALIZATION_NAME_INDEX].toString())
                            .transactionNumber(Objects.isNull(result[TRANSACTION_NUMBER_INDEX])
                                    ? null : result[TRANSACTION_NUMBER_INDEX].toString())
                            .appointmentAmount(appointmentAmount)
                            .doctorName(result[DOCTOR_NAME_INDEX].toString())
                            .remarks(remarks)
                            .build();

            appointmentLogSearchDTOS.add(appointmentRescheduleLogDTO);

            totalAmount.updateAndGet(v -> v + appointmentAmount);
        });

        rescheduleLogResponseDTO.setAppointmentRescheduleLogDTOS(appointmentLogSearchDTOS);
        rescheduleLogResponseDTO.setTotalAmount(totalAmount.get());

        return rescheduleLogResponseDTO;

    }

    public static AppointmentLogResponseDTO parseQueryResultToAppointmentLogResponse(List<Object[]> results) {

        AppointmentLogResponseDTO appointmentLogResponseDTO = new AppointmentLogResponseDTO();

        List<AppointmentLogDTO> appointmentLogSearchDTOS = new ArrayList<>();

        AtomicReference<Double> totalAmount = new AtomicReference<>(0D);

        results.forEach(result -> {
            final int HOSPITAL_NAME_INDEX = 0;
            final int APPOINTMENT_DATE_INDEX = 1;
            final int APPOINTMENT_NUMBER_INDEX = 2;
            final int APPOINTMENT_TIME_INDEX = 3;
            final int ESEWA_ID_INDEX = 4;
            final int REGISTRATION_NUMBER_INDEX = 5;
            final int PATIENT_NAME_INDEX = 6;
            final int PATIENT_GENDER_INDEX = 7;
            final int PATIENT_DOB_INDEX = 8;
            final int IS_REGISTERED_INDEX = 9;
            final int IS_SELF_INDEX = 10;
            final int PATIENT_MOBILE_NUMBER_INDEX = 11;
            final int SPECIALIZATION_NAME_INDEX = 12;
            final int TRANSACTION_NUMBER_INDEX = 13;
            final int APPOINTMENT_AMOUNT_INDEX = 14;
            final int DOCTOR_NAME_INDEX = 15;
            final int APPOINTMENT_STATUS_INDEX = 16;
            final int REFUND_AMOUNT_INDEX = 17;

            Date appointmentDate = (Date) result[APPOINTMENT_DATE_INDEX];
            Date patientDob = (Date) result[PATIENT_DOB_INDEX];

            Double appointmentAmount = Objects.isNull(result[APPOINTMENT_AMOUNT_INDEX]) ?
                    0D : Double.parseDouble(result[APPOINTMENT_AMOUNT_INDEX].toString());

            Double refundAmount = Objects.isNull(result[REFUND_AMOUNT_INDEX]) ?
                    0D : Double.parseDouble(result[REFUND_AMOUNT_INDEX].toString());

            String registrationNumber = Objects.isNull(result[REGISTRATION_NUMBER_INDEX]) ?
                    null : result[REGISTRATION_NUMBER_INDEX].toString();


            AppointmentLogDTO appointmentLogDTO =
                    AppointmentLogDTO.builder()
                            .hospitalName(result[HOSPITAL_NAME_INDEX].toString())
                            .appointmentDate(appointmentDate)
                            .appointmentNumber(result[APPOINTMENT_NUMBER_INDEX].toString())
                            .appointmentTime(result[APPOINTMENT_TIME_INDEX].toString())
                            .esewaId(result[ESEWA_ID_INDEX].toString())
                            .registrationNumber(registrationNumber)
                            .patientName(result[PATIENT_NAME_INDEX].toString())
                            .patientGender((Gender) result[PATIENT_GENDER_INDEX])
                            .patientDob(patientDob)
                            .patientAge(calculateAge(patientDob))
                            .isRegistered((Character) result[IS_REGISTERED_INDEX])
                            .isSelf((Character) result[IS_SELF_INDEX])
                            .mobileNumber(result[PATIENT_MOBILE_NUMBER_INDEX].toString())
                            .specializationName(result[SPECIALIZATION_NAME_INDEX].toString())
                            .transactionNumber(Objects.isNull(result[TRANSACTION_NUMBER_INDEX])
                                    ? null : result[TRANSACTION_NUMBER_INDEX].toString())
                            .appointmentAmount(appointmentAmount)
                            .doctorName(result[DOCTOR_NAME_INDEX].toString())
                            .status(result[APPOINTMENT_STATUS_INDEX].toString())
                            .refundAmount(refundAmount)
                            .build();

            appointmentLogSearchDTOS.add(appointmentLogDTO);

            totalAmount.updateAndGet(v -> v + appointmentAmount);
        });

        appointmentLogResponseDTO.setAppointmentLogs(appointmentLogSearchDTOS);
        appointmentLogResponseDTO.setTotalAmount(totalAmount.get());

        return appointmentLogResponseDTO;

    }

}
