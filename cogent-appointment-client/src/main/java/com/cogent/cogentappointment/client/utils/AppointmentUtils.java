package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.request.appointment.approval.AppointmentRejectDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.esewa.save.AppointmentRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.refund.AppointmentRefundRejectDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.reschedule.AppointmentRescheduleRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentBookedTimeResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appointmentQueue.AppointmentQueueDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appointmentQueue.AppointmentQueueSearchByTimeDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appointmentQueue.AppointmentTimeDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.esewa.*;
import com.cogent.cogentappointment.client.dto.response.appointment.log.AppointmentLogDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.log.AppointmentLogResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentStatus.AppointmentStatusResponseDTO;
import com.cogent.cogentappointment.client.dto.response.dashboard.AppointmentCountResponseDTO;
import com.cogent.cogentappointment.client.dto.response.doctorDutyRoster.DoctorDutyRosterTimeResponseDTO;
import com.cogent.cogentappointment.client.exception.BadRequestException;
import com.cogent.cogentappointment.persistence.enums.Gender;
import com.cogent.cogentappointment.persistence.model.*;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.AppointmentServiceMessage.INVALID_APPOINTMENT_DATE;
import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.AppointmentServiceMessage.INVALID_APPOINTMENT_DATE_TIME;
import static com.cogent.cogentappointment.client.constants.StatusConstants.AppointmentStatusConstants.*;
import static com.cogent.cogentappointment.client.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.client.constants.StringConstant.HYPHEN;
import static com.cogent.cogentappointment.client.utils.commons.DateConverterUtils.calculateAge;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.*;
import static com.cogent.cogentappointment.client.utils.commons.NumberFormatterUtils.generateRandomNumber;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

/**
 * @author smriti on 2019-10-24
 */
@Slf4j
public class AppointmentUtils {

    private static final DateTimeFormatter FORMAT = DateTimeFormat.forPattern("HH:mm");

    /*VALIDATE IF REQUESTED DATE AND TIME IS BEFORE CURRENT DATE AND TIME*/
    public static void validateIfRequestIsBeforeCurrentDateTime(Date appointmentDate,
                                                                String appointmentTime) {

        Date requestDateTime = parseAppointmentTime(appointmentDate, appointmentTime);

        Date currentDateTime = new Date();

        boolean isRequestedBeforeCurrentDateTime = requestDateTime.before(currentDateTime);

        if (isRequestedBeforeCurrentDateTime) {
            log.error(INVALID_APPOINTMENT_DATE_TIME);
            throw new BadRequestException(INVALID_APPOINTMENT_DATE_TIME);
        }
    }

    public static void validateIfRequestIsPastDate(Date requestedDate) {

        boolean isDateBefore = removeTime(requestedDate).before(removeTime(new Date()));
        if (isDateBefore)
            throw new BadRequestException(INVALID_APPOINTMENT_DATE);
    }

    private static boolean hasTimePassed(Date date, String time) {

        Date availableDateTime = parseAppointmentTime(date, time);

        Date currentDate = new Date();

        return availableDateTime.before(currentDate);
    }

    /*VALIDATE IF REQUESTED APPOINTMENT TIME LIES BETWEEN DOCTOR DUTY ROSTER TIME SCHEDULES
  * IF IT MATCHES, THEN DO NOTHING
  * ELSE REQUESTED TIME IS INVALID AND THUS CANNOT TAKE AN APPOINTMENT*/
    public static boolean validateIfRequestedAppointmentTimeIsValid(DoctorDutyRosterTimeResponseDTO doctorDutyRosterInfo,
                                                             String appointmentTime) {

        final DateTimeFormatter FORMAT = DateTimeFormat.forPattern("HH:mm");

        String doctorStartTime = getTimeFromDate(doctorDutyRosterInfo.getStartTime());
        String doctorEndTime = getTimeFromDate(doctorDutyRosterInfo.getEndTime());

        DateTime startDateTime = new DateTime(FORMAT.parseDateTime(doctorStartTime));

        do {
            String date = FORMAT.print(startDateTime);

            final Duration rosterGapDuration = Minutes.minutes(doctorDutyRosterInfo.getRosterGapDuration())
                    .toStandardDuration();

            if (date.equals(appointmentTime))
                return true;

            startDateTime = startDateTime.plus(rosterGapDuration);
        } while (startDateTime.compareTo(FORMAT.parseDateTime(doctorEndTime)) <= 0);

        return false;
    }

