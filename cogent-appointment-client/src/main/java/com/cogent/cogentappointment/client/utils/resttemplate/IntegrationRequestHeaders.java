package com.cogent.cogentappointment.client.utils.resttemplate;

import com.cogent.cogentappointment.client.security.dto.HmacRequestForEsewaDTO;
import com.cogent.cogentappointment.client.security.hmac.HMACUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Arrays;

import static com.cogent.cogentappointment.client.security.hmac.HMACUtils.hmacForEsewa;

/**
 * @author rupak on 2020-05-24
 */
public class IntegrationRequestHeaders {

    private HMACUtils hmacUtils;

    public static HttpHeaders getBheriAPIHeaders() {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("token", "DE26851D-AF4D-4CE7-9250-CCC2C9A728C9");
        headers.add("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

        return headers;

    }

    public static HttpHeaders getEsewaAPIHeaders() {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("esewa_id", "9841409090");
        headers.add("password", "dGVzdEAxMjM0");
        headers.add("device_unique_id", "b91bb1c3-43ac-4f97-846b-42adcf6fad11");

        return headers;
    }

    public static HttpHeaders getEsewaPaymentStatusAPIHeaders() {

        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        headers.add("signature",
                "ac497c6d7ee552429e05468dd0127d27c48b1cb81c00c5c7d1d0fd577fb3497f6e0e4f361c057051a5dc6b6dd1eaadd4c858a4058334f315c0cb8240671cef74");


        return headers;
    }

    public static HttpHeaders getEsewaHeader(String hash) {

        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        headers.add("signature", hash);


        return headers;
    }
}
