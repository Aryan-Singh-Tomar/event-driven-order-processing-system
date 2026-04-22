package com.orderflow.inventoryservice.repository;

import com.orderflow.inventoryservice.entity.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    boolean existsByOrderId(Long orderId);

}
