package com.cogent.cogentappointment.client.dto.response.eSewa;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author smriti on 15/03/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AvailableDatesWithSpecializationResponseDTO implements Serializable {
    private List<AvailableDatesWithSpecialization> availableDatesWithSpecialization;

    private int status;
}
