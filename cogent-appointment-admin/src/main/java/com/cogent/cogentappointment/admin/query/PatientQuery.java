package com.cogent.cogentappointment.admin.query;

/**
 * @author smriti ON 16/01/2020
 */
public class PatientQuery {

    public final static String QUERY_TO_VALIDATE_UPDATED_PATIENT_DUPLICITY =
            "SELECT " +
                    " COUNT(p.id)" +
                    " FROM Patient p" +
                    " LEFT JOIN Hospital h ON h.id = p.hospitalId.id" +
                    " WHERE " +
                    " (p.name =:name" +
                    " AND p.mobileNumber =:mobileNumber" +
                    " AND p.dateOfBirth =:dateOfBirth" +
                    " AND p.id !=:id)" +
                    " AND h.id =:hospitalId" +
                    " AND p.status != 'D'";

    public final static String QUERY_TO_VALIDATE_PATIENT_DUPLICITY =
            "SELECT " +
                    " COUNT(p.id)" +
                    " FROM Patient p" +
                    " WHERE " +
                    " (p.name =:name" +
                    " AND p.mobileNumber =:mobileNumber" +
                    " AND p.dateOfBirth =:dateOfBirth)" +
                    " AND p.status != 'D'";

    public static final String SELECT_CLAUSE_TO_FETCH_PATIENT_DETAILS =
            " SELECT p.id as patientId," +                              //[0]
                    " p.name as name," +                                //[1]
                    " p.mobileNumber as mobileNumber," +                //[2]
                    " p.address as address," +                          //[3]
                    " p.email as email," +                              //[4]
                    " p.gender as gender," +                            //[5]
                    " p.dateOfBirth as dateOfBirth," +                  //[6]
                    " p.nepaliDateOfBirth as nepaliDateOfBirth" +       //[7]
                    " FROM Patient p";

    public static final String GET_WHERE_CLAUSE_TO_FETCH_PATIENT_DETAILS =
            " WHERE p.eSewaId=:eSewaId" +
                    " AND p.isSelf=:isSelf" +
                    " AND p.hospitalId.id=:hospitalId" +
                    " AND p.status='Y'";

    public static final String QUERY_TO_FETCH_PATIENT_DETAILS =
            SELECT_CLAUSE_TO_FETCH_PATIENT_DETAILS + GET_WHERE_CLAUSE_TO_FETCH_PATIENT_DETAILS;

    public static final String QUERY_TO_FETCH_MINIMAL_PATIENT =
            " SELECT p.id as patientId," +                              //[0]
                    " p.name as name," +                                //[1]
                    " p.mobileNumber as mobileNumber," +                //[2]
                    " p.gender as gender" +                             //[3]
                    " FROM Patient p" +
                    GET_WHERE_CLAUSE_TO_FETCH_PATIENT_DETAILS;

    public static final String GET_WHERE_CLAUSE_TO_FETCH_PATIENT_DETAILS_BY_ID =
            " WHERE p.id=:id" +
                    " AND p.status='Y'";

    public static final String QUERY_TO_FETCH_PATIENT_DETAILS_BY_ID =
            SELECT_CLAUSE_TO_FETCH_PATIENT_DETAILS + GET_WHERE_CLAUSE_TO_FETCH_PATIENT_DETAILS_BY_ID;

//
//    public static final String QUERY_TO_FETCH_LATEST_PATIENT_HIS_NUMBER =
//            " SELECT " +
//                    " p.his_number" +
//                    " FROM patient p " +
//                    " WHERE p.status !='D'" +
//                    " AND (" +
//                    " SELECT max(p.created_date)" +
//                    " FROM" +
//                    " patient p)" +
//                    " ORDER BY p.id DESC";
//
}
