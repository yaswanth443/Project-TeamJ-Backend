package com.nutri.rest.repository;

import com.nutri.rest.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface DietitianMenuItemRepository extends JpaRepository<DietitianMenus, Long> {

    List<DietitianMenus> findByMenuNameAndDietitianId(String menuName, User dietitian);
    List<DietitianMenus> findByDietitianId(User dietitian);

    @Query(value = "SELECT DISTINCT MENU_NAME, to_char(cast(CREATED_DATE as date),'DD-MM-YYYY') FROM DIETITIAN_MENUS WHERE DIETITIAN_ID=?",nativeQuery = true)
    List<Object[]> findDistinctMenus(Long dietitianId);
}