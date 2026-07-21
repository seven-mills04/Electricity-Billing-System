package com.example.electricitybillingsystem.dto;

import com.example.electricitybillingsystem.entity.enums.PaymentMode;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDTO {
    @NotNull(message = "Payment mode is required")
    private PaymentMode paymentMode;
}
