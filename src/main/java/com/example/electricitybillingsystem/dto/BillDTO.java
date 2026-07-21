package com.example.electricitybillingsystem.dto;

import com.example.electricitybillingsystem.entity.enums.BillStatus;
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
public class BillDTO {
    private Long id;
    private String billNumber;
    private String billingMonth;
    private LocalDate billDate;
    private LocalDate dueDate;
    private BigDecimal unitsConsumed;
    private BigDecimal energyCharge;
    private BigDecimal fixedCharge;
    private BigDecimal electricityDuty;
    private BigDecimal totalAmount;
    private BillStatus billStatus;
    private MeterReadingDTO meterReading;
}
