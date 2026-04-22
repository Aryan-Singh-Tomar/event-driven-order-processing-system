package com.orderflow.inventoryservice.service.impl;

import com.orderflow.inventoryservice.entity.InventoryItem;
import com.orderflow.inventoryservice.entity.ProcessedEvent;
import com.orderflow.inventoryservice.event.OrderCreatedEvent;
import com.orderflow.inventoryservice.repository.InventoryItemRepository;
import com.orderflow.inventoryservice.repository.ProcessedEventRepository;
import com.orderflow.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryItemRepository inventoryItemRepository;
    private final ProcessedEventRepository processedEventRepository;


    @Override
    @Transactional
    public void processOrderCreatedEvent(OrderCreatedEvent event) {

        if(processedEventRepository.existsByOrderId(event.getOrderId())){
            log.warn("Duplicate event received — skipping | orderId={}",
                    event.getOrderId());
            return;
        }

        log.info("Processing inventory for orderId={}", event.getOrderId());

        InventoryItem item = InventoryItem.builder()
                .orderId(event.getOrderId())
                .productName(event.getProductName())
                .quantityReserved(event.getQuantity())
                .build();

        inventoryItemRepository.save(item);

        ProcessedEvent processedEvent = ProcessedEvent.builder()
                .orderId(event.getOrderId())
                .processedAt(LocalDateTime.now())
                .build();

        processedEventRepository.save(processedEvent);

        log.info("Inventory reserved | orderId={} | product={} | qty={}",
                event.getOrderId(),
                event.getProductName(),
                event.getQuantity());

    }
}
