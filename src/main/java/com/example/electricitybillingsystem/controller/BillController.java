package com.example.electricitybillingsystem.controller;

import com.example.electricitybillingsystem.dto.BillDTO;
import com.example.electricitybillingsystem.entity.enums.BillStatus;
import com.example.electricitybillingsystem.service.BillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @GetMapping
    public ResponseEntity<List<BillDTO>> getAllBills() {
        return ResponseEntity.ok(billService.getAllBills());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillDTO> getBillById(@PathVariable Long id) {
        return ResponseEntity.ok(billService.getBillById(id));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<BillDTO>> getBillsByStatus(@PathVariable BillStatus status) {
        return ResponseEntity.ok(billService.getBillsByStatus(status));
    }

    @GetMapping("/month/{month}")
    public ResponseEntity<List<BillDTO>> getBillsByMonth(@PathVariable String month) {
        return ResponseEntity.ok(billService.getBillsByMonth(month));
    }

    @GetMapping("/search")
    public ResponseEntity<List<BillDTO>> searchBills(
            @RequestParam(required = false) String month,
            @RequestParam(required = false) BillStatus status,
            @RequestParam(required = false) Long consumerId,
            @RequestParam(required = false) String consumerNumber) {

        List<BillDTO> bills = billService.searchBills(month, status, consumerId, consumerNumber);
        return ResponseEntity.ok(bills);
    }
}