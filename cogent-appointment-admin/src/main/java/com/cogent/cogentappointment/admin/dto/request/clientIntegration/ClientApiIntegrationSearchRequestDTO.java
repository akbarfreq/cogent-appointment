package com.cogent.cogentappointment.admin.dto.request.clientIntegration;

import lombok.*;

import java.io.Serializable;

/**
 * @author rupak on 2020-05-25
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientApiIntegrationSearchRequestDTO implements Serializable {

    private Long requestMethodId;

    private Long hospitalId;

    private Long featureTypeId;

    private String url;


}
