package com.ecommerce.service;

import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.Cart;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderItem;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.model.dto.request.CartItemRequest;
import com.ecommerce.model.dto.response.OrderResponse;
import com.ecommerce.repository.CartItemRepository;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Transactional
    public OrderResponse placeOrder(User user, CartItemRequest cartItemRequest, String paymentMethod) {
        Cart cart = cartService.getOrCreateCart(user);

        List<CartItem> cartItems = cart.getCartItems();
        if (cartItems == null || cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(com.ecommerce.model.OrderStatus.PENDING);
        order.setOrderNumber("ORD-" + System.currentTimeMillis());
        order.setPaymentMethod(paymentMethod);
        order.setPaymentStatus(com.ecommerce.model.PaymentStatus.PENDING);
        order.setTotalAmount(cart.getTotal());
        order.setCreatedAt(LocalDateTime.now());

        Double subtotal = 0.0;
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setSubtotal(cartItem.getSubtotal());

            Product product = cartItem.getProduct();
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product);

            subtotal += cartItem.getSubtotal();
            order.getOrderItems().add(orderItem);
        }

        order.setSubtotal(subtotal);
        order.setTax(subtotal * 0.10);
        order.setShipping(5.00);
        order.setDiscount(0.0);
        order.setTotalAmount(subtotal + order.getTax() + order.getShipping());

        order.setStatus(com.ecommerce.model.OrderStatus.CONFIRMED);
        order = orderRepository.save(order);

        cartItemRepository.deleteAll(cartItems);

        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setOrderNumber(order.getOrderNumber());
        response.setStatus(order.getStatus().toString());
        response.setTotalAmount(order.getTotalAmount());
        response.setSubtotal(order.getSubtotal());
        response.setTax(order.getTax());
        response.setShipping(order.getShipping());
        response.setPaid(false);
        response.setPaymentMethod(paymentMethod);
        return response;
    }

    public List<OrderResponse> getUserOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(order -> {
            OrderResponse response = new OrderResponse();
            response.setId(order.getId());
            response.setOrderNumber(order.getOrderNumber());
            response.setStatus(order.getStatus().toString());
            response.setTotalAmount(order.getTotalAmount());
            response.setSubtotal(order.getSubtotal());
            response.setTax(order.getTax());
            response.setShipping(order.getShipping());
            response.setPaid(order.isPaid());
            response.setPaymentMethod(order.getPaymentMethod());
            return response;
        }).collect(java.util.stream.Collectors.toList());
    }
        public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setOrderNumber(order.getOrderNumber());
        response.setStatus(order.getStatus().toString());
        response.setTotalAmount(order.getTotalAmount());
        response.setSubtotal(order.getSubtotal());
        response.setTax(order.getTax());
        response.setShipping(order.getShipping());
        response.setPaid(order.isPaid());
        response.setPaymentMethod(order.getPaymentMethod());
        return response;
    }
}