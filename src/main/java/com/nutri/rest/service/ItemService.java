package com.nutri.rest.service;

import com.nutri.rest.exception.ValidationException;
import com.nutri.rest.mapper.ItemMapper;
import com.nutri.rest.model.*;
import com.nutri.rest.repository.*;
import com.nutri.rest.request.ChildItemRequest;
import com.nutri.rest.request.MenuRequest;
import com.nutri.rest.response.ItemDetailsResponse;
import com.nutri.rest.response.MenuNameResponse;
import com.nutri.rest.response.MenuResponse;
import com.nutri.rest.response.RestaurantListResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.nutri.rest.utils.AppUtils.castObjectToString;

@Service
public class ItemService {

    private final UserRepository userRepository;

    private final LookupRepository lookupRepository;

    private final ParentItemRepository parentItemRepository;

    private final ChildItemRepository childItemRepository;

    private final DietitianMenuItemRepository dietitianMenuItemRepository;

    public ItemService(UserRepository userRepository, LookupRepository lookupRepository, ParentItemRepository parentItemRepository, ChildItemRepository childItemRepository, DietitianMenuItemRepository dietitianMenuItemRepository) {
        this.userRepository = userRepository;
        this.lookupRepository = lookupRepository;
        this.parentItemRepository = parentItemRepository;
        this.childItemRepository = childItemRepository;
        this.dietitianMenuItemRepository = dietitianMenuItemRepository;
    }

    public List<ItemDetailsResponse> getAllItems(){
        List<Object[]> items = parentItemRepository.getAllItemsWithUnitCodes();
        if(CollectionUtils.isEmpty(items)){
            return new ArrayList<>();
        }
        return items.stream().map(ItemMapper::mapItemDetailsFromObjArray)
                .map(parentItem -> {
                    parentItem.setChildItems(getChildItemsOfParent(parentItem.getItemName()));
                    return parentItem;
                }).collect(Collectors.toList());
    }

    public List<ItemDetailsResponse> getParentItems(){
        List<Object[]> items = parentItemRepository.getAllItemsWithUnitCodes();
        if(CollectionUtils.isEmpty(items)){
            return new ArrayList<>();
        }
        return items.stream().map(ItemMapper::mapItemDetailsFromObjArray).collect(Collectors.toList());
    }

    public List<ItemDetailsResponse> getChildItemsOfParent(String parentItemName){
        ParentItem item = parentItemRepository.findByItemName(parentItemName);
        List<ChildItem> childItems = childItemRepository.findByParentItem(item);
        if(childItems==null)
            return new ArrayList<>();
        return childItems.stream().map(childItem -> {
                    List<LookupValue> lookupValues = lookupRepository.findByLookupValueType(childItem.getParentItem().getLookupValueTypeOfItemUnit());
                    ItemDetailsResponse itemDetailsResponse = ItemMapper.mapChildItemDetailsToResponse(childItem);
                    itemDetailsResponse.setItemImage(childItem.getItemImage()!=null ?
                            new String(childItem.getItemImage(), StandardCharsets.UTF_8) : null);
                    itemDetailsResponse.setItemUnitsAndCodes(
                            lookupValues.stream().map(lookupValue -> ItemDetailsResponse.LookupUnits.builder()
                            .unitLookupCode(lookupValue.getLookupValueCode())
                                    .unitLookupValue(lookupValue.getLookupValue()).build()).collect(Collectors.toList())
                    );
                    return itemDetailsResponse;
                })
                .collect(Collectors.toList());
    }

