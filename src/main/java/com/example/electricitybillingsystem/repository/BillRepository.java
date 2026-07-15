package com.example.electricitybillingsystem.repository;

import com.example.electricitybillingsystem.entity.Bill;
import com.example.electricitybillingsystem.entity.enums.BillStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

public interface BillRepository extends JpaRepository<Bill, Long> {

    List<Bill> findByBillStatus(BillStatus billStatus);

    List<Bill> findByBillingMonth(String billingMonth);

    long countByBillStatus(BillStatus billStatus);
    Optional<Bill> findByMeterReadingId(Long meterReadingId);
    @Query("SELECT b FROM Bill b WHERE b.meterReading.connection.consumer.id = :consumerId")
    List<Bill> findByConsumerId(Long consumerId);

    @Query("SELECT b FROM Bill b WHERE b.meterReading.connection.consumer.consumerNumber = :consumerNumber")
    List<Bill> findByConsumerNumber(String consumerNumber);
}