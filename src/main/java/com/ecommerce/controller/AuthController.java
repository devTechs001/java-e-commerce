package com.ecommerce.controller;

import com.ecommerce.model.dto.request.LoginRequest;
import com.ecommerce.model.dto.request.RegisterRequest;
import com.ecommerce.model.dto.response.ApiResponse;
import com.ecommerce.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ApiResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public ApiResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}