package com.example.electricitybillingsystem.entity;

import com.example.electricitybillingsystem.entity.enums.BillStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String billNumber;

    private String billingMonth;

    private LocalDate billDate;

    private LocalDate dueDate;

    private BigDecimal unitsConsumed;

    private BigDecimal energyCharge;

    private BigDecimal fixedCharge;

    private BigDecimal electricityDuty;

    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private BillStatus billStatus;

    @OneToOne
    @JoinColumn(name = "meter_reading_id")
    private MeterReading meterReading;
}