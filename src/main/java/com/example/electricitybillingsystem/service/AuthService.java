package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.dto.AuthResponse;
import com.example.electricitybillingsystem.dto.ConsumerDTO;
import com.example.electricitybillingsystem.dto.LoginRequest;
import com.example.electricitybillingsystem.dto.RegisterRequest;
import com.example.electricitybillingsystem.entity.Consumer;
import com.example.electricitybillingsystem.entity.User;
import com.example.electricitybillingsystem.exception.DuplicateResourceException;
import com.example.electricitybillingsystem.mapper.ConsumerMapper;
import com.example.electricitybillingsystem.repository.ConsumerRepository;
import com.example.electricitybillingsystem.repository.UserRepository;
import com.example.electricitybillingsystem.security.JwtService;
import com.example.electricitybillingsystem.security.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final ConsumerRepository consumerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;
    private final ConsumerMapper consumerMapper;

    public AuthService(UserRepository userRepository,
                       ConsumerRepository consumerRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       UserDetailsServiceImpl userDetailsService,
                       ConsumerMapper consumerMapper) {
        this.userRepository = userRepository;
        this.consumerRepository = consumerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.consumerMapper = consumerMapper;
    }

    @Transactional
    public String register(RegisterRequest request) {
        log.info("Attempting registration for username: {}", request.getUsername());

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username is already taken.");
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
        log.info("User registered successfully: {}", request.getUsername());
        return "User registered successfully.";
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        log.info("Attempting login for username: {}", request.getUsername());

        User user = userRepository.findByUsername(request.getUsername())
                .orElse(null);

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Failed login attempt for username: {}", request.getUsername());
            throw new BadCredentialsException("Invalid username or password.");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtService.generateToken(userDetails);

        Long consumerId = null;
        String consumerName = "Admin User";

        if (user.getConsumer() != null) {
            consumerId = user.getConsumer().getId();
            consumerName = user.getConsumer().getFirstName() + " " + user.getConsumer().getLastName();
        }

        log.info("User logged in successfully: {}", request.getUsername());
        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole().replace("ROLE_", ""))
                .consumerId(consumerId)
                .consumerName(consumerName)
                .build();
    }

    @Transactional(readOnly = true)
    public List<ConsumerDTO> getConsumersForDropdown() {
        return consumerRepository.findAll().stream()
                .map(consumerMapper::toDTO)
                .collect(Collectors.toList());
    }
}
