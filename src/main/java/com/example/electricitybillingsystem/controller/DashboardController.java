package com.example.electricitybillingsystem.controller;

import com.example.electricitybillingsystem.dto.DashboardResponseDTO;
import com.example.electricitybillingsystem.service.DashboardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.electricitybillingsystem.dto.PredictionDTO;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@RestController
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping({"/dashboard", "/api/dashboard"})
    public ResponseEntity<DashboardResponseDTO> getDashboardData() {
        DashboardResponseDTO dashboardData = dashboardService.getDashboardData();
        return new ResponseEntity<>(dashboardData, HttpStatus.OK);
    }

    @GetMapping("/api/dashboard/predictions")
    public ResponseEntity<List<PredictionDTO>> getPredictions(@RequestParam(required = false) String connectionNumber) {
        List<PredictionDTO> predictions = dashboardService.getConsumptionPredictions(connectionNumber);
        return new ResponseEntity<>(predictions, HttpStatus.OK);
    }
}
