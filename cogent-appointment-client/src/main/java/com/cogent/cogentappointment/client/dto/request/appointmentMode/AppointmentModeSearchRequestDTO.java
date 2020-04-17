package com.cogent.cogentappointment.client.dto.request.appointmentMode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Sauravi Thapa ON 4/17/2019
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentModeSearchRequestDTO implements Serializable {

    private Long appointmentMode;

    private String code;

    private Character status;
}
