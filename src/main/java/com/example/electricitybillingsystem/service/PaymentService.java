package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.entity.Bill;
import com.example.electricitybillingsystem.entity.Payment;
import com.example.electricitybillingsystem.entity.enums.BillStatus;
import com.example.electricitybillingsystem.repository.BillRepository;
import com.example.electricitybillingsystem.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BillRepository billRepository;

    public Payment payBill(Long billId, Payment payment) {
        log.info("Processing payment for bill ID: {} with mode: {}", billId, payment.getPaymentMode());

        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new com.example.electricitybillingsystem.exception.ResourceNotFoundException("Bill not found"));

        if (bill.getBillStatus() == BillStatus.PAID) {
            throw new com.example.electricitybillingsystem.exception.BillAlreadyPaidException("Bill is already paid");
        }

        payment.setBill(bill);
        payment.setPaymentDate(LocalDate.now());
        payment.setAmountPaid(bill.getTotalAmount());
        payment.setTransactionId("TXN-" + UUID.randomUUID().toString().substring(0, 8));

        bill.setBillStatus(BillStatus.PAID);
        billRepository.save(bill);

        Payment savedPayment = paymentRepository.save(payment);
        log.info("Payment successfully processed. Transaction ID: {}, Amount Paid: {}", savedPayment.getTransactionId(), savedPayment.getAmountPaid());
        return savedPayment;
    }

    public Page<Payment> getAllPayments(Pageable pageable) {
        return paymentRepository.findAll(pageable);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}