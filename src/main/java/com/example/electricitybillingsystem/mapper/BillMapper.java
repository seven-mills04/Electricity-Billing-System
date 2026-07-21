package com.example.electricitybillingsystem.mapper;

import com.example.electricitybillingsystem.dto.BillDTO;
import com.example.electricitybillingsystem.entity.Bill;
import org.springframework.stereotype.Component;

@Component
public class BillMapper {

    private final MeterReadingMapper meterReadingMapper;

    public BillMapper(MeterReadingMapper meterReadingMapper) {
        this.meterReadingMapper = meterReadingMapper;
    }

    public BillDTO toDTO(Bill entity) {
        if (entity == null) {
            return null;
        }

        return BillDTO.builder()
                .id(entity.getId())
                .billNumber(entity.getBillNumber())
                .billingMonth(entity.getBillingMonth())
                .billDate(entity.getBillDate())
                .dueDate(entity.getDueDate())
                .unitsConsumed(entity.getUnitsConsumed())
                .energyCharge(entity.getEnergyCharge())
                .fixedCharge(entity.getFixedCharge())
                .electricityDuty(entity.getElectricityDuty())
                .totalAmount(entity.getTotalAmount())
                .billStatus(entity.getBillStatus())
                .meterReading(meterReadingMapper.toDTO(entity.getMeterReading()))
                .build();
    }
}
