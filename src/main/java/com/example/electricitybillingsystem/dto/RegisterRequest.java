package com.example.electricitybillingsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String username;
    private String password;
    private String role; // "ROLE_ADMIN" or "ROLE_CONSUMER"
    private Long consumerId; // optional consumer link
}
