package com.cogent.cogentappointment.admin.dto.request.dashboard;

import lombok.*;

/**
 * @author Rupak
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorRevenueRequestDTO {

    private Long doctorId;

    private Long hospitalId;

    private Long specializationId;
}
