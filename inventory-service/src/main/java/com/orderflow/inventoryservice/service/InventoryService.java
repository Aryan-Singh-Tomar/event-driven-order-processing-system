package com.orderflow.inventoryservice.service;

import com.orderflow.inventoryservice.event.OrderCreatedEvent;

public interface InventoryService {

    void processOrderCreatedEvent(OrderCreatedEvent event);

    void handleFailedEvent(OrderCreatedEvent event, String topic);
}
