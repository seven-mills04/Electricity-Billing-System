package com.example.electricitybillingsystem.controller;

import com.example.electricitybillingsystem.entity.MeterReading;
import com.example.electricitybillingsystem.service.MeterReadingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meter-readings")
public class MeterReadingController {

    @Autowired
    private MeterReadingService meterReadingService;

    @PostMapping
    public ResponseEntity<MeterReading> saveMeterReading(
            @Valid @RequestBody MeterReading meterReading) {

        MeterReading savedReading =
                meterReadingService.saveMeterReading(meterReading);

        return new ResponseEntity<>(savedReading, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MeterReading>> getAllMeterReadings() {

        List<MeterReading> readings =
                meterReadingService.getAllMeterReadings();

        return new ResponseEntity<>(readings, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeterReading> getMeterReadingById(
            @PathVariable Long id) {

        MeterReading reading =
                meterReadingService.getMeterReadingById(id);

        return new ResponseEntity<>(reading, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MeterReading> updateMeterReading(
            @PathVariable Long id,
            @Valid @RequestBody MeterReading meterReading) {

        MeterReading updatedReading =
                meterReadingService.updateMeterReading(id, meterReading);

        return new ResponseEntity<>(updatedReading, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMeterReading(
            @PathVariable Long id) {

        meterReadingService.deleteMeterReading(id);

        return new ResponseEntity<>(
                "Meter Reading deleted successfully",
                HttpStatus.OK
        );
    }
}