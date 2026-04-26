package com.orderflow.notificationservice.consumer;

import com.orderflow.notificationservice.event.OrderCreatedEvent;
import com.orderflow.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationService notificationService;

    @RetryableTopic(
            attempts = "4",
            backoff = @Backoff(
                    delay = 1000,
                    multiplier = 2.0
            ),
            autoCreateTopics = "true",
            topicSuffixingStrategy =
                    TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
            exclude = {IllegalArgumentException.class,
                    IllegalStateException.class}
    )
    @KafkaListener(
            topics = "${kafka.topics.order-created}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void handleOrderCreatedEvents(OrderCreatedEvent event){
        log.info("Received OrderCreatedEvent | orderId={}",
                event.getOrderId());
        notificationService.processOrderCreatedEvent(event);
    }

    @DltHandler
    public void handleDlt(
            OrderCreatedEvent event,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic
    ) {
        log.error(
                "Notification message reached DLT | topic={} | orderId={}",
                topic,
                event.getOrderId()
        );
        notificationService.handleFailedEvent(event, topic);
    }

}
