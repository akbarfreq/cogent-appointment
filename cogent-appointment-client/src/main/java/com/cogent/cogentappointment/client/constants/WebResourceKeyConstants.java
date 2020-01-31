package com.cogent.cogentappointment.client.constants;

/**
 * @author smriti on 7/5/19
 */
public class WebResourceKeyConstants {

    //A
    public static final String API_V1 = "/api/v1";
    public static final String ACTIVE = "/active";

    public interface AdminConstants {
        String BASE_ADMIN = "/admin";
        String AVATAR = "/avatar";
        String ADMIN_META_INFO = "/metaInfo";
        String ASSIGNED_SUB_DEPARTMENTS = "/assignedSubDepartments";
        String CHANGE_PASSWORD = "/changePassword";
        String RESET_PASSWORD = "/resetPassword";
        String INFO = "/info";
        String VERIFY = "/verify";
    }

    public interface AppointmentConstants {
        String BASE_APPOINTMENT = "/appointment";
        String CHECK_AVAILABILITY = "/checkAvailability";
        String RESCHEDULE = "/reschedule";
    }

    //B
    public static final String BASE_PASSWORD = "/password";

    //C
    public interface CountryConstants {
        String BASE_COUNTRY = "/country";
    }


    //D
    public static final String DETAIL = "/detail";

    public interface DepartmentConstants {
        String BASE_DEPARTMENT = "/department";
        String DEPARTMENT_ID_PATH_VARIABLE_BASE = "/{departmentId}";
    }

    public interface DoctorConstants {
        String BASE_DOCTOR = "/doctor";
        String DOCTOR_WISE = "/doctor-wise";
        String UPDATE_DETAILS = "/updateDetails";
        String DOCTOR_ID_PATH_VARIABLE_BASE = "/{doctorId}";
    }

    public interface DoctorDutyRosterConstants {
        String BASE_DOCTOR_DUTY_ROSTER = "/doctorDutyRoster";
        String DOCTOR_DUTY_ROSTER_OVERRIDE = "/doctorDutyRosterOverride";
        String DOCTOR_DUTY_ROSTER_STATUS = "/doctorDutyRosterStatus";
    }

    //E

    //F
    public static final String FILE = "/files/{subDirectoryLocation}/{filename:.+}";

    public interface ForgotPasswordConstants {
        String VERIFY = "/verify";
        String FORGOT = "/forgot";
    }


    //G

    //H
    public interface HospitalConstants {
        String BASE_HOSPITAL = "/hospital";
        String HOSPITAL_WISE = "/hospital-wise";
        String HOSPITAL_ID_PATH_VARIABLE_BASE = "/{hospitalId}";
    }

    //I
    public static final String ID_PATH_VARIABLE_BASE = "/{id}";


    //J

    //K

    //L
    public static final String LOGIN = "/login";

    //M
    public static final String MIN = "/min";

    //N


    //O

    //P
    public interface PatientConstant {
        String BASE_PATIENT = "/patient";
        String SELF = "/self";
        String OTHERS = "/others";
    }

    public interface ProfileSetupConstants {
        String BASE_PROFILE = "/profile";
    }

    //Q
    public interface QualificationConstants {
        String BASE_QUALIFICATION = "/qualification";
    }

    public interface QualificationAliasConstants {
        String BASE_QUALIFICATION_ALIAS = "/qualificationAlias";
    }

    //R


    //S
    public static final String SAVE = "/save";
    public static final String SEARCH = "/search";

    public interface SidebarConstants {
        String BASE_SIDE_BAR = "/sidebar";
    }


    public interface SpecializationConstants {
        String BASE_SPECIALIZATION = "/specialization";
        String SPECIALIZATION_WISE = "/specialization-wise";
        String SPECIALIZATION_ID_PATH_VARIABLE_BASE = "/{specializationId}";
    }


    //T

    //U
    public static final String USERNAME_VARIABLE_BASE = "/{username}";


    //V

    //W
    public interface WeekDaysConstants {
        String BASE_WEEK_DAYS = "/weekDays";
    }


    //X

    //Y

    //Z
}
