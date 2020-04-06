package com.cogent.cogentappointment.esewa.utils;

import com.cogent.cogentappointment.esewa.constants.StringConstant;
import com.cogent.cogentappointment.esewa.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.doctorDutyRoster.DoctorDutyRosterRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.doctorDutyRoster.DoctorDutyRosterUpdateRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.doctorDutyRoster.DoctorWeekDaysDutyRosterRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.doctorDutyRoster.DoctorWeekDaysDutyRosterUpdateRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.AppointmentBookedDateResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.doctorDutyRoster.*;
import com.cogent.cogentappointment.esewa.exception.BadRequestException;
import com.cogent.cogentappointment.persistence.model.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.cogent.cogentappointment.esewa.constants.ErrorMessageConstants.DoctorDutyRosterServiceMessages.APPOINTMENT_EXISTS_ON_WEEK_DAY_MESSAGE;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.convertDateToLocalDate;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.isLocalDateBetweenInclusive;

/**
 * @author smriti on 27/11/2019
 */
public class DoctorDutyRosterUtils {

    public static DoctorDutyRosterDetailResponseDTO parseToDoctorDutyRosterDetailResponseDTO(
            DoctorDutyRosterResponseDTO dutyRosterResponseDTO,
            List<DoctorWeekDaysDutyRosterResponseDTO> weekDaysRosters,
            List<DoctorDutyRosterOverrideResponseDTO> overrideRosters) {

        return DoctorDutyRosterDetailResponseDTO.builder()
                .doctorDutyRosterInfo(dutyRosterResponseDTO)
                .weekDaysRosters(weekDaysRosters)
                .overrideRosters(overrideRosters)
                .build();
    }

    /*ADD TO FINAL LIST ONLY IF QUERY RESULT IS WITHIN THE SELECTED SEARCH DATE RANGE*/
    public static List<DoctorDutyRosterStatusResponseDTO> parseQueryResultToDoctorDutyRosterStatusResponseDTOS(
            List<Object[]> queryResults,
            Date searchFromDate,
            Date searchToDate) {

        List<DoctorDutyRosterStatusResponseDTO> doctorDutyRosterStatusResponseDTOS = new ArrayList<>();

        LocalDate searchFromLocalDate = convertDateToLocalDate(searchFromDate);
        LocalDate searchToLocalDate = convertDateToLocalDate(searchToDate);

        queryResults.forEach(result -> {

            final int START_DATE_INDEX = 0;
            final int END_DATE_INDEX = 1;
            final int DOCTOR_TIME_DETAILS_INDEX = 2;
            final int DOCTOR_ID_INDEX = 3;
            final int DOCTOR_NAME_INDEX = 4;
            final int SPECIALIZATION_ID_INDEX = 5;
            final int SPECIALIZATION_NAME_INDEX = 6;
            final int ROSTER_GAP_DURATION_INDEX = 7;

            LocalDate startLocalDate = convertDateToLocalDate((Date) result[START_DATE_INDEX]);
            LocalDate endLocalDate = convertDateToLocalDate((Date) result[END_DATE_INDEX]);

            List<String> timeDetails = Arrays.asList(result[DOCTOR_TIME_DETAILS_INDEX].toString()
                    .split(StringConstant.COMMA_SEPARATED));

            Stream.iterate(startLocalDate, date -> date.plusDays(1))
                    .limit(ChronoUnit.DAYS.between(startLocalDate, endLocalDate) + 1)
                    .forEach(localDate -> {

                        if (isLocalDateBetweenInclusive(searchFromLocalDate, searchToLocalDate, localDate)) {

                            String dayOfWeek = localDate.getDayOfWeek().toString();

                            String weekMatched = timeDetails.stream()
                                    .filter(str -> str.contains(dayOfWeek))
                                    .findAny().orElse(null);

                            if (!Objects.isNull(weekMatched)) {

                                /*START TIME - END TIME - DAY OFF STATUS - WEEK NAME*/
                                String[] weekMatchedSplit = weekMatched.split(StringConstant.HYPHEN);

                                DoctorDutyRosterStatusResponseDTO responseDTO = DoctorDutyRosterStatusResponseDTO.builder()
                                        .date(localDate)
                                        .startTime(weekMatchedSplit[0])
                                        .endTime(weekMatchedSplit[1])
                                        .dayOffStatus(weekMatchedSplit[2].charAt(0))
                                        .doctorId(Long.parseLong(result[DOCTOR_ID_INDEX].toString()))
                                        .doctorName(result[DOCTOR_NAME_INDEX].toString())
                                        .specializationId(Long.parseLong(result[SPECIALIZATION_ID_INDEX].toString()))
                                        .specializationName(result[SPECIALIZATION_NAME_INDEX].toString())
                                        .rosterGapDuration(Integer.parseInt(result[ROSTER_GAP_DURATION_INDEX].toString()))
                                        .build();

                                doctorDutyRosterStatusResponseDTOS.add(responseDTO);
                            }
                        }
                    });
        });

        return doctorDutyRosterStatusResponseDTOS;
    }

    public static DoctorExistingDutyRosterDetailResponseDTO parseToExistingRosterDetails(
            List<DoctorWeekDaysDutyRosterResponseDTO> weekDaysRosters,
            List<DoctorDutyRosterOverrideResponseDTO> overrideRosters) {

        return DoctorExistingDutyRosterDetailResponseDTO.builder()
                .weekDaysRosters(weekDaysRosters)
                .overrideRosters(overrideRosters)
                .build();
    }
}
