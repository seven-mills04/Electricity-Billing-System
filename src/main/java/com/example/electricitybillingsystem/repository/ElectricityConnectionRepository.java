package com.example.electricitybillingsystem.repository;

import com.example.electricitybillingsystem.entity.ElectricityConnection;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ElectricityConnectionRepository extends JpaRepository<ElectricityConnection, Long> {

    @Override
    @EntityGraph(attributePaths = {"consumer"})
    List<ElectricityConnection> findAll();

    @Override
    @EntityGraph(attributePaths = {"consumer"})
    Optional<ElectricityConnection> findById(Long id);

    Optional<ElectricityConnection> findByConnectionNumber(String connectionNumber);
    Optional<ElectricityConnection> findByMeterNumber(String meterNumber);
}