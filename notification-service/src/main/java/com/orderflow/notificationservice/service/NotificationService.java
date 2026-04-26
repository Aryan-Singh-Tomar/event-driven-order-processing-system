package com.orderflow.notificationservice.service;

import com.orderflow.notificationservice.event.OrderCreatedEvent;

public interface NotificationService {

    void processOrderCreatedEvent(OrderCreatedEvent event);

    void handleFailedEvent(OrderCreatedEvent event, String topic);
}
