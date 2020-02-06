package com.cogent.cogentappointment.admin.dto.request.hospital;

import com.cogent.cogentappointment.admin.constraintvalidator.SpecialCharacters;
import com.cogent.cogentappointment.admin.constraintvalidator.Status;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author smriti ON 12/01/2020
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalRequestDTO implements Serializable {

    @NotNull
    @NotEmpty
    @SpecialCharacters
    private String name;

    private String hospitalCode;

    @NotNull
    @NotEmpty
    @SpecialCharacters
    private String address;

    @NotNull
    @NotEmpty
    @SpecialCharacters
    private String panNumber;

    @NotNull
    @Status
    private Character status;

    @NotNull
    @Status
    private Character isCogentAdmin;

    @NotNull
    private Double refundPercentage;

    @NotEmpty
    private List<String> contactNumber;
}

