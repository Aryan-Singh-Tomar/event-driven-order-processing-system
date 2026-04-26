package com.orderflow.notificationservice.service.impl;

import com.orderflow.notificationservice.entity.FailedEvent;
import com.orderflow.notificationservice.entity.NotificationRecord;
import com.orderflow.notificationservice.event.OrderCreatedEvent;
import com.orderflow.notificationservice.repository.FailedEventRepository;
import com.orderflow.notificationservice.repository.NotificationRepository;
import com.orderflow.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final FailedEventRepository failedEventRepository;

    @Override
    @Transactional
    public void processOrderCreatedEvent(OrderCreatedEvent event) {
        if (notificationRepository.existsByOrderId(event.getOrderId())) {
            log.warn("Duplicate notification event — skipping | orderId={}",
                    event.getOrderId());
            return;
        }

        String message = buildNotificationMessage(event);


        log.info("Sending ORDER CONFIRMATION notification");
        log.info("To      : {}", event.getCustomerName());
        log.info("Subject : Order #{} Confirmed!", event.getOrderId());
        log.info("Message : {}", message);


        NotificationRecord record = NotificationRecord.builder()
                .orderId(event.getOrderId())
                .customerName(event.getCustomerName())
                .productName(event.getProductName())
                .message(message)
                .build();

        notificationRepository.save(record);

        log.info("Notification sent and recorded | orderId={}",
                event.getOrderId());
    }

    @Override
    @Transactional
    public void handleFailedEvent(OrderCreatedEvent event, String topic) {
        log.error("Saving failed notification to DB | orderId={} | topic={}",
                event.getOrderId(), topic);

        FailedEvent failedEvent = FailedEvent.builder()
                .orderId(event.getOrderId())
                .topic(topic)
                .reason("Notification exhausted all retry attempts")
                .build();

        failedEventRepository.save(failedEvent);
    }

    private String buildNotificationMessage(OrderCreatedEvent event) {
        return String.format(
                "Dear %s, your order for %d x %s worth Rs.%.2f " +
                        "has been confirmed! Order ID: #%d",
                event.getCustomerName(),
                event.getQuantity(),
                event.getProductName(),
                event.getPrice(),
                event.getOrderId()
        );
    }
}
