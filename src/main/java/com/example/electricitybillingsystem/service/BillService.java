package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.entity.Bill;
import com.example.electricitybillingsystem.entity.MeterReading;
import com.example.electricitybillingsystem.entity.enums.BillStatus;
import com.example.electricitybillingsystem.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.electricitybillingsystem.repository.ElectricityConnectionRepository;
import com.example.electricitybillingsystem.entity.ElectricityConnection;
import java.util.List;
import com.example.electricitybillingsystem.entity.enums.BillStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private TariffCalculatorService tariffCalculatorService;

    @Autowired
    private ElectricityConnectionRepository electricityConnectionRepository;

    public Bill generateBill(MeterReading meterReading) {
        log.info("Generating bill for meter reading id: {}", meterReading.getId());

        Bill bill = new Bill();

        bill.setBillNumber("BILL-" + UUID.randomUUID().toString().substring(0,8));

        bill.setBillingMonth(YearMonth.now().toString());

        bill.setBillDate(LocalDate.now());

        bill.setDueDate(LocalDate.now().plusDays(15));

        bill.setUnitsConsumed(meterReading.getUnitsConsumed());

        BigDecimal energyCharge =
                tariffCalculatorService.calculateEnergyCharge(
                        meterReading.getUnitsConsumed());

        bill.setEnergyCharge(energyCharge);

        Long connectionId = meterReading.getConnection().getId();

        ElectricityConnection connection =
                electricityConnectionRepository.findById(connectionId)
                        .orElseThrow(() -> new com.example.electricitybillingsystem.exception.ResourceNotFoundException("Connection not found"));
        log.info("Sanctioned Load = {}", connection.getSanctionedLoad());
        Double load = connection.getSanctionedLoad();

        BigDecimal fixedCharge =
                tariffCalculatorService.calculateFixedCharge(load);

        bill.setFixedCharge(fixedCharge);

        BigDecimal duty =
                tariffCalculatorService.calculateElectricityDuty(energyCharge);

        bill.setElectricityDuty(duty);

        bill.setTotalAmount(
                energyCharge
                        .add(fixedCharge)
                        .add(duty));

        bill.setBillStatus(BillStatus.UNPAID);

        bill.setMeterReading(meterReading);

        Bill savedBill = billRepository.save(bill);
        log.info("Bill successfully generated with bill number: {} for amount: {}", savedBill.getBillNumber(), savedBill.getTotalAmount());
        return savedBill;
    }

    public Page<Bill> getAllBills(Pageable pageable) {
        return billRepository.findAll(pageable);
    }

    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    public Bill getBillById(Long id) {
        return billRepository.findById(id)
                .orElseThrow(() -> new com.example.electricitybillingsystem.exception.ResourceNotFoundException("Bill not found"));
    }

    public List<Bill> getBillsByStatus(BillStatus status) {
        return billRepository.findByBillStatus(status);
    }

    public List<Bill> getBillsByMonth(String month) {
        return billRepository.findByBillingMonth(month);
    }

    public List<Bill> searchBills(String month, BillStatus status, Long consumerId, String consumerNumber) {
        if (month != null && !month.trim().isEmpty()) {
            return billRepository.findByBillingMonth(month);
        } else if (status != null) {
            return billRepository.findByBillStatus(status);
        } else if (consumerId != null) {
            return billRepository.findByConsumerId(consumerId);
        } else if (consumerNumber != null && !consumerNumber.trim().isEmpty()) {
            return billRepository.findByConsumerNumber(consumerNumber);
        }
        return billRepository.findAll();
    }
}