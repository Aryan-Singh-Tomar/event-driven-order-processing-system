package com.orderflow.orderservice.dto.response;

import com.orderflow.orderservice.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long id;
    private String customerName;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
    private OrderStatus status;
    private LocalDateTime createdAt;

}
