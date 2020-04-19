package com.cogent.cogentappointment.admin.dto.response.appointmentMode;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti on 11/11/2019
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentModeResponseDTO implements Serializable {

    private String name;

    private String code;

    private Character status;

    private Character isEditable;

    private String remarks;

    private String description;

    private String createdBy;

    private String modifiedBy;

    private String createdDate;

    private String modifiedDate;

}
