package com.example.electricitybillingsystem.mapper;

import com.example.electricitybillingsystem.dto.PaymentDTO;
import com.example.electricitybillingsystem.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    private final BillMapper billMapper;

    public PaymentMapper(BillMapper billMapper) {
        this.billMapper = billMapper;
    }

    public PaymentDTO toDTO(Payment entity) {
        if (entity == null) {
            return null;
        }

        return PaymentDTO.builder()
                .id(entity.getId())
                .paymentDate(entity.getPaymentDate())
                .amountPaid(entity.getAmountPaid())
                .paymentMode(entity.getPaymentMode())
                .transactionId(entity.getTransactionId())
                .bill(billMapper.toDTO(entity.getBill()))
                .build();
    }
}
