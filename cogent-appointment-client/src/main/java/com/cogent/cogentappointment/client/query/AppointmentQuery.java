package com.cogent.cogentappointment.client.query;

import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentSearchRequestDTO;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author smriti on 2019-10-22
 */
public class AppointmentQuery {

    public static String QUERY_TO_VALIDATE_APPOINTMENT_EXISTS =
            "SELECT COUNT(a.id)" +
                    " FROM  Appointment a" +
                    " WHERE  a.appointmentDate =:appointmentDate" +
                    " AND a.doctorId.id =:doctorId" +
                    " AND a.specializationId.id =:specializationId" +
                    " AND DATE_FORMAT(a.appointmentTime,'%H:%i') =:appointmentTime" +
                    " AND a.status='PA'";

    public static Function<AppointmentSearchRequestDTO, String> QUERY_TO_SEARCH_APPOINTMENT =
            (searchRequestDTO) -> " SELECT" +
                    " a.id as appointmentId," +                                 //[0]
                    " a.patientTypeId.name as patientTypeName," +               //[1]
                    " CASE WHEN" +
                    " p.middleName iS NULL OR p.middleName =''" +
                    " THEN" +
                    " CONCAT" +
                    " (p.firstName, ' ', s.name) " +
                    " ELSE" +
                    " CONCAT" +
                    " (p.firstName, ' ',p.middleName,' ', s.name)" +
                    " END AS patientName, " +                                    //[2]
                    " a.appointmentTypeId.name as appointmentTypeName," +       //[3]
                    " a.appointmentModeId.name as appointmentModeName," +       //[4]
                    " a.doctorId.name as doctorName," +                          //[5]
                    " a.billType.name as billTypeName," +                       //[6]
                    " a.appointmentDate as appointmentDate," +                  //[7]
                    " a.startTime as startTime," +                             //[8]
                    " a.endTime as endTime," +                                 //[9]
                    " a.appointmentNumber as appointmentNumber," +             //[10]
                    " a.status as status" +                                    //[11]
                    " FROM Appointment a" +
                    " LEFT JOIN Patient p ON p.id = a.patientId" +
                    " LEFT JOIN Surname s ON s.id=p.surname.id" +
                    " LEFT JOIN PatientMetaInfo pmi ON p.id = pmi.patient"
                    + GET_WHERE_CLAUSE_TO_SEARCH_APPOINTMENT(searchRequestDTO);

    private static String GET_WHERE_CLAUSE_TO_SEARCH_APPOINTMENT(AppointmentSearchRequestDTO searchRequestDTO) {

        String whereClause = " WHERE a.status!= 'C'";

        if (!Objects.isNull(searchRequestDTO.getPatientTypeId()))
            whereClause += " AND a.patientTypeId = " + searchRequestDTO.getPatientTypeId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getPatientMetaInfo()))
            whereClause += " AND pmi.metaInfo LIKE '%" + searchRequestDTO.getPatientMetaInfo() + "%'";

        if (!Objects.isNull(searchRequestDTO.getAppointmentTypeId()))
            whereClause += " AND a.appointmentTypeId = " + searchRequestDTO.getAppointmentTypeId();

        if (!Objects.isNull(searchRequestDTO.getAppointmentModeId()))
            whereClause += " AND a.appointmentModeId = " + searchRequestDTO.getAppointmentModeId();

        if (!Objects.isNull(searchRequestDTO.getDoctorId()))
            whereClause += " AND a.doctorId = " + searchRequestDTO.getDoctorId();

        if (!Objects.isNull(searchRequestDTO.getBillType()))
            whereClause += " AND a.billType = " + searchRequestDTO.getBillType();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getAppointmentNumber()))
            whereClause += " AND a.appointmentNumber = '" + searchRequestDTO.getAppointmentNumber() + "'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getStatus()))
            whereClause += " AND a.status='" + searchRequestDTO.getStatus() + "'";

        return whereClause;
    }

    public static final String QUERY_TO_FETCH_APPOINTMENT_DETAILS =
            " SELECT" +
                    " a.patientTypeId.id as patientTypeId," +                //[0]
                    " a.patientTypeId.name as patientTypeName," +            //[1]
                    " p.id as patientId," +                                  //[2]
                    " CASE WHEN" +
                    " p.middleName iS NULL OR p.middleName =''" +
                    " THEN" +
                    " CONCAT" +
                    " (p.firstName, ' ', s.name) " +
                    " ELSE" +
                    " CONCAT" +
                    " (p.firstName, ' ',p.middleName,' ', s.name)" +
                    " END AS patientName, " +                                //[3]
                    " a.appointmentTypeId.id as appointmentTypeId," +        //[4]
                    " a.appointmentTypeId.name as appointmentTypeName," +    //[5]
                    " a.appointmentModeId.id as appointmentModeId," +        //[6]
                    " a.appointmentModeId.name as appointmentModeName," +    //[7]
                    " a.doctorId.id as doctorId," +                          //[8]
                    " a.doctorId.name as doctorName," +                      //[9]
                    " a.billType.id as billTypeId," +                       //[10]
                    " a.billType.name as billTypeName," +                   //[11]
                    " a.appointmentDate as appointmentDate," +             //[12]
                    " a.startTime as startTime," +                          //[13]
                    " a.endTime as endTime," +                               //[14]
                    " a.appointmentNumber as appointmentNumber," +          //[15]
                    " a.status as status," +                                //[16]
                    " a.reason as reason," +                                //[17]
                    " a.emergency as emergency," +                          //[18]
                    " a.referredBy as referredBy," +                        //[19]
                    " a.remarks as remarks," +                               //[20]
                    " a.specializationId.id as specializationId, " +        //[21]
                    " a.specializationId.name as specializationName" +      //[22]
                    " FROM Appointment a" +
                    " LEFT JOIN Patient p ON p.id = a.patientId" +
                    " LEFT JOIN Surname s ON s.id=p.surname.id" +
                    " LEFT JOIN PatientMetaInfo pmi ON p.id = pmi.patient" +
                    " WHERE a.id =:id";

    public static String QUERY_TO_FETCH_LATEST_APPOINTMENT_NUMBER =
            "SELECT appointment_number" +
                    " FROM appointment" +
                    " WHERE" +
                    " str_to_date(created_date_nepali,'%Y-%m-%d')" +
                    " BETWEEN :fromDate AND :toDate" +
                    " ORDER BY id DESC LIMIT 1";

    public static String QUERY_TO_FETCH_BOOKED_APPOINTMENT =
            "SELECT DATE_FORMAT(a.appointmentTime, '%H:%i') as appointmentTime" +               //[0]
                    " FROM Appointment a" +
                    " WHERE" +
                    " a.appointmentDate = :date" +
                    " AND a.doctorId.id = :doctorId" +
                    " AND a.specializationId.id = :specializationId" +
                    " AND a.status = 'PA'";

