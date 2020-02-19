package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentFollowUpResponseDTO;
import com.cogent.cogentappointment.persistence.model.*;

import java.util.Date;

import static com.cogent.cogentappointment.client.constants.StatusConstants.ACTIVE;
import static com.cogent.cogentappointment.client.constants.StatusConstants.INACTIVE;

/**
 * @author smriti on 16/02/20
 */
public class AppointmentFollowUpTrackerUtils {

    public static AppointmentFollowUpResponseDTO parseToAppointmentFollowUpResponseDTO(Character isFollowUp,
                                                                                       Double followUpAppointmentCharge,
                                                                                       Long parentAppointmentId) {

        return AppointmentFollowUpResponseDTO.builder()
                .isFreeFollowUp(isFollowUp)
                .followUpAppointmentCharge(followUpAppointmentCharge)
                .parentAppointmentId(parentAppointmentId)
                .build();

    }

    public static void updateNumberOfFreeFollowUps(AppointmentFollowUpTracker followUpTracker) {
        followUpTracker.setRemainingNumberOfFollowUps(followUpTracker.getRemainingNumberOfFollowUps() - 1);

        if (followUpTracker.getRemainingNumberOfFollowUps() <= 0)
            followUpTracker.setStatus(INACTIVE);
    }

    public static AppointmentFollowUpTracker parseToAppointmentFollowUpTracker(Long parentAppointmentId,
                                                                               String parentAppointmentNumber,
                                                                               Integer remainingFollowUpCount,
                                                                               Doctor doctor,
                                                                               Specialization specialization,
                                                                               Patient patient,
                                                                               Hospital hospital) {

        AppointmentFollowUpTracker followUpTracker = new AppointmentFollowUpTracker();
        followUpTracker.setDoctorId(doctor);
        followUpTracker.setPatientId(patient);
        followUpTracker.setSpecializationId(specialization);
        followUpTracker.setHospitalId(hospital);
        followUpTracker.setParentAppointmentId(parentAppointmentId);
        followUpTracker.setParentAppointmentNumber(parentAppointmentNumber);
        followUpTracker.setRemainingNumberOfFollowUps(remainingFollowUpCount);
        followUpTracker.setAppointmentApprovedDate(new Date());
        followUpTracker.setStatus(ACTIVE);
        return followUpTracker;
    }
}
