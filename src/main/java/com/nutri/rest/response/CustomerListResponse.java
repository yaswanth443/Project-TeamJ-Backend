package com.nutri.rest.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerListResponse {

    private String firstName;
    private String lastName;
    private String userName;
    private String phoneNumber;
    private ItemDetailsResponse.LookupUnits status;

    private BigDecimal subscriptionAmount;

    private ItemDetailsResponse.LookupUnits preferredMealOption;
    private String customerInput;
    private String dietitianInput;

    private String allergens;
    private String userProfileImage;
    private String sex;
    private String sleep;
    private String quesres;
    private String nutrition;
    private String phyActivity;
    private String hydration;
}
