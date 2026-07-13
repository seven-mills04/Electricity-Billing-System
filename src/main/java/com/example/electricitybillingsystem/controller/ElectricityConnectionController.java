package com.example.electricitybillingsystem.controller;

import com.example.electricitybillingsystem.entity.ElectricityConnection;
import com.example.electricitybillingsystem.service.ElectricityConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/electricityConnection")
public class ElectricityConnectionController {

    @Autowired
    private ElectricityConnectionService electricityConnectionService;

    @PostMapping("/save")
    public ElectricityConnection saveElectricityConnection(
            @Valid @RequestBody ElectricityConnection electricityConnection) {

        return electricityConnectionService.saveElectricityConnection(electricityConnection);
    }
}
