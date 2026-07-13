package com.example.electricitybillingsystem.controller;

import com.example.electricitybillingsystem.entity.Payment;
import com.example.electricitybillingsystem.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/pay/{billId}")
    public Payment payBill(@PathVariable Long billId,
                           @Valid @RequestBody Payment payment) {

        return paymentService.payBill(billId, payment);
    }

    @GetMapping
    public Page<Payment> getAllPayments(Pageable pageable) {
        return paymentService.getAllPayments(pageable);
    }

    @GetMapping("/all")
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }
}