package com.nutri.rest.repository;

import com.nutri.rest.model.Order;
import com.nutri.rest.model.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItems, Long> {
    List<OrderItems> findByOrderId(Order order);
}
