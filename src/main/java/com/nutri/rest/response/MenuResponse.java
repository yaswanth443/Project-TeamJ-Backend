package com.nutri.rest.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuResponse {

    private String parentItemName;

    private String childItemName;
    private Long quantity;

    private ItemDetailsResponse.LookupUnits quantityUnit;
    private String instructions;
}
