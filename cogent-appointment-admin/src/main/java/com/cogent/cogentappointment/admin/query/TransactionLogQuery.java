package com.cogent.cogentappointment.admin.query;

import com.cogent.cogentappointment.admin.dto.request.appointment.AppointmentLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.TransactionLogSearchDTO;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.function.Function;

import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.utilDateToSqlDate;

/**
 * @author Sauravi Thapa ON 4/19/20
 */
public class TransactionLogQuery {

    private static String SELECT_CLAUSE_FOR_LOGS =
            "SELECT" +
                    " h.name as hospitalName," +                                    //[0]
                    " a.appointmentDate as appointmentDate," +                      //[1]
                    " a.appointmentNumber as appointmentNumber," +                  //[2]
                    " DATE_FORMAT(a.appointmentTime, '%h:%i %p') as appointmentTime," +          //[3]
                    " p.eSewaId as esewaId," +                                      //[4]
                    " hpi.registrationNumber as registrationNumber," +                //[5]
                    " p.name as patientName," +                                     //[6]
                    " p.gender as patientGender," +                                 //[7]
                    " p.dateOfBirth as patientDob," +                               //[8]
                    " hpi.isRegistered as isRegistered," +                          //[9]
                    " p.mobileNumber as mobileNumber," +                            //[10]
                    " sp.name as specializationName," +                             //[11]
                    " atd.transactionNumber as transactionNumber," +                //[12]
                    " atd.appointmentAmount as appointmentAmount," +                //[13]
                    " d.name as doctorName," +                                     //[14]
                    " a.status as status," +                                       //[15]
                    " ard.refundAmount as refundAmount," +                         //[16]
                    " atd.transactionDate as transactionDate," +                    //[17]
                    " am.name as appointmentMode," +                                //[18]
                    " a.isFollowUp as isFollowUp," +                                //[19]
                    " hpi.address as patientAddress," +                            //[20]
                    " DATE_FORMAT(atd.transactionDateTime, '%h:%i %p') as appointmentTime," + //[21]
                    " (atd.appointmentAmount - COALESCE(ard.refundAmount,0)) as revenueAmount" + //[22]
                    " FROM Appointment a" +
                    " LEFT JOIN AppointmentMode am On am.id=a.appointmentModeId.id" +
                    " LEFT JOIN Patient p ON a.patientId.id=p.id" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " LEFT JOIN Doctor d ON d.id = a.doctorId.id" +
                    " LEFT JOIN Specialization sp ON a.specializationId=sp.id" +
                    " LEFT JOIN Hospital h ON a.hospitalId.id=h.id" +
                    " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                    " LEFT JOIN AppointmentRefundDetail ard ON a.id=ard.appointmentId";

    public static Function<TransactionLogSearchDTO, String> QUERY_TO_FETCH_TRANSACTION_LOGS =
            (transactionLogSearchDTO) ->
                    SELECT_CLAUSE_FOR_LOGS
                            + GET_WHERE_CLAUSE_TO_SEARCH_TRANSACTION_LOG_DETAILS(transactionLogSearchDTO);

    private static String GET_WHERE_CLAUSE_TO_SEARCH_TRANSACTION_LOG_DETAILS(
            TransactionLogSearchDTO transactionLogSearchDTO) {

        String whereClause = " WHERE " +
                " sp.status='Y' ";

        if (!ObjectUtils.isEmpty(transactionLogSearchDTO.getFromDate())
                && !ObjectUtils.isEmpty(transactionLogSearchDTO.getToDate()))
            whereClause += " AND (atd.transactionDate BETWEEN '" +
                    utilDateToSqlDate(transactionLogSearchDTO.getFromDate())
                    + "' AND '" + utilDateToSqlDate(transactionLogSearchDTO.getToDate()) + "')";

        if (!ObjectUtils.isEmpty(transactionLogSearchDTO.getTransactionNumber()))
            whereClause += " AND atd.transactionNumber LIKE '%" + transactionLogSearchDTO.getTransactionNumber() + "%'";

        if (!Objects.isNull(transactionLogSearchDTO.getStatus()) && !transactionLogSearchDTO.getStatus().equals(""))
            whereClause += " AND a.status = '" + transactionLogSearchDTO.getStatus() + "'";

        if (!Objects.isNull(transactionLogSearchDTO.getHospitalId()))
            whereClause += " AND h.id = " + transactionLogSearchDTO.getHospitalId();

        if (!Objects.isNull(transactionLogSearchDTO.getPatientMetaInfoId()))
            whereClause += " AND pi.id = " + transactionLogSearchDTO.getPatientMetaInfoId();

        if (!Objects.isNull(transactionLogSearchDTO.getSpecializationId()))
            whereClause += " AND sp.id = " + transactionLogSearchDTO.getSpecializationId();

        if (!ObjectUtils.isEmpty(transactionLogSearchDTO.getPatientType()))
            whereClause += " AND hpi.isRegistered = '" + transactionLogSearchDTO.getPatientType() + "'";

        if (!ObjectUtils.isEmpty(transactionLogSearchDTO.getAppointmentCategory()))
            whereClause += " AND a.isSelf = '" + transactionLogSearchDTO.getAppointmentCategory() + "'";

        if (!Objects.isNull(transactionLogSearchDTO.getDoctorId()))
            whereClause += " AND d.id = " + transactionLogSearchDTO.getDoctorId();

        whereClause += " ORDER BY a.appointmentDate DESC ";

        return whereClause;
    }

