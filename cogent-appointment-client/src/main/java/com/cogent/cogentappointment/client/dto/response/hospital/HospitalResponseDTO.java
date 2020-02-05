package com.cogent.cogentappointment.client.dto.response.hospital;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HospitalResponseDTO implements Serializable {

    private Long id;

    private String name;

    private String hospitalCode;

    private Character status;

    private String address;

    private String panNumber;

    private String hospitalLogo;

    private String hospitalBanner;

    private String remarks;

    private List<HospitalContactNumberResponseDTO> contactNumberResponseDTOS;
}
