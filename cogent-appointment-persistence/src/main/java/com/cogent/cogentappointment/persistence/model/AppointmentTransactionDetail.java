package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.AppointmentTransactionDetailEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti ON 04/02/2020
 *
 * ALL THE TRANSACTION INFO ARE SAVED HERE.
 * taxAmount, discountAmount, serviceChargeAmount IS ZERO AND NOT CONSIDERED AS OF NOW
 *
 * RELATION BETWEEN APPOINTMENT + TRANSACTION DETAILS + TRANSACTION STATUS
 *
 */
@Entity
@Table(name = "appointment_transaction_detail")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AppointmentTransactionDetailEntityListener.class)
public class AppointmentTransactionDetail extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    //TODO : REMOVE UNNECESSARY PARAMETERS FROM HERE SINCE IT IS SAVED NOW IN AppointmentTransactionRequestLog
    @Temporal(TemporalType.DATE)
    @Column(name = "transaction_date")
    private Date transactionDate;

    @Column(name = "transaction_number")
    private String transactionNumber;

    @Column(name = "appointment_amount")
    private Double appointmentAmount;

    @Column(name = "tax_amount")
    private Double taxAmount;

    @Column(name = "discount_amount")
    private Double discountAmount;

    @Column(name = "service_charge_amount")
    private Double serviceChargeAmount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_transaction_request_log_id")
    private AppointmentTransactionRequestLog appointmentTransactionRequestLog;

    @Override
    public String toString() {
        return "AppointmentTransactionDetail{" +
                "id=" + id +
                ", appointment=" + appointment.getAppointmentNumber() +
                ", transactionDate=" + transactionDate +
                ", transactionNumber='" + transactionNumber + '\'' +
                ", appointmentAmount=" + appointmentAmount +
                ", taxAmount=" + taxAmount +
                ", discountAmount=" + discountAmount +
                ", serviceChargeAmount=" + serviceChargeAmount +
                '}';
    }
}
