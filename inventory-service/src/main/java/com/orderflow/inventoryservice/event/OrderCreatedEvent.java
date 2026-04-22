package com.orderflow.inventoryservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreatedEvent {

    private Long orderId;
    private String customerName;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime eventTimestamp;
}