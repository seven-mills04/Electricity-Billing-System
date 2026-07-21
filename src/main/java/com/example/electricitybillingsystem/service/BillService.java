package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.dto.BillDTO;
import com.example.electricitybillingsystem.entity.Bill;
import com.example.electricitybillingsystem.entity.ElectricityConnection;
import com.example.electricitybillingsystem.entity.MeterReading;
import com.example.electricitybillingsystem.entity.enums.BillStatus;
import com.example.electricitybillingsystem.exception.ResourceNotFoundException;
import com.example.electricitybillingsystem.mapper.BillMapper;
import com.example.electricitybillingsystem.repository.BillRepository;
import com.example.electricitybillingsystem.repository.ElectricityConnectionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BillService {

    private final BillRepository billRepository;
    private final TariffCalculatorService tariffCalculatorService;
    private final ElectricityConnectionRepository electricityConnectionRepository;
    private final BillMapper billMapper;

    public BillService(BillRepository billRepository,
                       TariffCalculatorService tariffCalculatorService,
                       ElectricityConnectionRepository electricityConnectionRepository,
                       BillMapper billMapper) {
        this.billRepository = billRepository;
        this.tariffCalculatorService = tariffCalculatorService;
        this.electricityConnectionRepository = electricityConnectionRepository;
        this.billMapper = billMapper;
    }

    @Transactional
    public Bill generateBill(MeterReading meterReading) {
        log.info("Generating bill for meter reading id: {}", meterReading.getId());

        Bill bill = billRepository
                .findByMeterReadingId(meterReading.getId())
                .orElse(new Bill());

        if (bill.getId() == null) {
            bill.setBillNumber("BILL-" + UUID.randomUUID().toString().substring(0, 8));
        }

        bill.setBillingMonth(YearMonth.from(meterReading.getReadingDate()).toString());
        bill.setBillDate(LocalDate.now());
        bill.setDueDate(LocalDate.now().plusDays(15));
        bill.setUnitsConsumed(meterReading.getUnitsConsumed());

        BigDecimal energyCharge = tariffCalculatorService.calculateEnergyCharge(meterReading.getUnitsConsumed());
        bill.setEnergyCharge(energyCharge);

        Long connectionId = meterReading.getConnection().getId();
        ElectricityConnection connection = electricityConnectionRepository.findById(connectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Connection not found with id: " + connectionId));

        Double load = connection.getSanctionedLoad();
        BigDecimal fixedCharge = tariffCalculatorService.calculateFixedCharge(load);
        bill.setFixedCharge(fixedCharge);

        BigDecimal duty = tariffCalculatorService.calculateElectricityDuty(energyCharge);
        bill.setElectricityDuty(duty);

        bill.setTotalAmount(energyCharge.add(fixedCharge).add(duty));
        bill.setBillStatus(BillStatus.UNPAID);
        bill.setMeterReading(meterReading);

        Bill savedBill = billRepository.save(bill);
        log.info("Bill successfully generated with bill number: {} for amount: {}", savedBill.getBillNumber(), savedBill.getTotalAmount());
        return savedBill;
    }

    @Transactional(readOnly = true)
    public Page<BillDTO> getAllBills(Pageable pageable) {
        return billRepository.findAll(pageable)
                .map(billMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public List<BillDTO> getAllBills() {
        return billRepository.findAll().stream()
                .map(billMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BillDTO getBillById(Long id) {
        return billRepository.findById(id)
                .map(billMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<BillDTO> getBillsByStatus(BillStatus status) {
        return billRepository.findByBillStatus(status).stream()
                .map(billMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BillDTO> getBillsByMonth(String month) {
        return billRepository.findByBillingMonth(month).stream()
                .map(billMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BillDTO> searchBills(String month, BillStatus status, Long consumerId, String consumerNumber) {
        List<Bill> bills;
        if (month != null && !month.trim().isEmpty()) {
            bills = billRepository.findByBillingMonth(month);
        } else if (status != null) {
            bills = billRepository.findByBillStatus(status);
        } else if (consumerId != null) {
            bills = billRepository.findByConsumerId(consumerId);
        } else if (consumerNumber != null && !consumerNumber.trim().isEmpty()) {
            bills = billRepository.findByConsumerNumber(consumerNumber);
        } else {
            bills = billRepository.findAll();
        }
        return bills.stream()
                .map(billMapper::toDTO)
                .collect(Collectors.toList());
    }
}