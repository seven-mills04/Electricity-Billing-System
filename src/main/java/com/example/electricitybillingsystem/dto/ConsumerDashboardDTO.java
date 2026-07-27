package com.example.electricitybillingsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerDashboardDTO {
    private String consumerNumber;
    private String fullName;
    private String connectionNumber;
    private String meterNumber;
    private String connectionType;
    private String connectionStatus;
    
    private BigDecimal currentMonthUnitsConsumed;
    private BigDecimal currentOutstandingBill;
    private String paymentStatus;
    private Double currentTariff;
    private BigDecimal lastPaymentAmount;
    private LocalDate lastPaymentDate;

    private List<BillDTO> recentBills;
    private List<PaymentDTO> paymentHistory;
    private List<ConsumptionMonthDTO> monthlyConsumptionHistory;
}
