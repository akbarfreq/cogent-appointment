package com.cogent.cogentappointment.admin.dto.request.doctorDutyRoster;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author smriti ON 08/02/2020
 */
@Getter
@Setter
public class DoctorDutyRosterOverrideDeleteRequestDTO implements Serializable {

    @NotNull
    private Long doctorDutyRosterOverrideId;

    @NotEmpty
    private String remarks;

    @NotNull
    private Character status;
}
