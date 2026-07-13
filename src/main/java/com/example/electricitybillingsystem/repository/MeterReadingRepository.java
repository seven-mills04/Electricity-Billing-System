package com.example.electricitybillingsystem.repository;

import com.example.electricitybillingsystem.entity.MeterReading;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeterReadingRepository extends JpaRepository<MeterReading, Long> {
}