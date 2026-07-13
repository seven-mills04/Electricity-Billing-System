package com.example.electricitybillingsystem.repository;

import com.example.electricitybillingsystem.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import java.math.BigDecimal;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT COALESCE(SUM(p.amountPaid), 0) FROM Payment p WHERE p.paymentDate = CURRENT_DATE")
    BigDecimal sumTodayCollection();

    @Query("SELECT COALESCE(SUM(p.amountPaid), 0) FROM Payment p WHERE YEAR(p.paymentDate) = YEAR(CURRENT_DATE) AND MONTH(p.paymentDate) = MONTH(CURRENT_DATE)")
    BigDecimal sumMonthlyRevenue();

    @Query("SELECT COALESCE(SUM(p.amountPaid), 0) FROM Payment p")
    BigDecimal sumTotalRevenue();
}