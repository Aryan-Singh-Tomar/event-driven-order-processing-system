package com.orderflow.inventoryservice.consumer;

import com.orderflow.inventoryservice.event.OrderCreatedEvent;
import com.orderflow.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class InventoryConsumer {

    private final InventoryService inventoryService;


    @RetryableTopic(
            attempts = "4",
            backoff = @Backoff(
                    delay = 1000,
                    multiplier = 2.0
            ),
            autoCreateTopics = "true",
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
            exclude = {IllegalArgumentException.class,
            IllegalStateException.class}
    )
    @KafkaListener(
            topics = "${kafka.topics.order-created}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void handleOrderCreatedEvent(OrderCreatedEvent event){
        log.info("Received OrderCreatedEvent | orderId={}",event.getOrderId());
        inventoryService.processOrderCreatedEvent(event);
    }

    public void handleDlt(OrderCreatedEvent event, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic){
        log.error(
                "Message reached DLT — manual intervention required " +
                        "| topic={} | orderId={}",
                topic,
                event.getOrderId()
        );
        inventoryService.handleFailedEvent(event, topic);
    }
}
