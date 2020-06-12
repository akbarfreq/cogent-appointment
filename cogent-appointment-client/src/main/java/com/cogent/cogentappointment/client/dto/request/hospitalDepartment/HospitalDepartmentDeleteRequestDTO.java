package com.cogent.cogentappointment.client.dto.request.hospitalDepartment;

import com.cogent.cogentappointment.client.constraintvalidator.DeleteStatus;
import com.cogent.cogentappointment.client.constraintvalidator.Status;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author smriti ON 24/01/2020
 */
@Getter
@Setter
public class HospitalDepartmentDeleteRequestDTO implements Serializable {

    @NotNull
    private Long hospitalDepartmentId;

    @NotNull
    private Long id;

    @NotNull
    @NotEmpty
    @NotBlank
    private String remarks;

    @NotNull
    @DeleteStatus
    private Character status;
}
