package com.cogent.cogentappointment.client.dto.request.room;

import com.cogent.cogentappointment.client.constraintvalidator.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author smriti on 2019-09-25
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomRequestDTO implements Serializable {

    @NotNull
    private Integer roomNumber;

    @NotNull
    @Status
    private Character status;

}
