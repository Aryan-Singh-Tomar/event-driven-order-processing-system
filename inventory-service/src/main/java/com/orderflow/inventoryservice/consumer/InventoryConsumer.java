package com.orderflow.inventoryservice.consumer;

import com.orderflow.inventoryservice.event.OrderCreatedEvent;
import com.orderflow.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryConsumer {

    private final InventoryService inventoryService;


    @KafkaListener(
            topics = "${kafka.topics.order-created}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void handleOrderCreatedEvent(OrderCreatedEvent event){
        log.info("Received OrderCreatedEvent | orderId={}",event.getOrderId());
        inventoryService.processOrderCreatedEvent(event);
    }
}
