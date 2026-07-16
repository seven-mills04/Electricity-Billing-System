package com.example.electricitybillingsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PredictionDTO {
    private String month;
    private double predictedKwh;
    private double lowerBoundKwh;
    private double upperBoundKwh;
}
