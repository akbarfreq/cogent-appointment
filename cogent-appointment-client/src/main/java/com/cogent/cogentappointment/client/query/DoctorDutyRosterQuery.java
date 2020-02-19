package com.cogent.cogentappointment.client.query;

import com.cogent.cogentappointment.client.dto.request.doctorDutyRoster.DoctorDutyRosterSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.doctorDutyRoster.DoctorDutyRosterStatusRequestDTO;

import java.util.Objects;

/**
 * @author smriti on 26/11/2019
 */
public class DoctorDutyRosterQuery {

    public static final String VALIDATE_DOCTOR_DUTY_ROSTER_COUNT =
            " SELECT COUNT(d.id)" +
                    " FROM DoctorDutyRoster d" +
                    " WHERE d.status != 'D'" +
                    " AND d.doctorId.id=:doctorId" +
                    " AND d.specializationId.id= :specializationId" +
                    " AND d.toDate >=:fromDate" +
                    " AND d.fromDate <=:toDate";

    public static String QUERY_TO_SEARCH_DOCTOR_DUTY_ROSTER(DoctorDutyRosterSearchRequestDTO searchRequestDTO) {

        String sql = " SELECT" +
                " ddr.id as id," +                                                      //[0]
                " d.name as doctorName," +                                              //[1]
                " s.name as specializationName," +                                      //[2]
                " ddr.rosterGapDuration as rosterGapDuration," +                        //[3]
                " ddr.fromDate as fromDate," +                                          //[4]
                " ddr.toDate as toDate," +                                              //[5]
                " ddr.status as status" +                                               //[6]
                " FROM DoctorDutyRoster ddr" +
                " LEFT JOIN Doctor d ON ddr.doctorId.id = d.id" +
                " LEFT JOIN Specialization s ON ddr.specializationId.id = s.id" +
                " WHERE" +
                " ddr.status !='D'" +
                " AND ddr.toDate >=:fromDate AND ddr.fromDate <=:toDate";

        if (!Objects.isNull(searchRequestDTO.getDoctorId()))
            sql += " AND d.id = " + searchRequestDTO.getDoctorId();

        if (!Objects.isNull(searchRequestDTO.getSpecializationId()))
            sql += " AND s.id = " + searchRequestDTO.getSpecializationId();

        return sql;
    }

    public static final String QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_DETAILS =
            " SELECT" +
                    " ddr.id as id," +                                                  //[0]
                    " d.id as doctorId," +                                               //[1]
                    " d.name as doctorName," +                                          //[2]
                    " s.id as specializationId," +                                      //[3]
                    " s.name as specializationName," +                                  //[4]
                    " ddr.rosterGapDuration as rosterGapDuration," +                    //[5]
                    " ddr.fromDate as fromDate," +                                      //[6]
                    " ddr.toDate as toDate," +                                          //[7]
                    " ddr.status as status," +                                          //[8]
                    " ddr.remarks as remarks," +                                        //[9]
                    " ddr.hasOverrideDutyRoster as hasOverrideDutyRoster" +             //[10]
                    " FROM DoctorDutyRoster ddr" +
                    " LEFT JOIN Doctor d ON ddr.doctorId.id = d.id" +
                    " LEFT JOIN Specialization s ON ddr.specializationId.id = s.id" +
                    " WHERE ddr.status !='D'" +
                    " AND ddr.id = :id" +
                    " AND ddr.hospitalId.id=:hospitalId";

    public static final String QUERY_TO_FETCH_DOCTOR_WEEK_DAYS_DUTY_ROSTER =
            " SELECT" +
                    " dw.id as doctorWeekDaysDutyRosterId," +                           //[0]
                    " dw.startTime as startTime," +                                     //[1]
                    " dw.endTime as endTime," +                                         //[2]
                    " dw.dayOffStatus as dayOffStatus," +                               //[3]
                    " dw.weekDaysId.id as weekDaysId," +                                //[4]
                    " dw.weekDaysId.name as weekDaysName" +                             //[5]
                    " FROM DoctorWeekDaysDutyRoster dw" +
                    " WHERE" +
                    " dw.doctorDutyRosterId.status !='D'" +
                    " AND dw.doctorDutyRosterId.id = :id";

