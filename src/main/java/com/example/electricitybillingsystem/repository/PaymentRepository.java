package com.example.electricitybillingsystem.repository;

import com.example.electricitybillingsystem.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Override
    @EntityGraph(attributePaths = {"bill", "bill.meterReading", "bill.meterReading.connection", "bill.meterReading.connection.consumer"})
    List<Payment> findAll();

    @Override
    @EntityGraph(attributePaths = {"bill", "bill.meterReading", "bill.meterReading.connection", "bill.meterReading.connection.consumer"})
    Page<Payment> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"bill", "bill.meterReading", "bill.meterReading.connection", "bill.meterReading.connection.consumer"})
    Optional<Payment> findById(Long id);

    @Query("SELECT COALESCE(SUM(p.amountPaid), 0) FROM Payment p WHERE p.paymentDate = CURRENT_DATE")
    BigDecimal sumTodayCollection();

    @Query("SELECT COALESCE(SUM(p.amountPaid), 0) FROM Payment p WHERE YEAR(p.paymentDate) = YEAR(CURRENT_DATE) AND MONTH(p.paymentDate) = MONTH(CURRENT_DATE)")
    BigDecimal sumMonthlyRevenue();

    @Query("SELECT COALESCE(SUM(p.amountPaid), 0) FROM Payment p")
    BigDecimal sumTotalRevenue();
}