package com.orderflow.orderservice.event;

import com.orderflow.orderservice.config.KafkaTopics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventPublisher {

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public void publishOrderCreatedEvent(OrderCreatedEvent event){
        String key = String.valueOf(event.getOrderId());

        CompletableFuture<SendResult<String, OrderCreatedEvent>> future = kafkaTemplate.send(KafkaTopics.ORDER_CREATED, key, event);

        future.whenComplete((result, ex) -> {
            if(ex == null){
                log.info("OrderCreatedEvent published | orderId={} | partition={} | offset={}",
                        event.getOrderId(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset()   // (7)
                );
            }else{
                log.error("Failed to publish OrderCreatedEvent | orderId={} | error={}",
                        event.getOrderId(),
                        ex.getMessage()
                );
            }
        });

    }
}
