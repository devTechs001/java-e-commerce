package com.ecommerce.controller;

import com.ecommerce.model.dto.request.CartItemRequest;
import com.ecommerce.model.dto.response.ApiResponse;
import com.ecommerce.service.CartService;
import com.ecommerce.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public ApiResponse getCart() {
        User user = getCurrentUser();
        return ApiResponse.success("Cart retrieved successfully", cartService.getCartResponse(user));
    }

    @PostMapping("/add")
    public ApiResponse addToCart(@RequestBody CartItemRequest request) {
        User user = getCurrentUser();
        cartService.addItem(user.getId(), request.getProductId(), request.getQuantity());
        return ApiResponse.success("Item added to cart", cartService.getCartResponse(user));
    }

    @DeleteMapping("/{itemId}")
    public ApiResponse removeFromCart(@PathVariable Long itemId) {
        cartService.removeItem(itemId);
        User user = getCurrentUser();
        return ApiResponse.success("Item removed from cart", cartService.getCartResponse(user));
    }

    private User getCurrentUser() {
        User user = new User();
        user.setId(1L);
        return user;
    }
}