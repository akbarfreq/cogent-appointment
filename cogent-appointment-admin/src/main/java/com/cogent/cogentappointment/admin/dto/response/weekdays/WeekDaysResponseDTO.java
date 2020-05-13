package com.cogent.cogentappointment.admin.dto.response.weekdays;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rupak
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeekDaysResponseDTO {

    private Long weekDaysId;

    private String weekDaysName;

    private String startTime="";

    private String endTime="";

    private Character dayOffStatus='N';

    private String errorMessage="";

    private List<String> breakDetail = new ArrayList<>();

}