    private static String SELECT_CLAUSE_TO_GET_TOTAL_AMOUNT =
            "SELECT" +
                    " COALESCE (SUM(atd.appointmentAmount),0) - COALESCE(SUM(ard.refundAmount),0 ) as totalAmount" +
                    " FROM Appointment a" +
                    " LEFT JOIN Patient p ON a.patientId.id=p.id" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " LEFT JOIN Doctor d ON d.id = a.doctorId.id" +
                    " LEFT JOIN Specialization sp ON a.specializationId.id=sp.id" +
                    " LEFT JOIN Hospital h ON a.hospitalId.id=h.id" +
                    " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id AND pi.status='Y'" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                    " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id AND ard.status='A'" +
                    " WHERE" +
                    " sp.status='Y'";

    private static String SELECT_CLAUSE_TO_GET_AMOUNT_AND_APPOINTMENT_COUNT =
            "SELECT" +
                    " COUNT(a.id)," +
                    " COALESCE(SUM(atd.appointmentAmount ),0)" +
                    " FROM Appointment a" +
                    " LEFT JOIN Patient p ON a.patientId.id=p.id" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " LEFT JOIN Doctor d ON d.id = a.doctorId.id" +
                    " LEFT JOIN Specialization sp ON a.specializationId.id=sp.id" +
                    " LEFT JOIN Hospital h ON a.hospitalId.id=h.id" +
                    " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id AND pi.status='Y'" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                    " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id AND ard.status='A'" +
                    " WHERE";

