package org.yearup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yearup.models.OrderItem;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    List<OrderItem> getOrderItemByOrderItemId(int orderItemId);

    List<OrderItem> findOrderItemByOrderItemId(int orderItemId);
}
