package com.cogent.cogentappointment.admin.loghandler;

import javax.servlet.http.HttpServletResponse;

public class Checkpoint {

    public static int checkResponseStatus(HttpServletResponse response) {

        int code = response.getStatus();
        System.out.println("Status----------------------" + code);
        return code;
    }
}
