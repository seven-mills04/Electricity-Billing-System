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
public class DashboardResponseDTO {
    private long totalConsumers;
    private long totalConnections;
    private long totalBills;
    private long paidBills;
    private long unpaidBills;
    private BigDecimal monthlyRevenue;
    private BigDecimal todayCollection;
    private BigDecimal totalRevenue;
}
