package com.orderflow.inventoryservice.repository;

import com.orderflow.inventoryservice.entity.ProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessedEventRepository extends JpaRepository<ProcessedEvent, Long> {
    boolean existsByOrderId(Long orderId);

}
