package com.cogent.cogentappointment.admin.dto.response.clientIntegration;

import lombok.*;

import java.io.Serializable;
import java.util.Map;

/**
 * @author rupak on 2020-05-25
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientApiIntegrationDetailResponseDTO  implements Serializable {

    private String hospitalName;

    private Long featureId;

    private String featureCode;

    private Long requestMethodId;

    private String requestMethodName;

    private String url;

    private Map<String,String> headers;

    private Map<String,String> queryParameters;

}
