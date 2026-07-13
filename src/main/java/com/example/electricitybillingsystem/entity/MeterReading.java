package com.example.electricitybillingsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MeterReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Reading date is required")
    private LocalDate readingDate;

    @NotNull(message = "Previous reading is required")
    @Min(value = 0, message = "Previous reading must be non-negative")
    private BigDecimal previousReading;

    @NotNull(message = "Current reading is required")
    @Min(value = 0, message = "Current reading must be non-negative")
    private BigDecimal currentReading;
    private BigDecimal unitsConsumed;

    private String remarks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "connection_id")
    private ElectricityConnection connection;


}
