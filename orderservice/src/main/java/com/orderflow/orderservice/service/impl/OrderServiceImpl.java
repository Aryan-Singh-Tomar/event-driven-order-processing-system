package com.orderflow.orderservice.service.impl;

import com.orderflow.orderservice.dto.request.CreateOrderRequest;
import com.orderflow.orderservice.dto.response.OrderResponse;
import com.orderflow.orderservice.entity.Order;
import com.orderflow.orderservice.event.OrderCreatedEvent;
import com.orderflow.orderservice.event.OrderEventPublisher;
import com.orderflow.orderservice.repository.OrderRepository;
import com.orderflow.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderEventPublisher orderEventPublisher;   // (1)
    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {

        // Step A: Request DTO → Entity
        Order order = mapToEntity(request);

        // Step B: DB mein save karo
        Order savedOrder = orderRepository.save(order);

        log.info("Order saved to DB | orderId={}", savedOrder.getId());

        OrderCreatedEvent event = mapToEvent(savedOrder);
        orderEventPublisher.publishOrderCreatedEvent(event);

        log.info("OrderCreatedEvent published | orderId={}", savedOrder.getId());


        // Step C: Saved Entity → Response DTO
        return mapToResponse(savedOrder);
    }

    // ---- Private helper methods ----

    private Order mapToEntity(CreateOrderRequest request) {
        return Order.builder()
                .customerName(request.getCustomerName())
                .productName(request.getProductName())
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .build();   // (7) status aur createdAt @PrePersist set karega
    }

    private OrderResponse mapToResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .customerName(order.getCustomerName())
                .productName(order.getProductName())
                .quantity(order.getQuantity())
                .price(order.getPrice())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }

    private OrderCreatedEvent mapToEvent(Order order) {
        return OrderCreatedEvent.builder()
                .orderId(order.getId())
                .customerName(order.getCustomerName())
                .productName(order.getProductName())
                .quantity(order.getQuantity())
                .price(order.getPrice())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .eventTimestamp(LocalDateTime.now())
                .build();
    }
}
