package com.nutri.rest.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExperienceDetailsRequestAndResponse {
    private Long fromYear;
    private Long toYear;
    private String organization;
}
