
package com.example.electricitybillingsystem.repository;

import com.example.electricitybillingsystem.entity.ElectricityConnection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ElectricityConnectionRepository extends JpaRepository<ElectricityConnection, Long> {
}