    public List<RestaurantListResponse> getRestaurantsByParentItem(String parentItemName){
        List<Object[]> restaurantList = userRepository.getAllRestaurantsByParentItem(parentItemName);
        List<RestaurantListResponse> listResponses = new ArrayList<>();
        if(!CollectionUtils.isEmpty(restaurantList)){
            listResponses = restaurantList.stream().map(restaurant -> RestaurantListResponse.builder()
                    .userName(castObjectToString(restaurant[0]))
                    .restaurantName(castObjectToString(restaurant[1])).build()).collect(Collectors.toList());
        }
        return listResponses;
    }
    public void saveMenu(List<MenuRequest> menuRequestList, String menuName){
        String dietitianUsername = CurrentUserService.getLoggedUserName();
        User dietitian = userRepository.findByUserName(dietitianUsername).get();

        if(CollectionUtils.isEmpty(dietitianMenuItemRepository.findByMenuNameAndDietitianId(menuName, dietitian))) {
            menuRequestList.parallelStream().forEach(menuRequest -> {
                ChildItem childItem = childItemRepository.findByItemName(menuRequest.getChildItemName());
                LookupValue quantityUnit = lookupRepository.findByLookupValueCode(menuRequest.getQuantityUnit().getUnitLookupCode());
                DietitianMenus dietitianMenus = DietitianMenus.builder()
                        .childItem(childItem)
                        .menuName(menuName)
                        .quantity(menuRequest.getQuantity())
                        .dietitianId(dietitian)
                        .quantityUnit(quantityUnit)
                        .instructions(menuRequest.getInstructions()).build();
                dietitianMenuItemRepository.save(dietitianMenus);
            });
        }else {
            throw new ValidationException("Menu with same name already exists, use a different name or modify the menu");
        }
    }
    public void modifyMenu(List<MenuRequest> menuRequestList, String menuName){
        String dietitianUsername = CurrentUserService.getLoggedUserName();
        User dietitian = userRepository.findByUserName(dietitianUsername).get();
        List<DietitianMenus> menus = dietitianMenuItemRepository.findByMenuNameAndDietitianId(menuName, dietitian);

        if(!CollectionUtils.isEmpty(menus)) {
            menus.parallelStream().forEach(menu -> dietitianMenuItemRepository.delete(menu));

            menuRequestList.stream().forEach(menuRequest -> {
                ChildItem childItem = childItemRepository.findByItemName(menuRequest.getChildItemName());
                LookupValue quantityUnit = lookupRepository.findByLookupValueCode(menuRequest.getQuantityUnit().getUnitLookupCode());
                DietitianMenus dietitianMenus = DietitianMenus.builder()
                        .childItem(childItem)
                        .menuName(menuName)
                        .quantity(menuRequest.getQuantity())
                        .dietitianId(dietitian)
                        .quantityUnit(quantityUnit)
                        .instructions(menuRequest.getInstructions())
                        .build();
                dietitianMenuItemRepository.save(dietitianMenus);
            });
        }else {
            throw new ValidationException("Invalid menu");
        }
    }

    public List<MenuNameResponse> getSavedMenus(){
        String dietitianUsername = CurrentUserService.getLoggedUserName();
        User dietitian = userRepository.findByUserName(dietitianUsername).get();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        formatter = formatter.withLocale(Locale.US);  // Locale specifies human language for translating, and cultural norms for lowercase/uppercase and abbreviations and such. Example: Locale.US or Locale.CANADA_FRENCH

        List<Object[]> dietitianMenus = dietitianMenuItemRepository.findDistinctMenus(dietitian.getId());
        if(!CollectionUtils.isEmpty(dietitianMenus)){
            DateTimeFormatter finalFormatter = formatter;
            return dietitianMenus.parallelStream().map(menu -> MenuNameResponse.builder()
                    .menuName(castObjectToString(menu[0]))
                    .createdDate(LocalDate.parse(castObjectToString(menu[1]), finalFormatter))
                    .build()).collect(Collectors.toList());
        }
        return null;
    }

    public List<MenuResponse> getSavedMenuItems(String menuName){
        String dietitianUsername = CurrentUserService.getLoggedUserName();
        User dietitian = userRepository.findByUserName(dietitianUsername).get();

        List<DietitianMenus> dietitianMenus = dietitianMenuItemRepository.findByMenuNameAndDietitianId(menuName, dietitian);
        if(!CollectionUtils.isEmpty(dietitianMenus)){
            return dietitianMenus.parallelStream().map(menu -> MenuResponse.builder()
                    .childItemName(menu.getChildItem().getItemName())
                    .parentItemName(menu.getChildItem().getParentItem().getItemName())
                    .quantity(menu.getQuantity())
                    .instructions(menu.getInstructions())
                    .quantityUnit(ItemDetailsResponse.LookupUnits.builder()
                            .unitLookupCode(menu.getQuantityUnit().getLookupValueCode())
                            .unitLookupValue(menu.getQuantityUnit().getLookupValue()).build()).build()).collect(Collectors.toList());
        }
        return null;
    }

    public void createItem(ChildItemRequest itemRequest){
        ParentItem item = parentItemRepository.findByItemName(itemRequest.getParentItemName());
        ChildItem childItem = childItemRepository.findByItemName(itemRequest.getItemName());
        if(childItem!=null){
            childItem.setItemImage(itemRequest.getItemImage().getBytes());
            childItemRepository.save(childItem);
        }else {
            childItem = ChildItem.builder()
                    .itemName(itemRequest.getItemName())
                    .itemImage(itemRequest.getItemImage().getBytes())
                    .parentItem(item).build();
            childItemRepository.save(childItem);
        }
    }
}
