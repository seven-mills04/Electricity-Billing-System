package com.example.electricitybillingsystem.repository;

import com.example.electricitybillingsystem.entity.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface ConsumerRepository extends JpaRepository<Consumer, Long> {

    List<Consumer> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);

    List<Consumer> findByPhone(String phone);

    @Query("SELECT c FROM Consumer c JOIN c.connections ec WHERE ec.meterNumber = :meterNumber")
    List<Consumer> findByMeterNumber(String meterNumber);
}