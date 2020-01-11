package com.cogent.cogentappointment.dto.request.qualification;

import com.cogent.cogentappointment.constraintvalidator.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author smriti on 11/11/2019
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QualificationRequestDTO implements Serializable {

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    private String university;

    @NotNull
    private Long countryId;

    @NotNull
    private Long qualificationAliasId;

    @NotNull
    @Status
    private Character status;
}
