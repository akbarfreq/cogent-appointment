package com.cogent.cogentappointment.admin.dto.response.appointment.appointmentPendingApproval;


import com.cogent.cogentappointment.persistence.enums.Gender;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Rupak
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentPendingApprovalDTO implements Serializable {

    private Long appointmentId;

    private Date appointmentDate;

    private String appointmentNumber;

    private String appointmentTime;

    private String registrationNumber;

    private String patientName;

    private String mobileNumber;

    private String doctorName;

    private String specializationName;

}
