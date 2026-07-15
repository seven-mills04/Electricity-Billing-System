package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.entity.MeterReading;
import com.example.electricitybillingsystem.repository.MeterReadingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MeterReadingService {

    @Autowired
    private MeterReadingRepository meterReadingRepository;

    @Autowired
    private BillService billService;

    public MeterReading saveMeterReading(MeterReading meterReading) {

        log.info("Saving meter reading for connection ID: {}",
                meterReading.getConnection() != null
                        ? meterReading.getConnection().getId()
                        : "unknown");

        if (meterReading.getCurrentReading()
                .compareTo(meterReading.getPreviousReading()) < 0) {

            throw new IllegalArgumentException(
                    "Current reading cannot be less than previous reading."
            );
        }

        meterReading.setUnitsConsumed(
                meterReading.getCurrentReading()
                        .subtract(meterReading.getPreviousReading())
        );

        MeterReading savedReading = meterReadingRepository.save(meterReading);

        log.info("Meter reading successfully saved. ID: {}, Units Consumed: {}",
                savedReading.getId(),
                savedReading.getUnitsConsumed());

        billService.generateBill(savedReading);

        return savedReading;
    }

    public List<MeterReading> getAllMeterReadings() {
        return meterReadingRepository.findAll();
    }

    public MeterReading getMeterReadingById(Long id) {
        return meterReadingRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Meter Reading not found with id " + id));
    }

    public MeterReading updateMeterReading(Long id, MeterReading updatedReading) {

        MeterReading reading = getMeterReadingById(id);

        if (updatedReading.getCurrentReading()
                .compareTo(updatedReading.getPreviousReading()) < 0) {

            throw new  IllegalArgumentException(
                    "Current reading cannot be less than previous reading."
            );
        }

        reading.setReadingDate(updatedReading.getReadingDate());
        reading.setPreviousReading(updatedReading.getPreviousReading());
        reading.setCurrentReading(updatedReading.getCurrentReading());

        reading.setUnitsConsumed(
                updatedReading.getCurrentReading()
                        .subtract(updatedReading.getPreviousReading())
        );

        reading.setRemarks(updatedReading.getRemarks());
        reading.setConnection(updatedReading.getConnection());

        MeterReading savedReading = meterReadingRepository.save(reading);

        billService.generateBill(savedReading);

        return savedReading;
    }

    public void deleteMeterReading(Long id) {

        MeterReading reading = getMeterReadingById(id);

        meterReadingRepository.delete(reading);
    }
}