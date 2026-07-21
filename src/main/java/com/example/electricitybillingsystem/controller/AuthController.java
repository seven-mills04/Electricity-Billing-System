package com.example.electricitybillingsystem.controller;

import com.example.electricitybillingsystem.dto.AuthResponse;
import com.example.electricitybillingsystem.dto.ConsumerDTO;
import com.example.electricitybillingsystem.dto.LoginRequest;
import com.example.electricitybillingsystem.dto.RegisterRequest;
import com.example.electricitybillingsystem.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        String message = authService.register(request);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/consumers")
    public ResponseEntity<List<ConsumerDTO>> getConsumersForDropdown() {
        List<ConsumerDTO> consumers = authService.getConsumersForDropdown();
        return ResponseEntity.ok(consumers);
    }
}
