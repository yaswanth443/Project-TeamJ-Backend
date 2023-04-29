package com.nutri.rest.utils;

import com.nutri.rest.response.ItemDetailsResponse;
import lombok.*;

@Data
@Builder
public class DietitianEducationDetails {
    private ItemDetailsResponse.LookupUnits qualifiedDegree;
    private String degreeUniversity;
    private Long degreeYear;
}
