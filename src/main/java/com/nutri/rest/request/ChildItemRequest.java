package com.nutri.rest.request;

import com.nutri.rest.response.ItemDetailsResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChildItemRequest {

    private String parentItemName;
    private String itemName;
    private String itemImage;

}