    public static Appointment parseToAppointment(AppointmentRequestDTO requestDTO,
                                                 AppointmentReservationLog appointmentReservationLog,
                                                 String appointmentNumber,
                                                 Character isSelf,
                                                 Patient patient,
                                                 Specialization specialization,
                                                 Doctor doctor,
                                                 Hospital hospital,
                                                 AppointmentMode appointmentMode) {

        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(appointmentReservationLog.getAppointmentDate());
        appointment.setAppointmentTime(appointmentReservationLog.getAppointmentTime());
        appointment.setAppointmentNumber(appointmentNumber);
        appointment.setCreatedDateNepali(requestDTO.getCreatedDateNepali());
        appointment.setIsFollowUp(requestDTO.getIsFollowUp());
        appointment.setIsSelf(isSelf);
        appointment.setAppointmentModeId(appointmentMode);
        parseToAppointment(patient, specialization, doctor, hospital, appointment);
        return appointment;
    }

    private static Date parseAppointmentTime(Date appointmentDate, String appointmentTime) {
        return datePlusTime(utilDateToSqlDate(appointmentDate), Objects.requireNonNull(parseTime(appointmentTime)));
    }

    private static void parseToAppointment(Patient patient,
                                           Specialization specialization,
                                           Doctor doctor,
                                           Hospital hospital,
                                           Appointment appointment) {
        appointment.setDoctorId(doctor);
        appointment.setSpecializationId(specialization);
        appointment.setHospitalId(hospital);
        appointment.setPatientId(patient);
        appointment.setStatus(PENDING_APPROVAL);
        appointment.setSerialNumber(generateRandomNumber(6));
    }

    public static AppointmentSuccessResponseDTO parseToAppointmentSuccessResponseDTO(String appointmentNumber,
                                                                                     Character transactionStatus) {
        return AppointmentSuccessResponseDTO.builder()
                .appointmentNumber(appointmentNumber)
                .appointmentTransactionStatus(transactionStatus)
                .responseStatus(CREATED)
                .responseCode(CREATED.value())
                .build();
    }

    public static void parseAppointmentCancelledDetails(Appointment appointment,
                                                        String remarks) {

        appointment.setRemarks(remarks);
        appointment.setStatus(CANCELLED);
    }

    public static AppointmentRefundDetail parseToAppointmentRefundDetail(Appointment appointment,
                                                                         Double refundAmount) {

        AppointmentRefundDetail refundDetail = new AppointmentRefundDetail();
        refundDetail.setAppointmentId(appointment);
        refundDetail.setRefundAmount(refundAmount);
        refundDetail.setStatus(PENDING_APPROVAL);
        refundDetail.setCancelledDate(new Date());
        return refundDetail;
    }

    /*startingFiscalYear = 2076-04-01
    * startingYear = 2076
    * splitStartingYear = 76
    *
    * endingFiscalYear = 2077-03-01
    * endingYear = 2077
    * splitEndingYear = 77
    *
    * APPOINTMENT NUMBER IS GENERATED IN FORMAT : 76-77-0001
    * (fiscal year start- fiscal year end – unique appointment no)
    * appointment number starts with ‘0001’ and increments by 1 & starts with ‘0001’ again in next
    * fiscal year.
    *
    * results[0] = start fiscal year
    * results[1] = end fiscal year
    * results[2] = appointment number*/
    public static String generateAppointmentNumber(List results,
                                                   String startingFiscalYear,
                                                   String endingFiscalYear) {

        String startingYear = startingFiscalYear.split(HYPHEN)[0];
        String splitStartingYear = startingYear.substring(startingYear.length() - 2);

        String endingYear = endingFiscalYear.split(HYPHEN)[0];
        String splitEndingYear = endingYear.substring(endingYear.length() - 2);

        String appointmentNumber;

        if (results.isEmpty())
            appointmentNumber = "0001";
        else
            appointmentNumber = results.get(0).toString().contains(HYPHEN) ?
                    String.format("%04d", Integer.parseInt(results.get(0).toString().split(HYPHEN)[2]) + 1)
                    : String.format("%04d", Integer.parseInt(results.get(0).toString()) + 1);

        appointmentNumber = splitStartingYear + HYPHEN + splitEndingYear + HYPHEN + appointmentNumber;

        return appointmentNumber;
    }

