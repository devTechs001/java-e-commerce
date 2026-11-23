package com.ecommerce.javafx.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ecommerce.model.dto.response.ProductResponse;
import com.ecommerce.model.dto.response.CartResponse;
import com.ecommerce.model.dto.response.OrderResponse;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ApiService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private static final String BASE_URL = "http://localhost:8080/api";
    private String token = null;

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    private String getJson(String path) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(BASE_URL + path))
                .timeout(Duration.ofSeconds(15))
                .header("Content-Type", "application/json");

        if (token != null && !token.isBlank()) {
            builder.header("Authorization", "Bearer " + token);
        }

        HttpRequest request = builder.GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 400) {
            throw new RuntimeException("API Error: " + response.statusCode() + " - " + response.body());
        }
        return response.body();
    }

    public List<ProductResponse> getProducts() throws IOException, InterruptedException {
        String body = getJson("/products");
        com.ecommerce.model.dto.response.ApiResponse resp = objectMapper.readValue(body, com.ecommerce.model.dto.response.ApiResponse.class);
        if (resp.getData() != null) {
            return objectMapper.convertValue(resp.getData(), new TypeReference<List<ProductResponse>>() {});
        }
        return Collections.emptyList();
    }

    public void addToCart(Long productId, int quantity) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + "/cart/add"))
                .timeout(Duration.ofSeconds(15))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(
                    Map.of("productId", productId, "quantity", quantity))))
                .header("Authorization", "Bearer " + token)
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 400) {
            throw new RuntimeException("Failed to add to cart: " + response.statusCode());
        }
    }

    public CartResponse getCart() throws IOException, InterruptedException {
        String body = getJson("/cart");
        com.ecommerce.model.dto.response.ApiResponse resp = objectMapper.readValue(body, com.ecommerce.model.dto.response.ApiResponse.class);
        if (resp.getData() != null) {
            return objectMapper.convertValue(resp.getData(), CartResponse.class);
        }
        return new CartResponse();
    }

    public List<OrderResponse> getOrders() throws IOException, InterruptedException {
        String body = getJson("/orders");
        com.ecommerce.model.dto.response.ApiResponse resp = objectMapper.readValue(body, com.ecommerce.model.dto.response.ApiResponse.class);
        if (resp.getData() != null) {
            return objectMapper.convertValue(resp.getData(), new TypeReference<List<OrderResponse>>() {});
        }
        return Collections.emptyList();
    }
}