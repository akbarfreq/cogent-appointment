package com.cogent.cogentappointment.client.dto.response.patient;

import com.cogent.cogentappointment.persistence.enums.Gender;
import lombok.*;

import java.io.Serializable;

/**
 * @author smriti ON 18/01/2020
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientSearchResponseDTO implements Serializable {

    private Long id;

    private String name;

    private Gender gender;

    private String address;

    private String email;

    private String registrationNumber;

    private String mobileNumber;

    private String eSewaId;

    private String age;

    private Character status;

    private String hospitalNumber;

    private Integer totalItems;
}
