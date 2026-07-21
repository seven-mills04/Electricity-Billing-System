package com.example.electricitybillingsystem.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class TariffCalculatorService {

    public BigDecimal calculateEnergyCharge(BigDecimal units) {
        if (units == null || units.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        double unit = units.doubleValue();
        if (unit <= 200) {
            return units.multiply(BigDecimal.valueOf(3.00));
        } else if (unit <= 400) {
            return units.multiply(BigDecimal.valueOf(4.50));
        } else if (unit <= 800) {
            return units.multiply(BigDecimal.valueOf(6.50));
        } else if (unit <= 1200) {
            return units.multiply(BigDecimal.valueOf(7.00));
        } else {
            return units.multiply(BigDecimal.valueOf(7.75));
        }
    }

    public BigDecimal calculateFixedCharge(Double sanctionedLoad) {
        if (sanctionedLoad == null || sanctionedLoad <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal rate;
        if (sanctionedLoad <= 2) {
            rate = BigDecimal.valueOf(125);
        } else if (sanctionedLoad <= 4) {
            rate = BigDecimal.valueOf(275);
        } else if (sanctionedLoad <= 6) {
            rate = BigDecimal.valueOf(400);
        } else {
            rate = BigDecimal.valueOf(500);
        }

        return BigDecimal.valueOf(sanctionedLoad).multiply(rate);
    }

    public BigDecimal calculateElectricityDuty(BigDecimal energyCharge) {
        if (energyCharge == null || energyCharge.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        return energyCharge.multiply(BigDecimal.valueOf(0.05));
    }
}
