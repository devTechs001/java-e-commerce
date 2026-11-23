package com.ecommerce.service;

import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.Cart;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.model.dto.response.CartItemResponse;
import com.ecommerce.model.dto.response.CartResponse;
import com.ecommerce.repository.CartItemRepository;
import com.ecommerce.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    public Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }

    public Cart addItem(Long userId, Long productId, Integer quantity) {
        // Simplified implementation - will integrate with auth context
        throw new UnsupportedOperationException("Cart operations require authenticated user");
    }

    public void removeItem(Long itemId) {
        cartItemRepository.deleteById(itemId);
    }

    public CartItem updateItemQuantity(Long itemId, Integer quantity) {
        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + itemId));

        if (quantity <= 0) {
            removeItem(itemId);
            return null;
        }

        cartItem.setQuantity(quantity);
        cartItem.setSubtotal(cartItem.getProduct().getPrice() * quantity);
        return cartItemRepository.save(cartItem);
    }

    public Double getTotal(User user) {
        Cart cart = getOrCreateCart(user);
        Double total = 0.0;
        if (cart.getCartItems() != null) {
            for (CartItem item : cart.getCartItems()) {
                total += item.getSubtotal();
            }
        }
        return total;
    }

    public CartResponse getCartResponse(User user) {
        Cart cart = getOrCreateCart(user);
        List<CartItemResponse> items = new ArrayList<>();
        if (cart.getCartItems() != null) {
            for (CartItem item : cart.getCartItems()) {
                Product product = item.getProduct();
                CartItemResponse itemResponse = new CartItemResponse();
                itemResponse.setId(item.getId());
                itemResponse.setProductId(product.getId());
                itemResponse.setProductName(product.getName());
                itemResponse.setProductImageUrl(product.getImageUrl());
                itemResponse.setQuantity(item.getQuantity());
                itemResponse.setPrice(product.getPrice());
                itemResponse.setSubtotal(item.getSubtotal());
                items.add(itemResponse);
            }
        }

        CartResponse response = new CartResponse();
        response.setId(cart.getId());
        response.setTotal(cart.getTotal());
        response.setItemCount(cart.getCartItems() != null ? cart.getCartItems().size() : 0);
        response.setItems(items);
        return response;
    }
}