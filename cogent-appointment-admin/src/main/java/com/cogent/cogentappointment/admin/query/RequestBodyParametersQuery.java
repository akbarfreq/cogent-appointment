package com.cogent.cogentappointment.admin.query;

/**
 * @author rupak ON 2020/05/29-11:00 AM
 */
public class RequestBodyParametersQuery {
    public static final String QUERY_TO_FETCH_MIN_REQUEST_BODY_PARAMTERS =
            "SELECT" +
                    " airbp.id as value," +
                    " airbp.name as label" +
                    " FROM" +
                    " ApiIntegrationRequestBodyParameters airbp" +
                    " WHERE airbp.status ='Y'" +
                    " ORDER by airbp.name ASC";
}
