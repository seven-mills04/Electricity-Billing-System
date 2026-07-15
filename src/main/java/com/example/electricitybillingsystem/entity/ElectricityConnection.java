package com.example.electricitybillingsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.validation.constraints.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ElectricityConnection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @ManyToOne
    @JoinColumn(name = "consumer_id")
    @JsonBackReference
    private Consumer consumer;
}