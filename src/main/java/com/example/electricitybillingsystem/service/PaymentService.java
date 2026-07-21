package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.dto.PaymentDTO;
import com.example.electricitybillingsystem.dto.PaymentRequestDTO;
import com.example.electricitybillingsystem.entity.Bill;
import com.example.electricitybillingsystem.entity.Payment;
import com.example.electricitybillingsystem.entity.enums.BillStatus;
import com.example.electricitybillingsystem.exception.BillAlreadyPaidException;
import com.example.electricitybillingsystem.exception.ResourceNotFoundException;
import com.example.electricitybillingsystem.mapper.PaymentMapper;
import com.example.electricitybillingsystem.repository.BillRepository;
import com.example.electricitybillingsystem.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BillRepository billRepository;
    private final PaymentMapper paymentMapper;

    public PaymentService(PaymentRepository paymentRepository,
                          BillRepository billRepository,
                          PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.billRepository = billRepository;
        this.paymentMapper = paymentMapper;
    }

    @Transactional
    public PaymentDTO payBill(Long billId, PaymentRequestDTO request) {
        log.info("Processing payment for bill ID: {} with mode: {}", billId, request.getPaymentMode());

        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with id: " + billId));

        if (bill.getBillStatus() == BillStatus.PAID) {
            throw new BillAlreadyPaidException("Bill is already paid");
        }

        Payment payment = new Payment();
        payment.setBill(bill);
        payment.setPaymentMode(request.getPaymentMode());
        payment.setPaymentDate(LocalDate.now());
        payment.setAmountPaid(bill.getTotalAmount());
        payment.setTransactionId("TXN-" + UUID.randomUUID().toString().substring(0, 8));

        bill.setBillStatus(BillStatus.PAID);
        billRepository.save(bill);

        Payment savedPayment = paymentRepository.save(payment);
        log.info("Payment successfully processed. Transaction ID: {}, Amount Paid: {}",
                savedPayment.getTransactionId(), savedPayment.getAmountPaid());

        return paymentMapper.toDTO(savedPayment);
    }

    @Transactional(readOnly = true)
    public Page<PaymentDTO> getAllPayments(Pageable pageable) {
        return paymentRepository.findAll(pageable)
                .map(paymentMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public List<PaymentDTO> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(paymentMapper::toDTO)
                .collect(Collectors.toList());
    }
}