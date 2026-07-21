package com.example.electricitybillingsystem.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeterReadingRequestDTO {
    @NotNull(message = "Reading date is required")
    private LocalDate readingDate;

    @NotNull(message = "Previous reading is required")
    @Min(value = 0, message = "Previous reading must be non-negative")
    private BigDecimal previousReading;

    @NotNull(message = "Current reading is required")
    @Min(value = 0, message = "Current reading must be non-negative")
    private BigDecimal currentReading;

    private String remarks;

    private ConnectionRefDTO connection;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ConnectionRefDTO {
        private Long id;
    }
}
