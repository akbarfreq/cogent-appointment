package com.cogent.cogentappointment.esewa.dto.response.appointment;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * @author smriti on 31/03/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentTransactionStatusResponseDTO implements Serializable {

    private Character transactionStatus;

    private int responseCode;

    private HttpStatus responseStatus;
}


