package com.example.electricitybillingsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerDTO {
    private Long id;
    private String consumerNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private List<ElectricityConnectionDTO> connections;
}
