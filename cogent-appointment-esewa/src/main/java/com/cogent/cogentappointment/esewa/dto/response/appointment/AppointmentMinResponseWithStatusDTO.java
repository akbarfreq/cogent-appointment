package com.cogent.cogentappointment.esewa.dto.response.appointment;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentMinResponseWithStatusDTO implements Serializable{
    private List<AppointmentMinResponseDTO> appointmentInfo;

    private int responseCode;

    private HttpStatus responseStatus;
}
