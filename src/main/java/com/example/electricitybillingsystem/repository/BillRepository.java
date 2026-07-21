package com.example.electricitybillingsystem.repository;

import com.example.electricitybillingsystem.entity.Bill;
import com.example.electricitybillingsystem.entity.enums.BillStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    @Override
    @EntityGraph(attributePaths = {"meterReading", "meterReading.connection", "meterReading.connection.consumer"})
    List<Bill> findAll();

    @Override
    @EntityGraph(attributePaths = {"meterReading", "meterReading.connection", "meterReading.connection.consumer"})
    Page<Bill> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"meterReading", "meterReading.connection", "meterReading.connection.consumer"})
    Optional<Bill> findById(Long id);

    @EntityGraph(attributePaths = {"meterReading", "meterReading.connection", "meterReading.connection.consumer"})
    List<Bill> findByBillStatus(BillStatus billStatus);

    @EntityGraph(attributePaths = {"meterReading", "meterReading.connection", "meterReading.connection.consumer"})
    List<Bill> findByBillingMonth(String billingMonth);

    long countByBillStatus(BillStatus billStatus);

    @EntityGraph(attributePaths = {"meterReading", "meterReading.connection", "meterReading.connection.consumer"})
    Optional<Bill> findByMeterReadingId(Long meterReadingId);

    @EntityGraph(attributePaths = {"meterReading", "meterReading.connection", "meterReading.connection.consumer"})
    @Query("SELECT b FROM Bill b WHERE b.meterReading.connection.consumer.id = :consumerId")
    List<Bill> findByConsumerId(@Param("consumerId") Long consumerId);

    @EntityGraph(attributePaths = {"meterReading", "meterReading.connection", "meterReading.connection.consumer"})
    @Query("SELECT b FROM Bill b WHERE b.meterReading.connection.consumer.consumerNumber = :consumerNumber")
    List<Bill> findByConsumerNumber(@Param("consumerNumber") String consumerNumber);
}