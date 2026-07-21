package com.example.electricitybillingsystem.dto;

import com.example.electricitybillingsystem.entity.enums.PaymentMode;
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
public class PaymentDTO {
    private Long id;
    private LocalDate paymentDate;
    private BigDecimal amountPaid;
    private PaymentMode paymentMode;
    private String transactionId;
    private BillDTO bill;
}
