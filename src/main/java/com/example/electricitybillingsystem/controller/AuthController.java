package com.example.electricitybillingsystem.controller;

import com.example.electricitybillingsystem.dto.AuthResponse;
import com.example.electricitybillingsystem.dto.LoginRequest;
import com.example.electricitybillingsystem.dto.RegisterRequest;
import com.example.electricitybillingsystem.entity.Consumer;
import com.example.electricitybillingsystem.entity.User;
import com.example.electricitybillingsystem.repository.ConsumerRepository;
import com.example.electricitybillingsystem.repository.UserRepository;
import com.example.electricitybillingsystem.security.JwtService;
import com.example.electricitybillingsystem.security.UserDetailsServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final ConsumerRepository consumerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthController(UserRepository userRepository,
                          ConsumerRepository consumerRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService,
                          UserDetailsServiceImpl userDetailsService) {
        this.userRepository = userRepository;
        this.consumerRepository = consumerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is already taken.");
        }

        Consumer consumer = null;
        if (request.getConsumerId() != null) {
            consumer = consumerRepository.findById(request.getConsumerId()).orElse(null);
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() != null ? request.getRole() : "ROLE_CONSUMER")
                .consumer(consumer)
                .build();

        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElse(null);

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtService.generateToken(userDetails);

        Long consumerId = null;
        String consumerName = "Admin User";

        if (user.getConsumer() != null) {
            consumerId = user.getConsumer().getId();
            consumerName = user.getConsumer().getFirstName() + " " + user.getConsumer().getLastName();
        }

        AuthResponse response = AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole().replace("ROLE_", ""))
                .consumerId(consumerId)
                .consumerName(consumerName)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/consumers")
    public ResponseEntity<?> getConsumersForDropdown() {
        return ResponseEntity.ok(consumerRepository.findAll());
    }
}
