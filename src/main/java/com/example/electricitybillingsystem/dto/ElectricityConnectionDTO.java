package com.example.electricitybillingsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ElectricityConnectionDTO {
    private Long id;
    private String connectionNumber;
    private String meterNumber;
    private String connectionType;
    private String status;
    private Double sanctionedLoad;
    private String phaseType;
    private Long consumerId;
    private ConsumerDTO consumer;
}
