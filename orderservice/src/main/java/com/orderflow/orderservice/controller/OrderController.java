package com.orderflow.orderservice.controller;

import com.orderflow.orderservice.dto.request.CreateOrderRequest;
import com.orderflow.orderservice.dto.response.OrderResponse;
import com.orderflow.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(@RequestBody CreateOrderRequest request){
        return orderService.createOrder(request);
    }
}