    public static List<String> calculateAvailableTimeSlots(String startTime,
                                                           String endTime,
                                                           Duration rosterGapDuration,
                                                           List<AppointmentBookedTimeResponseDTO> bookedAppointments) {

        List<String> availableTimeSlots = new ArrayList<>();

        DateTime startDateTime = new DateTime(FORMAT.parseDateTime(startTime));

        do {
            String date = FORMAT.print(startDateTime);

            if (!isAppointmentDateMatched(bookedAppointments, date))
                availableTimeSlots.add(date);

            startDateTime = startDateTime.plus(rosterGapDuration);
        } while (startDateTime.compareTo(FORMAT.parseDateTime(endTime)) <= 0);

        return availableTimeSlots;
    }

    /*ADD ONLY TIME AFTER AFTER CURRENT TIME*/
    public static List<String> calculateCurrentAvailableTimeSlots(String startTime,
                                                                  String endTime,
                                                                  Duration rosterGapDuration,
                                                                  List<AppointmentBookedTimeResponseDTO> bookedAppointments,
                                                                  Date requestedDate) {

        List<String> availableTimeSlots = new ArrayList<>();

        DateTime startDateTime = new DateTime(FORMAT.parseDateTime(startTime));

        do {
            String date = FORMAT.print(startDateTime);

            if ((!isAppointmentDateMatched(bookedAppointments, date)) && (!hasTimePassed(requestedDate, date)))
                availableTimeSlots.add(date);

            startDateTime = startDateTime.plus(rosterGapDuration);
        } while (startDateTime.compareTo(FORMAT.parseDateTime(endTime)) <= 0);

        return availableTimeSlots;
    }

    public static AppointmentCheckAvailabilityResponseDTO parseToAvailabilityResponse(
            String startTime,
            String endTime,
            Date queryDate,
            List<String> availableTimeSlots) {

        return AppointmentCheckAvailabilityResponseDTO.builder()
                .queryDate(queryDate)
                .doctorAvailableTime(startTime + HYPHEN + endTime)
                .availableTimeSlots(availableTimeSlots)
                .responseStatus(OK)
                .responseCode(OK.value())
                .build();
    }

    private static boolean isAppointmentDateMatched(List<AppointmentBookedTimeResponseDTO> bookedAppointments,
                                                    String date) {
        return bookedAppointments.stream()
                .anyMatch(bookedAppointment -> bookedAppointment.getAppointmentTime().equals(date));
    }

    public static void updateAppointmentDetails(Appointment appointment,
                                                AppointmentRescheduleRequestDTO rescheduleRequestDTO) {

        appointment.setAppointmentDate(rescheduleRequestDTO.getRescheduleDate());

        appointment.setAppointmentTime(parseAppointmentTime(
                rescheduleRequestDTO.getRescheduleDate(),
                rescheduleRequestDTO.getRescheduleTime())
        );

        appointment.setRemarks(rescheduleRequestDTO.getRemarks());
    }

    public static AppointmentRescheduleLog parseToAppointmentRescheduleLog(
            Appointment appointment,
            AppointmentRescheduleRequestDTO rescheduleRequestDTO,
            AppointmentRescheduleLog appointmentRescheduleLog) {

        appointmentRescheduleLog.setAppointmentId(appointment);

        appointmentRescheduleLog.setPreviousAppointmentDate(appointment.getAppointmentTime());

        appointmentRescheduleLog.setRescheduleDate(parseAppointmentTime(
                rescheduleRequestDTO.getRescheduleDate(),
                rescheduleRequestDTO.getRescheduleTime())
        );

        appointmentRescheduleLog.setRemarks(rescheduleRequestDTO.getRemarks());

        appointmentRescheduleLog.setStatus(RESCHEDULED);

        return appointmentRescheduleLog;
    }

    public static AppointmentCountResponseDTO parseToAppointmentCountResponseDTO(Long overAllAppointment, Long newPatient,
                                                                                 Long registeredPatient, Long followUp,
                                                                                 Character pillType) {
        AppointmentCountResponseDTO countResponseDTO = new AppointmentCountResponseDTO();
        countResponseDTO.setTotalAppointment(overAllAppointment);
        countResponseDTO.setNewPatient(newPatient);
        countResponseDTO.setRegisteredPatient(registeredPatient);
        countResponseDTO.setFollowUpPatient(followUp);
        countResponseDTO.setPillType(pillType);

        return countResponseDTO;
    }

