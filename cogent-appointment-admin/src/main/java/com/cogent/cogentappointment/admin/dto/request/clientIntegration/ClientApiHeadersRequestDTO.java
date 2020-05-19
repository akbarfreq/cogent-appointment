package com.cogent.cogentappointment.admin.dto.request.clientIntegration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author rupak on 2020-05-19
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientApiHeadersRequestDTO implements Serializable {

    private String key;

    private String value;

    private String description;
}
