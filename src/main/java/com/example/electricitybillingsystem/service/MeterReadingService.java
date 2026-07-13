package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.entity.MeterReading;
import com.example.electricitybillingsystem.repository.MeterReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MeterReadingService {
    @Autowired
    private MeterReadingRepository meterReadingRepository;
    @Autowired
    private BillService billService;
            public MeterReading saveMeterReading(MeterReading meterReading){
                log.info("Saving meter reading for connection ID: {}", meterReading.getConnection() != null ? meterReading.getConnection().getId() : "unknown");
                meterReading.setUnitsConsumed(
                        meterReading.getCurrentReading()
                                .subtract(meterReading.getPreviousReading()));

                MeterReading savedReading = meterReadingRepository.save(meterReading);
                log.info("Meter reading successfully saved. ID: {}, Units Consumed: {}", savedReading.getId(), savedReading.getUnitsConsumed());
                billService.generateBill(savedReading);
                return savedReading;
}
}
