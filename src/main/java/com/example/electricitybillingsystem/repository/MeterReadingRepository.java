package com.example.electricitybillingsystem.repository;

import com.example.electricitybillingsystem.entity.MeterReading;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeterReadingRepository extends JpaRepository<MeterReading, Long> {

    @Override
    @EntityGraph(attributePaths = {"connection", "connection.consumer"})
    List<MeterReading> findAll();

    @Override
    @EntityGraph(attributePaths = {"connection", "connection.consumer"})
    Optional<MeterReading> findById(Long id);
}