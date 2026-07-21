package com.example.electricitybillingsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ElectricityConnectionRequestDTO {
    @NotBlank(message = "Connection number is required")
    private String connectionNumber;

    @NotBlank(message = "Meter number is required")
    private String meterNumber;

    @NotBlank(message = "Connection type is required")
    private String connectionType;

    @NotBlank(message = "Status is required")
    private String status;

    @NotNull(message = "Sanctioned load is required")
    @Positive(message = "Sanctioned load must be positive")
    private Double sanctionedLoad;

    @NotBlank(message = "Phase type is required")
    private String phaseType;

    private Long consumerId;
}
