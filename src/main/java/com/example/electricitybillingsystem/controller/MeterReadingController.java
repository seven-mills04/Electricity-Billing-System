package com.example.electricitybillingsystem.controller;

import com.example.electricitybillingsystem.dto.MeterReadingDTO;
import com.example.electricitybillingsystem.dto.MeterReadingRequestDTO;
import com.example.electricitybillingsystem.service.MeterReadingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meter-readings")
public class MeterReadingController {

    private final MeterReadingService meterReadingService;

    public MeterReadingController(MeterReadingService meterReadingService) {
        this.meterReadingService = meterReadingService;
    }

    @PostMapping
    public ResponseEntity<MeterReadingDTO> saveMeterReading(
            @Valid @RequestBody MeterReadingRequestDTO meterReading) {

        MeterReadingDTO savedReading =
                meterReadingService.saveMeterReading(meterReading);

        return new ResponseEntity<>(savedReading, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MeterReadingDTO>> getAllMeterReadings() {

        List<MeterReadingDTO> readings =
                meterReadingService.getAllMeterReadings();

        return ResponseEntity.ok(readings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeterReadingDTO> getMeterReadingById(@PathVariable Long id) {

        MeterReadingDTO reading =
                meterReadingService.getMeterReadingById(id);

        return ResponseEntity.ok(reading);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MeterReadingDTO> updateMeterReading(
            @PathVariable Long id,
            @Valid @RequestBody MeterReadingRequestDTO meterReading) {

        MeterReadingDTO updatedReading =
                meterReadingService.updateMeterReading(id, meterReading);

        return ResponseEntity.ok(updatedReading);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMeterReading(@PathVariable Long id) {

        meterReadingService.deleteMeterReading(id);

        return ResponseEntity.ok("Meter Reading deleted successfully");
    }
}