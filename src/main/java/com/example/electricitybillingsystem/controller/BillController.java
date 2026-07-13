package com.example.electricitybillingsystem.controller;

import com.example.electricitybillingsystem.entity.Bill;
import com.example.electricitybillingsystem.entity.enums.BillStatus;
import com.example.electricitybillingsystem.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/bill")
public class BillController {

    @Autowired
    private BillService billService;

    @GetMapping
    public Page<Bill> getAllBills(Pageable pageable) {
        return billService.getAllBills(pageable);
    }

    @GetMapping("/all")
    public List<Bill> getAllBills() {
        return billService.getAllBills();
    }


    @GetMapping("/{id}")
    public Bill getBillById(@PathVariable Long id) {
        return billService.getBillById(id);
    }

    @GetMapping("/status/{status}")
    public List<Bill> getBillsByStatus(@PathVariable BillStatus status) {
        return billService.getBillsByStatus(status);
    }

    @GetMapping("/month/{month}")
    public List<Bill> getBillsByMonth(@PathVariable String month) {
        return billService.getBillsByMonth(month);
    }

    @GetMapping("/search")
    public List<Bill> searchBills(
            @RequestParam(required = false) String month,
            @RequestParam(required = false) BillStatus status,
            @RequestParam(required = false) Long consumerId,
            @RequestParam(required = false) String consumerNumber) {
        return billService.searchBills(month, status, consumerId, consumerNumber);
    }
}