//    public static String QUERY_TO_FETCH_APPOINTMENT_FOR_APPOINTMENT_STATUS(AppointmentStatusRequestDTO requestDTO) {
//
//        String SQL = " SELECT" +
//                " a.appointment_date as date," +                                                        //[0]
//                " GROUP_CONCAT(DATE_FORMAT(a.start_time, '%H:%i'), '-', a.status)" +
//                " as startTimeDetails," +                                                               //[1]
//                " d.id as doctorId," +                                                                  //[2]
//                " d.name as doctorName," +                                                              //[3]
//                " s.id as specializationId," +                                                          //[4]
//                " s.name as specializationName" +                                                       //[5]
//                " FROM appointment a" +
//                " LEFT JOIN doctor d ON d.id = a.doctor_id" +
//                " LEFT JOIN specialization s ON s.id = a.specialization_id" +
//                " WHERE" +
//                " a.appointment_date BETWEEN :fromDate AND :toDate";
//
//        if (!Objects.isNull(requestDTO.getDoctorId()))
//            SQL += " AND d.id =:doctorId";
//
//        if (!Objects.isNull(requestDTO.getSpecializationId()))
//            SQL += " AND s.id = :specializationId ";
//
//        SQL += " GROUP BY a.appointment_date, a.doctor_id, a.specialization_id";
//
//        return SQL;
//    }

    public static final String QUERY_TO_FETCH_BOOKED_APPOINTMENT_DATES =
            "SELECT" +
                    " a.appointmentDate as appointmentDate" +
                    " FROM Appointment a" +
                    " WHERE" +
                    " a.status='PA'" +
                    " AND a.appointmentDate BETWEEN :fromDate AND :toDate" +
                    " AND a.doctorId.id = :doctorId" +
                    " AND a.specializationId.id = :specializationId";

    public static final String QUERY_TO_FETCH_BOOKED_APPOINTMENT_COUNT =
            "SELECT" +
                    " COUNT(a.appointmentDate) as appointmentDate" +
                    " FROM Appointment a" +
                    " WHERE" +
                    " a.status='PA'" +
                    " AND a.appointmentDate BETWEEN :fromDate AND :toDate" +
                    " AND a.doctorId.id = :doctorId" +
                    " AND a.specializationId.id = :specializationId";

    /*%H - hour (e.g., 00,01,02,…12)
    * %i - minutes (e.g., 00,01,02,…12)
    * %p - AM/PM
    * */
    public static final String QUERY_TO_FETCH_PENDING_APPOINTMENTS =
            " SELECT" +
                    " a.id as appointmentId," +                                             //[0]
                    " a.appointmentDate as appointmentDate," +                              //[1]
                    " DATE_FORMAT(a.appointmentTime,'%H:%i %p') as appointmentTime," +                              //[2]
                    " a.appointmentNumber as appointmentNumber," +                          //[3]
                    " p.name as patientName," +                                             //[4]
                    " d.name as doctorName," +                                              //[5]
                    " s.name as specializationName," +                                      //[6]
                    " h.name as hospitalName," +                                             //[7]
                    " atd.appointmentAmount as appointmentAmount" +                          //[8]
                    " FROM Appointment a" +
                    " LEFT JOIN Patient p ON p.id = a.patientId.id" +
                    " LEFT JOIN Doctor d ON d.id = a.doctorId.id" +
                    " LEFT JOIN Specialization s ON s.id = a.specializationId.id" +
                    " LEFT JOIN Hospital h ON h.id = a.hospitalId.id" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id = a.id" +
                    " WHERE a.status = 'PA'" +
                    " AND a.appointmentDate BETWEEN :fromDate AND :toDate" +
                    " ORDER BY a.appointmentDate DESC";

}
