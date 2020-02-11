package com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author smriti ON 16/12/2019
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentStatusResponseDTO implements Serializable {

    private LocalDate date;

    private Long doctorId;

    private String doctorName;

    private Long specializationId;

    private String specializationName;

    private String appointmentTimeDetails;
}
