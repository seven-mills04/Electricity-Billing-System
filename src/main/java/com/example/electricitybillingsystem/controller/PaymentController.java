package com.example.electricitybillingsystem.controller;

import com.example.electricitybillingsystem.dto.PaymentDTO;
import com.example.electricitybillingsystem.dto.PaymentRequestDTO;
import com.example.electricitybillingsystem.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/pay/{billId}")
    public ResponseEntity<PaymentDTO> payBill(@PathVariable Long billId,
                                              @Valid @RequestBody PaymentRequestDTO payment) {

        PaymentDTO savedPayment = paymentService.payBill(billId, payment);
        return ResponseEntity.ok(savedPayment);
    }

    @GetMapping
    public ResponseEntity<Page<PaymentDTO>> getAllPayments(Pageable pageable) {
        return ResponseEntity.ok(paymentService.getAllPayments(pageable));
    }

    @GetMapping("/all")
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }
}