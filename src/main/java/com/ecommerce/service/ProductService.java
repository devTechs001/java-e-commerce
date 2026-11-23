package com.ecommerce.service;

import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.Product;
import com.ecommerce.model.dto.response.ProductResponse;
import com.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ProductResponse getProductResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setDiscountPrice(product.getDiscountPrice());
        response.setImageUrl(product.getImageUrl());
        response.setBrand(product.getBrand());
        response.setSku(product.getSku());
        response.setStockQuantity(product.getStockQuantity());
        response.setActive(product.getActive());
        response.setFeatured(product.getFeatured());
        response.setNewArrival(product.getNewArrival());
        response.setCategoryName(product.getCategory() != null ? product.getCategory().getName() : null);
        response.setCategorySlug(product.getCategory() != null ? product.getCategory().getSlug() : null);
        response.setSellerEmail(product.getSeller() != null ? product.getSeller().getEmail() : null);
        response.setRating(product.getRating());
        response.setReviewCount(product.getReviewCount());
        return response;
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return getProductResponse(product);
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findByActiveTrue().stream()
                .map(this::getProductResponse)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> searchProducts(String keyword, Integer page, Integer size) {
        return productRepository.findByNameContainingIgnoreCase(keyword).stream()
                .map(this::getProductResponse)
                .collect(Collectors.toList());
    }
}