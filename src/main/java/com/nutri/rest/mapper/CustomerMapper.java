package com.nutri.rest.mapper;

import com.nutri.rest.model.Subscription;
import com.nutri.rest.model.User;
import com.nutri.rest.response.CustomerListResponse;
import com.nutri.rest.response.ItemDetailsResponse;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.sql.Blob;

import static com.nutri.rest.utils.AppUtils.*;

@UtilityClass
@Slf4j
public class CustomerMapper {
    public CustomerListResponse mapFromUserDomainToResponse(User user, Subscription subscription){
        ItemDetailsResponse.LookupUnits lookupUnits = ItemDetailsResponse.LookupUnits.builder()
                .unitLookupCode(subscription!=null?subscription.getStatus().getLookupValueCode():"")
                .unitLookupValue(subscription!=null?subscription.getStatus().getLookupValue():"")
                .build();
        ItemDetailsResponse.LookupUnits preferredMealOption = ItemDetailsResponse.LookupUnits.builder()
                .unitLookupCode(subscription!=null?subscription.getPreferredMealOption().getLookupValueCode():"")
                .unitLookupValue(subscription!=null?subscription.getPreferredMealOption().getLookupValue():"")
                .build();

        return CustomerListResponse
                .builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userName(user.getUserName())
                .phoneNumber(user.getPhoneNumber())
                .status(lookupUnits)
                .customerInput(subscription!=null?subscription.getCustomerInput():"")
                .dietitianInput(subscription!=null?subscription.getDietitianInput():"")
                .preferredMealOption(preferredMealOption)
                .allergens(subscription!=null?subscription.getAllergens():"")
                .build();
    }

    public CustomerListResponse mapCustomerDetails(User user){
        return CustomerListResponse
                .builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userName(user.getUserName())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    public CustomerListResponse mapCustomerDetailsFromObjArray(Object[] user){
        ItemDetailsResponse.LookupUnits lookupUnits = ItemDetailsResponse.LookupUnits.builder()
                .unitLookupCode(castObjectToString(user[4]))
                .unitLookupValue(castObjectToString(user[5]))
                .build();

        ItemDetailsResponse.LookupUnits preferredMeal = ItemDetailsResponse.LookupUnits.builder()
                .unitLookupCode(castObjectToString(user[9]))
                .unitLookupValue(castObjectToString(user[10]))
                .build();
        String userProfileImage = null;
        try {
            userProfileImage = user[12]!=null ? new String(((Blob) user[12]).getBytes(1, (int) ((Blob) user[12]).length())) : null;
        }catch (Exception e){
            log.error("Error occurred while converting image to string: "+e.getMessage());
        }

        return CustomerListResponse
                .builder()
                .firstName(castObjectToString(user[0]))
                .lastName(castObjectToString(user[1]))
                .userName(castObjectToString(user[2]))
                .phoneNumber(castObjectToString(user[3]))
                .status(lookupUnits)
                .customerInput(castObjectToString(user[6]))
                .dietitianInput(castObjectToString(user[7]))
                .subscriptionAmount(castObjectToBigDecimal(user[8]))
                .preferredMealOption(preferredMeal)
                .allergens(castObjectToString(user[11]))
                .userProfileImage(userProfileImage)
                .sex(castObjectToString(user[13]))
                .sleep(castObjectToString(user[14]))
                .quesres(castObjectToString(user[15]))
                .nutrition(castObjectToString(user[16]))
                .phyActivity(castObjectToString(user[17]))
                .hydration(castObjectToString(user[18]))
                .build();
    }


}
