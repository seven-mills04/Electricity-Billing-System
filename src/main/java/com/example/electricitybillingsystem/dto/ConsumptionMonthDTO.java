package com.example.electricitybillingsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsumptionMonthDTO {
    private String month;
    private BigDecimal unitsConsumed;
}
