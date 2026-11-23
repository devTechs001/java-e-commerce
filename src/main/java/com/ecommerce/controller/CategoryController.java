package com.ecommerce.controller;

import com.ecommerce.model.dto.response.ApiResponse;
import com.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ApiResponse getRootCategories() {
        return ApiResponse.success("Categories retrieved successfully", categoryService.getRootCategories());
    }

    @GetMapping("/{slug}")
    public ApiResponse getCategoryBySlug(@PathVariable String slug) {
        return ApiResponse.success("Category retrieved successfully", categoryService.getCategoryBySlug(slug));
    }
}