package com.example.electricitybillingsystem.controller;

import com.example.electricitybillingsystem.entity.MeterReading;
import com.example.electricitybillingsystem.service.MeterReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/meterReading")
public class MeterReadingController {

    @Autowired
    private MeterReadingService meterReadingService;

    @PostMapping("/save")
    public MeterReading saveMeterReading(@Valid @RequestBody MeterReading meterReading) {
        return meterReadingService.saveMeterReading(meterReading);
    }
}