    public static void parseRefundRejectDetails(AppointmentRefundRejectDTO refundRejectDTO,
                                                AppointmentRefundDetail refundDetail) {
        refundDetail.setStatus(REJECTED);
        refundDetail.setRemarks(refundRejectDTO.getRemarks());
    }

    public static void parseAppointmentRejectDetails(AppointmentRejectDTO rejectDTO,
                                                     Appointment appointment) {
        appointment.setStatus(REJECTED);
        appointment.setRemarks(rejectDTO.getRemarks());
    }

    public static AppointmentLogResponseDTO parseQueryResultToAppointmentLogResponse(List<Object[]> results) {

        AppointmentLogResponseDTO appointmentLogResponseDTO = new AppointmentLogResponseDTO();

        List<AppointmentLogDTO> appointmentLogSearchDTOS = new ArrayList<>();

        results.forEach(result -> {
            final int APPOINTMENT_DATE_INDEX = 0;
            final int APPOINTMENT_NUMBER_INDEX = 1;
            final int APPOINTMENT_TIME_INDEX = 2;
            final int ESEWA_ID_INDEX = 3;
            final int REGISTRATION_NUMBER_INDEX = 4;
            final int PATIENT_NAME_INDEX = 5;
            final int PATIENT_GENDER_INDEX = 6;
            final int PATIENT_DOB_INDEX = 7;
            final int IS_REGISTERED_INDEX = 8;
            final int PATIENT_MOBILE_NUMBER_INDEX = 9;
            final int SPECIALIZATION_NAME_INDEX = 10;
            final int TRANSACTION_NUMBER_INDEX = 11;
            final int APPOINTMENT_AMOUNT_INDEX = 12;
            final int DOCTOR_NAME_INDEX = 13;
            final int APPOINTMENT_STATUS_INDEX = 14;
            final int REFUND_AMOUNT_INDEX = 15;
            final int PATIENT_ADDRESS_INDEX = 16;
            final int TRANSACTION_DATE_INDEX = 17;
            final int APPOINTMENT_MODE_INDEX = 18;
            final int IS_FOLLOW_UP_INDEX = 19;
            final int REVENUE_AMOUNT_INDEX = 20;

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
                            .appointmentDate(appointmentDate)
                            .appointmentNumber(result[APPOINTMENT_NUMBER_INDEX].toString())
                            .appointmentTime(result[APPOINTMENT_TIME_INDEX].toString())
                            .esewaId(Objects.isNull(result[ESEWA_ID_INDEX]) ? null : result[ESEWA_ID_INDEX].toString())
                            .registrationNumber(registrationNumber)
                            .patientName(result[PATIENT_NAME_INDEX].toString())
                            .patientGender((Gender) result[PATIENT_GENDER_INDEX])
                            .patientDob(patientDob)
                            .patientAge(calculateAge(patientDob))
                            .isRegistered((Character) result[IS_REGISTERED_INDEX])
                            .mobileNumber(result[PATIENT_MOBILE_NUMBER_INDEX].toString())
                            .specializationName(result[SPECIALIZATION_NAME_INDEX].toString())
                            .transactionNumber(Objects.isNull(result[TRANSACTION_NUMBER_INDEX])
                                    ? null : result[TRANSACTION_NUMBER_INDEX].toString())
                            .appointmentAmount(appointmentAmount)
                            .doctorName(result[DOCTOR_NAME_INDEX].toString())
                            .status(result[APPOINTMENT_STATUS_INDEX].toString())
                            .refundAmount(refundAmount)
                            .patientAddress(Objects.isNull(result[PATIENT_ADDRESS_INDEX]) ? "" :
                                    result[PATIENT_ADDRESS_INDEX].toString())
                            .transactionDate((Date) result[TRANSACTION_DATE_INDEX])
                            .appointmentMode(result[APPOINTMENT_MODE_INDEX].toString())
                            .isFollowUp(result[IS_FOLLOW_UP_INDEX].toString().charAt(0))
                            .revenueAmount(Double.parseDouble(result[REVENUE_AMOUNT_INDEX].toString()))
                            .build();

            appointmentLogSearchDTOS.add(appointmentLogDTO);
        });

        appointmentLogResponseDTO.setAppointmentLogs(appointmentLogSearchDTOS);

        return appointmentLogResponseDTO;
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
            final int APPOINTMENT_ID_INDEX = 9;
            final int IS_FOLLOW_UP_INDEX = 10;

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
                    .appointmentId(Long.parseLong(result[APPOINTMENT_ID_INDEX].toString()))
                    .isFollowUp((Character) result[IS_FOLLOW_UP_INDEX])
                    .build();

            appointmentStatusResponseDTOS.add(appointmentStatusResponseDTO);
        });

        return appointmentStatusResponseDTOS;
    }

    public static Map<String, List<AppointmentQueueDTO>> parseQueryResultToAppointmentQueueForTodayByTimeResponse(List<Object[]> results) {

        List<AppointmentQueueSearchByTimeDTO> appointmentQueueSearchByTimeDTOS = new ArrayList<>();

        AppointmentQueueDTO appointmentQueueSearchDTO = new AppointmentQueueDTO();

        List<AppointmentQueueDTO> appointmentQueueByTimeDTOS = new ArrayList<>();

        AtomicReference<Double> totalAmount = new AtomicReference<>(0D);

        results.forEach(result -> {
            final int APPOINTMENT_TIME_INDEX = 0;
            final int DOCTOR_NAME_INDEX = 1;
            final int SPECIALIZATION_NAME_INDEX = 2;
            final int PATIENT_NAME_INDEX = 3;
            final int PATIENT_MOBILE_NUMBER_INDEX = 4;
            final int DOCTOR_AVATAR_INDEX = 5;

            AppointmentTimeDTO appointmentTimeDTO = AppointmentTimeDTO.builder()
                    .appointmentTime(result[APPOINTMENT_TIME_INDEX].toString())
                    .build();

            AppointmentQueueDTO appointmentQueueByTimeDTO =
                    AppointmentQueueDTO.builder()
                            .appointmentTime(appointmentTimeDTO.getAppointmentTime())
                            .doctorName(result[DOCTOR_NAME_INDEX].toString())
                            .specializationName(result[SPECIALIZATION_NAME_INDEX].toString())
                            .patientName(result[PATIENT_NAME_INDEX].toString())
                            .patientMobileNumber(result[PATIENT_MOBILE_NUMBER_INDEX].toString())
                            .doctorAvatar(result[DOCTOR_AVATAR_INDEX].toString())
                            .build();

            appointmentQueueByTimeDTOS.add(appointmentQueueByTimeDTO);

        });

//        appointmentQueueSearchDTO.setAppointmentQueueByTimeDTOList(appointmentQueueByTimeDTOS);
        appointmentQueueSearchDTO.setTotalItems(appointmentQueueByTimeDTOS.size());

        //group by price
        Map<String, List<AppointmentQueueDTO>> groupByPriceMap =
                appointmentQueueByTimeDTOS.stream().collect(Collectors.groupingBy(AppointmentQueueDTO::getAppointmentTime));

        return groupByPriceMap;

    }

    public static StatusResponseDTO parseToStatusResponseDTO() {
        return StatusResponseDTO.builder()
                .responseCode(OK.value())
                .responseStatus(OK)
                .build();
    }

    public static AppointmentMinResponseWithStatusDTO parseToAppointmentMinResponseWithStatusDTO(
            List<AppointmentMinResponseDTO> minResponseDTOList) {

        return AppointmentMinResponseWithStatusDTO.builder()
                .appointmentMinResponseDTOS(minResponseDTOList)
                .responseStatus(OK)
                .responseCode(OK.value())
                .build();
    }

    public static AppointmentDetailResponseWithStatusDTO parseToAppointmentDetailResponseWithStatusDTO(
            AppointmentDetailResponseDTO responseDTO) {

        return AppointmentDetailResponseWithStatusDTO.builder()
                .appointmentDetailResponseDTO(responseDTO)
                .responseStatus(OK)
                .responseCode(OK.value())
                .build();
    }

    public static AppointmentStatistics parseAppointmentStatisticsForNew(Appointment appointment) {
        AppointmentStatistics appointmentStatistics = new AppointmentStatistics();
        appointmentStatistics.setAppointmentId(appointment);
        appointmentStatistics.setAppointmentCreatedDate(new Date());
        appointmentStatistics.setIsNew(YES);

        return appointmentStatistics;
    }

    public static AppointmentStatistics parseAppointmentStatisticsForRegistered(Appointment appointment) {
        AppointmentStatistics appointmentStatistics = new AppointmentStatistics();
        appointmentStatistics.setAppointmentId(appointment);
        appointmentStatistics.setAppointmentCreatedDate(new Date());
        appointmentStatistics.setIsRegistered(YES);

        return appointmentStatistics;
    }

}
