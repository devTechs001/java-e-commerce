package com.ecommerce.controller;

import com.ecommerce.model.dto.response.ApiResponse;
import com.ecommerce.model.dto.response.OrderResponse;
import com.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ApiResponse getUserOrders() {
        return ApiResponse.success("Orders retrieved successfully", orderService.getUserOrders());
    }

    @GetMapping("/{id}")
    public ApiResponse getOrderById(@PathVariable Long id) {
        return ApiResponse.success("Order retrieved successfully", orderService.getOrderById(id));
    }
}