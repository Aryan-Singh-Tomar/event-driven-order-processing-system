package com.orderflow.orderservice.service;

import com.orderflow.orderservice.dto.request.CreateOrderRequest;
import com.orderflow.orderservice.dto.response.OrderResponse;

public interface OrderService {
    OrderResponse createOrder(CreateOrderRequest request);
}
