package com.cogent.cogentappointment.constants;

public class SwaggerConstants {
    public static String BASE_PACKAGE = "com.cogent.cogentappointment.resource";

    public static String PATH_REGEX = "/api.*";

    //A
    public interface AppointmentConstant {
        String BASE_API_VALUE = "This is Appointment Controller";
        String CHECK_APPOINTMENT_AVAILABILITY = "Check available time schedules in requested date" +
                " for specific doctor and specialization";
        String SAVE_OPERATION = "Save new appointment";
        String UPDATE_OPERATION = "Update existing appointment";
        String DELETE_OPERATION = "Set appointment status as 'D' when deleted";
        String SEARCH_OPERATION = "Search appointment according to given request parameters";
        String DETAILS_OPERATION = "Fetch appointment details by its id";
        String RESCHEDULE_OPERATION = "Reschedule appointment date and time.";
    }

    //B

    //C
    public interface CountryConstant {
        String BASE_API_VALUE = "This is Country Controller.";
        String FETCH_ACTIVE_COUNTRY = "Fetch active country for dropdown.";
    }


    //D
    public interface DepartmentConstant {
        String BASE_DEPARTMENT_API_VALUE = "This is Department Controller";
        String SAVE_DEPARTMENT_OPERATION = "Save new department";
        String UPDATE_DEPARTMENT_OPERATION = "Update existing department";
        String DELETE_DEPARTMENT_OPERATION = "Set department status as 'D' when deleted";
        String SEARCH_DEPARTMENT_OPERATION = "Search department according to given request parameters";
        String DEPARTMENT_DETAILS_OPERATION = "Fetch department details";
        String FETCH_DEPARTMENT_FOR_DROP_DOWN_OPERATION = "Fetch minimal department details (id and name) for dropdown";
        String FETCH_ACTIVE_DEPARTMENT_FOR_DROP_DOWN_OPERATION = "Fetch minimal active department details (id and name)" +
                " for dropdown";
    }

    public interface DoctorConstant {
        String BASE_API_VALUE = "This is Doctor Controller";
        String SAVE_OPERATION = "Save new Doctor like Dr.Sanjeeev Upreti, Dr. Daniel Shrestha, etc";
        String UPDATE_OPERATION = "Update existing Doctor";
        String DELETE_OPERATION = "Set Doctor status as 'D' when deleted";
        String SEARCH_OPERATION = "Search Doctor according to given request parameters";
        String DETAILS_OPERATION = "Fetch Doctor details by its id";
        String DETAILS_FOR_UPDATE_MODAL_OPERATION = "Fetch Doctor details for update modal by its id";
        String FETCH_DETAILS_FOR_DROPDOWN = "Fetch Doctor details (id and name) for dropdown";
    }

    public interface DoctorDutyRosterConstant {
        String BASE_API_VALUE = "This is Doctor Duty Roster Controller";
        String SAVE_OPERATION = "Save Doctor Duty Roster";
        String UPDATE_OPERATION = "Update Doctor Duty Roster. Note that week days time can be updated " +
                "only if there are no appointments within the selected date range";
        String DELETE_OPERATION = "Set Doctor Duty Roster status as 'D' when deleted";
        String SEARCH_OPERATION = "Search Doctor Duty Roster according to given request parameters";
        String DETAILS_OPERATION = "Fetch Doctor Duty Roster details by its id";
        String FETCH_DOCTOR_DUTY_ROSTER_STATUS_OPERATION = "Fetch doctor duty roster status (used in Appointment status)";
        String UPDATE_DOCTOR_DUTY_ROSTER_OVERRIDE_OPERATION = "Update Doctor Duty Roster Override schedules";
    }


    //E


    //F


    //G

    //H
    public interface HospitalConstant {
        String BASE_API_VALUE = "This is Hospital setup Controller";
        String SAVE_OPERATION = "Save new hospital";
        String UPDATE_OPERATION = "Update existing hospital";
        String DELETE_OPERATION = "Set hospital status as 'D' when deleted with remarks";
        String SEARCH_OPERATION = "Search hospital according to given request parameters";
        String DETAILS_OPERATION = "Fetch hospital details by id";
        String FETCH_DETAILS_FOR_DROPDOWN = "Fetch minimal hospital details (id and name) for dropdown";
    }

    //I

    //J
    //K
    //L
    //M

    //N


    //O

    //P
    public interface PatientConstant {
        String BASE_PATIENT_API_VALUE = "This is Patient Controller";
        String SEARCH_PATIENT_WITH_SELF_TYPE_OPERATION = "Search patient info according to given " +
                "request parameters (esewa id, isSelf='Y' and hospital id)";
        String SEARCH_PATIENT_WITH_OTHERS_TYPE_OPERATION = "Fetch list of minimal patient info according to given " +
                "request parameters (esewa id, isSelf='N' and hospital id)";
        String FETCH_DETAILS_BY_ID = " Fetch patient(with type: OTHERS) details by id";
    }

    public interface ProfileConstant {
        String BASE_API_VALUE = "This is Profile Controller";
        String SAVE_OPERATION = "Save new profile";
        String UPDATE_OPERATION = "Update existing profile";
        String DELETE_OPERATION = "Set profile status as 'D' when deleted";
        String SEARCH_OPERATION = "Search profile according to given request parameters";
        String DETAILS_OPERATION = "Fetch profile details by its id";
        String FETCH_DETAILS_FOR_DROPDOWN = "Fetch minimal profile details (id and name) for dropdown";
        String FETCH_PROFILE_BY_DEPARTMENT_ID = "Fetch active profiles by department id";
    }


    //Q
    public interface QualificationConstant {
        String BASE_API_VALUE = "This is Qualification Controller";
        String SAVE_OPERATION = "Save new Qualification";
        String UPDATE_OPERATION = "Update existing Qualification";
        String DELETE_OPERATION = "Set Qualification status as 'D' when deleted";
        String SEARCH_OPERATION = "Search Qualification according to given request parameters";
        String DETAILS_OPERATION = "Fetch Qualification details by its id";
        String FETCH_DETAILS_FOR_DROPDOWN = "Fetch minimal Qualification details for dropdown";
    }

    public interface QualificationAliasConstant {
        String BASE_API_VALUE = "This is Qualification Alias Controller";
        String FETCH_ACTIVE_QUALIFICATION_ALIAS = "Fetch active Qualification Alias like M.D.,M.B.B.S, etc";
    }

    //R

    //S

    public interface SpecializationConstant {
        String BASE_API_VALUE = "This is Specialization Controller";
        String SAVE_OPERATION = "Save new Specialization like Physician, Surgeon, etc. Generates random 3-digit code.";
        String UPDATE_OPERATION = "Update existing Specialization";
        String DELETE_OPERATION = "Set Specialization status as 'D' when deleted";
        String SEARCH_OPERATION = "Search Specialization according to given request parameters";
        String DETAILS_OPERATION = "Fetch Specialization details by its id";
        String FETCH_DETAILS_FOR_DROPDOWN = "Fetch minimal Specialization details (id and name) for dropdown";
        String FETCH_BY_DOCTOR_ID = "Fetch active specializations by doctor id";
    }


    //T


    //U

    //V

    //W
    public interface WeekDaysConstant {
        String BASE_API_VALUE = "This is Week Days Controller.";
        String FETCH_ACTIVE_WEEK_DAYS = "Fetch active week days.";
    }


    //X
    //Y
    //Z
}
