package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.persistence.model.*;

import java.util.Date;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.ACTIVE;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.INACTIVE;

/**
 * @author smriti on 18/11/2019
 */
public class AppointmentFollowUpTrackerUtils {

    public static AppointmentFollowUpTracker parseToAppointmentFollowUpTracker(Long parentAppointmentId,
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
        followUpTracker.setRemainingNumberOfFollowUps(remainingFollowUpCount);
        followUpTracker.setAppointmentApprovedDate(new Date());
        followUpTracker.setStatus(ACTIVE);
        return followUpTracker;
    }

    public static void updateNumberOfFollowUps(AppointmentFollowUpTracker followUpTracker) {
        followUpTracker.setRemainingNumberOfFollowUps(followUpTracker.getRemainingNumberOfFollowUps() - 1);

        if (followUpTracker.getRemainingNumberOfFollowUps() <= 0)
            followUpTracker.setStatus(INACTIVE);
    }
}
