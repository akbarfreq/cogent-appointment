package com.cogent.cogentappointment.admin.dto.request.patient;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author smriti ON 18/01/2020
 */
@Getter
@Setter
public class PatientSearchRequestDTO implements Serializable {

    private String esewaId;

    private Long patientMetaInfo;

    private Character status;

    private Long hospitalId;
}
