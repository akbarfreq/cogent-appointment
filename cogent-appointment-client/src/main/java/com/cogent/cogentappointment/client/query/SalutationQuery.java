package com.cogent.cogentappointment.client.query;

public class SalutationQuery {

    public static final String QUERY_TO_FETCH_ACTIVE_SALUTATION_FOR_DROPDOWN =
            " SELECT" +
                    " sa.id as value," +
                    " sa.code as label" +
                    " FROM" +
                    " Salutation sa " +
                    " WHERE sa.status='Y'";

    public static String QUERY_TO_VALIDATE_SALUTATION_COUNT(String ids) {

        return " SELECT " +
                " sal " +
                " FROM Salutation sal" +
                " WHERE sal.status ='Y'" +
                " AND sal.id IN (" + ids + ")";

    }
}
