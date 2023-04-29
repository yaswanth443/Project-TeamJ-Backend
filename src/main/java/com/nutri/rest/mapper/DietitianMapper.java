package com.nutri.rest.mapper;

import com.nutri.rest.model.Subscription;
import com.nutri.rest.model.User;
import com.nutri.rest.response.DietitianListResponse;
import com.nutri.rest.response.ItemDetailsResponse;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.sql.Blob;

import static com.nutri.rest.utils.AppUtils.castObjectToBigDecimal;
import static com.nutri.rest.utils.AppUtils.castObjectToString;

@UtilityClass
@Slf4j
public class DietitianMapper {
    public DietitianListResponse mapFromUserDomainToResponse(User user, Subscription subscription, Double dietitianRating){
        ItemDetailsResponse.LookupUnits lookupUnits = ItemDetailsResponse.LookupUnits.builder()
                .unitLookupCode(subscription!=null?subscription.getStatus().getLookupValueCode():"")
                .unitLookupValue(subscription!=null?subscription.getStatus().getLookupValue():"")
                .build();
        ItemDetailsResponse.LookupUnits preferredMealOption = ItemDetailsResponse.LookupUnits.builder()
                .unitLookupCode(subscription!=null?subscription.getPreferredMealOption()!=null?subscription.getPreferredMealOption().getLookupValueCode():"":"")
                .unitLookupValue(subscription!=null?subscription.getPreferredMealOption()!=null?subscription.getPreferredMealOption().getLookupValue():"":"")
                .build();

        return DietitianListResponse
                .builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userName(user.getUserName())
                .phoneNumber(user.getPhoneNumber())
                .price(user.getBasePrice())
                .subscriptionAmount(subscription!=null?subscription.getAmount():BigDecimal.ZERO)
                .status(lookupUnits)
                .customerInput(subscription!=null?subscription.getCustomerInput():"")
                .dietitianInput(subscription!=null?subscription.getDietitianInput():"")
                .preferredMealOption(preferredMealOption)
                .rating(dietitianRating)
                .allergens(subscription!=null?subscription.getAllergens():"")
                .userProfileImage(user.getProfileImage()!=null ? new String(user.getProfileImage()) : null)
                .build();
    }

    public DietitianListResponse mapDietitianDetailsFromObjArray(Object[] user){
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
        return DietitianListResponse
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
