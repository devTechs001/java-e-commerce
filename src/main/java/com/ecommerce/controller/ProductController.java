package com.ecommerce.controller;

import com.ecommerce.model.dto.response.ApiResponse;
import com.ecommerce.model.dto.response.ProductResponse;
import com.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ApiResponse getAllProducts() {
        return ApiResponse.success("Products retrieved successfully", productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ApiResponse getProductById(@PathVariable Long id) {
        return ApiResponse.success("Product retrieved successfully", productService.getProductById(id));
    }

    @GetMapping("/search")
    public ApiResponse searchProducts(@RequestParam String keyword) {
        return ApiResponse.success("Search results", productService.searchProducts(keyword, 0, 10));
    }
}