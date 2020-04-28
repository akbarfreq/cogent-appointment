package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.request.DoctorRevenueRequestDTO;
import com.cogent.cogentappointment.client.dto.request.dashboard.DashBoardRequestDTO;
import com.cogent.cogentappointment.client.dto.response.commons.AppointmentRevenueStatisticsResponseDTO;
import com.cogent.cogentappointment.client.dto.response.dashboard.DoctorFollowUpResponse;
import com.cogent.cogentappointment.client.dto.response.dashboard.DoctorRevenueDTO;
import com.cogent.cogentappointment.client.dto.response.dashboard.RevenueTrendResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.custom.AppointmentTransactionDetailRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AppointmentTransactionDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.client.constants.QueryConstants.*;
import static com.cogent.cogentappointment.client.query.DashBoardQuery.*;
import static com.cogent.cogentappointment.client.utils.AppointmentRevenueStatisticsUtils.*;
import static com.cogent.cogentappointment.client.utils.DashboardUtils.revenueStatisticsResponseDTO;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.utilDateToSqlDate;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.transformQueryToResultList;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.transformQueryToSingleResult;

/**
 * @author Sauravi Thapa २०/२/१०
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class AppointmentTransactionDetailRepositoryCustomImpl implements AppointmentTransactionDetailRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Double getRevenueByDates(Date toDate, Date fromDate, Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_GET_REVENUE_BY_DATE)
                .setParameter(TO_DATE, toDate)
                .setParameter(FROM_DATE, fromDate)
                .setParameter(HOSPITAL_ID, hospitalId);

        Double count = (Double) query.getSingleResult();

        return (count == null) ? 0D : count;
    }

    @Override
    public AppointmentRevenueStatisticsResponseDTO calculateAppointmentStatistics(Date toDate,
                                                                                  Date fromDate,
                                                                                  Long hospitalId) {

        AppointmentRevenueStatisticsResponseDTO responseDTO = new AppointmentRevenueStatisticsResponseDTO();

        calculateBookedAppointmentStatistics(toDate, fromDate, hospitalId, responseDTO);

        calculateCheckedInAppointmentStatistics(toDate, fromDate, hospitalId, responseDTO);

        calculateCancelledAppointmentStatistics(toDate, fromDate, hospitalId, responseDTO);

        calculateRefundedAppointmentStatistics(toDate, fromDate, hospitalId, responseDTO);

        calculateRevenueFromRefundedAppointmentStatistics(toDate, fromDate, hospitalId, responseDTO);

        calculateTotalRevenueExcludingBooked(responseDTO);

        calculateTotalRevenueIncludingBooked(responseDTO);

        return responseDTO;
    }

    /*Revenue from Booked*/
    private void calculateBookedAppointmentStatistics(Date toDate,
                                                      Date fromDate,
                                                      Long hospitalId,
                                                      AppointmentRevenueStatisticsResponseDTO responseDTO) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_BOOKED_APPOINTMENT_REVENUE)
                .setParameter(FROM_DATE, fromDate)
                .setParameter(TO_DATE, toDate)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<Object[]> results = query.getResultList();

        parseBookedAppointmentDetails(results.get(0), responseDTO);
    }

    /*Revenue from Checked-In*/
    private void calculateCheckedInAppointmentStatistics(Date toDate,
                                                         Date fromDate,
                                                         Long hospitalId,
                                                         AppointmentRevenueStatisticsResponseDTO responseDTO) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_CHECKED_IN_APPOINTMENT_REVENUE)
                .setParameter(FROM_DATE, fromDate)
                .setParameter(TO_DATE, toDate)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<Object[]> results = query.getResultList();

        parseCheckedInAppointmentDetails(results.get(0), responseDTO);
    }

    /*Revenue from Cancelled*/
    private void calculateCancelledAppointmentStatistics(Date toDate,
                                                         Date fromDate,
                                                         Long hospitalId,
                                                         AppointmentRevenueStatisticsResponseDTO responseDTO) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_CANCELLED_APPOINTMENT_REVENUE)
                .setParameter(FROM_DATE, fromDate)
                .setParameter(TO_DATE, toDate)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<Object[]> results = query.getResultList();

        parseCancelledAppointmentDetails(results.get(0), responseDTO);
    }

    /*Refund Amount*/
    private void calculateRefundedAppointmentStatistics(Date toDate,
                                                        Date fromDate,
                                                        Long hospitalId,
                                                        AppointmentRevenueStatisticsResponseDTO responseDTO) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_REFUNDED_APPOINTMENT_AMOUNT)
                .setParameter(FROM_DATE, fromDate)
                .setParameter(TO_DATE, toDate)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<Object[]> results = query.getResultList();

        parseRefundedAppointmentDetails(results.get(0), responseDTO);
    }

    /*Revenue from Refund*/
    private void calculateRevenueFromRefundedAppointmentStatistics(Date toDate,
                                                                   Date fromDate,
                                                                   Long hospitalId,
                                                                   AppointmentRevenueStatisticsResponseDTO responseDTO) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_REVENUE_FROM_REFUNDED_APPOINTMENT)
                .setParameter(FROM_DATE, fromDate)
                .setParameter(TO_DATE, toDate)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<Object[]> results = query.getResultList();

        parseRevenueFromRefundedAppointmentDetails(results.get(0), responseDTO);
    }

    /*Client Revenue Amount = Revenue from Checked-In+ Revenue from Refund + Revenue from Cancelled */
    private void calculateTotalRevenueExcludingBooked(AppointmentRevenueStatisticsResponseDTO responseDTO) {
        Double totalAmount = responseDTO.getCheckedInAmount() +
                responseDTO.getRevenueFromRefundedAmount() +
                responseDTO.getCancelAmount();
        responseDTO.setTotalAmountExcludingBooked(totalAmount);
    }

    /*Client Revenue Amount Including Booked Appointments  =
     Revenue from Checked-In + Revenue from Refund + Revenue from Cancelled + Revenue from Booked*/
    private void calculateTotalRevenueIncludingBooked(AppointmentRevenueStatisticsResponseDTO responseDTO) {
        Double totalAmount = responseDTO.getCheckedInAmount() +
                responseDTO.getRevenueFromRefundedAmount() +
                responseDTO.getCancelAmount() +
                responseDTO.getBookedAmount();
        responseDTO.setTotalAmount(totalAmount);
    }

    @Override
    public RevenueTrendResponseDTO getRevenueTrend(DashBoardRequestDTO dashBoardRequestDTO,
                                                   Long hospitalId, Character filter) {

        final String queryByFilter = getQueryByFilter(filter);

        Query query = createQuery.apply(entityManager, queryByFilter)
                .setParameter(TO_DATE, dashBoardRequestDTO.getToDate())
                .setParameter(FROM_DATE, dashBoardRequestDTO.getFromDate())
                .setParameter(HOSPITAL_ID, hospitalId);
        List<Object[]> objects = query.getResultList();

        RevenueTrendResponseDTO responseDTO = revenueStatisticsResponseDTO(objects, filter);

        return responseDTO;
    }

    @Override
    public List<DoctorRevenueDTO> calculateDoctorRevenue(DoctorRevenueRequestDTO doctorRevenueRequestDTO) {

        Query query = createQuery.apply(entityManager, QUERY_TO_CALCULATE_DOCTOR_REVENUE(doctorRevenueRequestDTO))
                .setParameter(FROM_DATE, utilDateToSqlDate(doctorRevenueRequestDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(doctorRevenueRequestDTO.getToDate()))
                .setParameter(HOSPITAL_ID, doctorRevenueRequestDTO.getHospitalId());

        List<DoctorRevenueDTO> doctorRevenueDTOList = transformQueryToResultList(query, DoctorRevenueDTO.class);

        doctorRevenueDTOList.forEach(doctorRevenueDTO -> {
            Query queryToGetFollowUp = createQuery.apply(entityManager, QUERY_TO_GET_FOLLOW_UP)
                    .setParameter(FROM_DATE, utilDateToSqlDate(doctorRevenueRequestDTO.getFromDate()))
                    .setParameter(TO_DATE, utilDateToSqlDate(doctorRevenueRequestDTO.getToDate()))
                    .setParameter(DOCTOR_ID, doctorRevenueDTO.getDoctorId());
            DoctorFollowUpResponse doctorFollowUpResponse= transformQueryToSingleResult(queryToGetFollowUp,
                    DoctorFollowUpResponse.class);
            doctorRevenueDTO.setTotalFollowUp(doctorFollowUpResponse.getFollowUpCount());
            doctorRevenueDTO.setTotalRevenue(doctorRevenueDTO.getTotalRevenue()+doctorFollowUpResponse.getFollowUpAmount());
        });


        return doctorRevenueDTOList;
    }

    @Override
    public List<DoctorRevenueDTO> calculateCancelledRevenue(DoctorRevenueRequestDTO doctorRevenueRequestDTO) {

        Query query = createQuery.apply(entityManager, QUERY_TO_CALCULATE_COMPANY_REVENUE(doctorRevenueRequestDTO))
                .setParameter(FROM_DATE, utilDateToSqlDate(doctorRevenueRequestDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(doctorRevenueRequestDTO.getToDate()))
                .setParameter(HOSPITAL_ID, doctorRevenueRequestDTO.getHospitalId());

        return transformQueryToResultList(query, DoctorRevenueDTO.class);
    }

    private String getQueryByFilter(Character filter) {
        Map<Character, String> queriesWithFilterAsKey = new HashMap<>();
        queriesWithFilterAsKey.put('D', QUERY_TO_FETCH_REVENUE_DAILY);
        queriesWithFilterAsKey.put('W', QUERY_TO_FETCH_REVENUE_WEEKLY);
        queriesWithFilterAsKey.put('M', QUERY_TO_FETCH_REVENUE_MONTHLY);
        queriesWithFilterAsKey.put('Y', QUERY_TO_FETCH_REVENUE_YEARLY);

        return queriesWithFilterAsKey.get(filter);
    }

    private Supplier<NoContentFoundException> DOCTOR_REVENUE_NOT_FOUND = () ->
            new NoContentFoundException(AppointmentTransactionDetail.class);
}