    public static String QUERY_TO_FETCH_TOTAL_APPOINTMENT_AMOUNT_FOR_TRANSACTION_LOG(TransactionLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_GET_TOTAL_AMOUNT;

        return QUERY_TO_SEARCH_BY_DATES(query, searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_BOOKED_APPOINTMENT_FOR_TXN_LOG(TransactionLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_GET_AMOUNT_AND_APPOINTMENT_COUNT +
                " a.status='PA' AND a.isFollowUp = 'N'";

        return QUERY_TO_SEARCH_BY_DATES(query, searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_BOOKED_APPOINTMENT_WITH_FOLLOW_UP_FOR_TXN_LOG
            (TransactionLogSearchDTO searchRequestDTO) {

        String query = SELECT_CLAUSE_TO_GET_AMOUNT_AND_APPOINTMENT_COUNT +
                " a.status='PA' AND a.isFollowUp = 'Y'";

        return QUERY_TO_SEARCH_BY_DATES(query, searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_CHECKED_IN_APPOINTMENT_FOR_TXN_LOG(TransactionLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_GET_AMOUNT_AND_APPOINTMENT_COUNT +
                " a.status='A' AND a.isFollowUp = 'N'";

        return QUERY_TO_SEARCH_BY_DATES(query, searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_CHECKED_IN_APPOINTMENT_WITH_FOLLOW_UP_FOR_TXN_LOG(TransactionLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_GET_AMOUNT_AND_APPOINTMENT_COUNT +
                " a.status='A' AND a.isFollowUp = 'Y'";

        return QUERY_TO_SEARCH_BY_DATES(query, searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_CANCELLED_APPOINTMENT_FOR_TXN_LOG(TransactionLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_GET_AMOUNT_AND_APPOINTMENT_COUNT +
                " a.status='C' AND a.isFollowUp = 'N'";

        return QUERY_TO_SEARCH_BY_DATES(query, searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_CANCELLED_APPOINTMENT_WITH_FOLLOW_UP_FOR_TXN_LOG(TransactionLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_GET_AMOUNT_AND_APPOINTMENT_COUNT +
                " a.status='C' AND a.isFollowUp = 'Y'";

        return QUERY_TO_SEARCH_BY_DATES(query, searchRequestDTO);
    }

    private static String SELECT_CLAUSE_TO_FETCH_REFUNDED_APPOINTMENT =
            "SELECT" +
                    " COUNT(a.id)," +
                    " COALESCE (SUM(ard.refundAmount ),0) as amount" +
                    " FROM Appointment a" +
                    " LEFT JOIN Patient p ON a.patientId.id=p.id" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " LEFT JOIN Doctor d ON d.id = a.doctorId.id" +
                    " LEFT JOIN Specialization sp ON a.specializationId.id=sp.id" +
                    " LEFT JOIN Hospital h ON a.hospitalId.id=h.id" +
                    " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id AND pi.status='Y'" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                    " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id AND ard.status='A'" +
                    " WHERE" +
                    " a.status='RE'";

    public static String QUERY_TO_FETCH_REFUNDED_APPOINTMENT_FOR_TXN_LOG(TransactionLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_FETCH_REFUNDED_APPOINTMENT +
                " AND a.isFollowUp = 'N'";

        return QUERY_TO_SEARCH_BY_DATES(query, searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_REFUNDED_APPOINTMENT_WITH_FOLLOW_UP_FOR_TXN_LOG(TransactionLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_FETCH_REFUNDED_APPOINTMENT +
                " AND a.isFollowUp = 'Y'";

        return QUERY_TO_SEARCH_BY_DATES(query, searchRequestDTO);
    }

    private static String SELECT_CLAUSE_TO_FETCH_REVENUE_REFUNDED_APPOINTMENT =
            "SELECT" +
                    " COUNT(a.id)," +
                    " (COALESCE(SUM(atd.appointmentAmount ),0) - COALESCE(SUM(ard.refundAmount ),0)) as amount" +
                    " FROM Appointment a" +
                    " LEFT JOIN Patient p ON a.patientId.id=p.id" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " LEFT JOIN Doctor d ON d.id = a.doctorId.id" +
                    " LEFT JOIN Specialization sp ON a.specializationId.id=sp.id" +
                    " LEFT JOIN Hospital h ON a.hospitalId.id=h.id" +
                    " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id AND pi.status='Y'" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                    " LEFT JOIN AppointmentRefundDetail ard ON ard.appointmentId=a.id AND ard.status='A'" +
                    " WHERE" +
                    " a.status='RE'";

    public static String QUERY_TO_FETCH_REVENUE_REFUNDED_APPOINTMENT_FOR_TXN_LOG(TransactionLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_FETCH_REVENUE_REFUNDED_APPOINTMENT +
                " AND a.isFollowUp = 'N'";

        return QUERY_TO_SEARCH_BY_DATES(query, searchRequestDTO);
    }

    public static String QUERY_TO_FETCH_REVENUE_REFUNDED_APPOINTMENT_WITH_FOLLOW_UP_FOR_TXN_LOG(TransactionLogSearchDTO searchRequestDTO) {
        String query = SELECT_CLAUSE_TO_FETCH_REVENUE_REFUNDED_APPOINTMENT +
                " AND a.isFollowUp = 'Y'";

        return QUERY_TO_SEARCH_BY_DATES(query, searchRequestDTO);
    }

    private static String QUERY_TO_SEARCH_BY_DATES(String query, TransactionLogSearchDTO searchRequestDTO) {

        if (!Objects.isNull(searchRequestDTO.getHospitalId()))
            query += " AND h.id = " + searchRequestDTO.getHospitalId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getFromDate())
                && !ObjectUtils.isEmpty(searchRequestDTO.getToDate()))
            query += " AND (atd.transactionDate BETWEEN '" +
                    utilDateToSqlDate(searchRequestDTO.getFromDate())
                    + "' AND '" + utilDateToSqlDate(searchRequestDTO.getToDate()) + "')";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getTransactionNumber()))
            query += " AND atd.transactionNumber LIKE '%" + searchRequestDTO.getTransactionNumber() + "%'";

        if (!Objects.isNull(searchRequestDTO.getStatus()) && !searchRequestDTO.getStatus().equals(""))
            query += " AND a.status = '" + searchRequestDTO.getStatus() + "'";

        if (!Objects.isNull(searchRequestDTO.getPatientMetaInfoId()))
            query += " AND pi.id = " + searchRequestDTO.getPatientMetaInfoId();

        if (!Objects.isNull(searchRequestDTO.getSpecializationId()))
            query += " AND sp.id = " + searchRequestDTO.getSpecializationId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getPatientType()))
            query += " AND hpi.isRegistered = '" + searchRequestDTO.getPatientType() + "'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getAppointmentCategory()))
            query += " AND a.isSelf = '" + searchRequestDTO.getAppointmentCategory() + "'";

        if (!Objects.isNull(searchRequestDTO.getDoctorId()))
            query += " AND d.id = " + searchRequestDTO.getDoctorId();

        query += " ORDER BY a.appointmentDate DESC ";

        return query;
    }
}
