package com.example.electricitybillingsystem.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TariffCalculatorService {
    public BigDecimal calculateEnergyCharge(BigDecimal units){
        double unit=units.doubleValue();
        if (unit<=200)
            return units.multiply(BigDecimal.valueOf(3.00));
        if (unit<=400)
            return units.multiply(BigDecimal.valueOf(4.50));
        if (unit<=800)
            return units.multiply(BigDecimal.valueOf(6.50));
        if (unit<=1200)
            return units.multiply(BigDecimal.valueOf(7.00));
        else
            return units.multiply(BigDecimal.valueOf(7.75));
    }
    public BigDecimal calculateFixedCharge(Double sanctionedLoad) {

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
    public BigDecimal calculateElectricityDuty(BigDecimal energyCharge){
        return energyCharge.multiply(BigDecimal.valueOf(0.05));
    }
}