    public static final String QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_TIME =
            " SELECT" +
                    " dw.startTime as startTime," +                                     //[0]
                    " dw.endTime as endTime," +                                         //[1]
                    " dw.dayOffStatus as dayOffStatus," +                               //[2]
                    " d.rosterGapDuration as rosterGapDuration" +                       //[3]
                    " FROM DoctorDutyRoster d" +
                    " LEFT JOIN DoctorWeekDaysDutyRoster dw ON dw.doctorDutyRosterId.id = d.id" +
                    " LEFT JOIN WeekDays w ON w.id = dw.weekDaysId.id" +
                    " WHERE" +
                    " d.status = 'Y'" +
                    " AND :date BETWEEN d.fromDate AND d.toDate" +
                    " AND d.doctorId.id = :doctorId" +
                    " AND d.specializationId.id = :specializationId" +
                    " AND w.code = :code";

    public static String QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_STATUS(DoctorDutyRosterStatusRequestDTO requestDTO) {

        String SQL = "SELECT" +
                " d.from_date," +                                       //[0]
                " d.to_date," +                                         //[1]
                " GROUP_CONCAT((CONCAT(" +
                " DATE_FORMAT(dw.start_time, '%H:%i')," +
                " '-'," +
                " DATE_FORMAT(dw.end_time, '%H:%i'), " +
                " '-'," +
                " dw.day_off_status," +
                " '-'," +
                " w.name))) as doctorTimeDetails," +                      //[2]
                " dr.id as doctorId, " +                                  //[3]
                " dr.name as doctorName," +                               //[4]
                " s.id as specializationId," +                            //[5]
                " s.name as specializationName," +                        //[6]
                " d.roster_gap_duration as rosterGapDuration" +           //[7]
                " FROM doctor_duty_roster d" +
                " LEFT JOIN doctor_week_days_duty_roster dw ON d.id = dw.doctor_duty_roster_id" +
                " LEFT JOIN week_days w ON w.id = dw.week_days_id" +
                " LEFT JOIN doctor dr ON dr.id = d.doctor_id" +
                " LEFT JOIN specialization s ON s.id = d.specialization_id" +
                " WHERE d.status = 'Y'" +
                " AND d.to_date >=:fromDate" +
                " AND d.from_date <=:toDate";

        if (!Objects.isNull(requestDTO.getDoctorId()))
            SQL += " AND dr.id = :doctorId";

        if (!Objects.isNull(requestDTO.getSpecializationId()))
            SQL += " AND s.id = :specializationId";

        SQL += " GROUP BY d.id";

        return SQL;
    }

    public static final String QUERY_TO_FETCH_EXISTING_DOCTOR_DUTY_ROSTERS =
            " SELECT" +
                    " dd.id as doctorDutyRosterId," +                                      //[0]
                    " dd.fromDate as fromDate," +                                          //[1]
                    " dd.toDate as toDate," +                                              //[2]
                    " dd.rosterGapDuration as rosterGapDuration" +                         //[3]
                    " FROM DoctorDutyRoster dd" +
                    " WHERE dd.status != 'D'" +
                    " AND dd.doctorId.id=:doctorId" +
                    " AND dd.specializationId.id= :specializationId" +
                    " AND dd.toDate >=:fromDate" +
                    " AND dd.fromDate <=:toDate";

    public static final String QUERY_TO_CHECK_IF_OVERRIDE_EXISTS =
            " SELECT" +
                    " dd.hasOverrideDutyRoster as hasOverrideDutyRoster" +             //[0]
                    " FROM DoctorDutyRoster dd" +
                    " WHERE dd.status !='D'" +
                    " AND dd.id = :id";

}
