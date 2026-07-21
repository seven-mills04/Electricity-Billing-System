package com.example.electricitybillingsystem.repository;

import com.example.electricitybillingsystem.entity.Consumer;
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
public interface ConsumerRepository extends JpaRepository<Consumer, Long> {

    @Override
    @EntityGraph(attributePaths = {"connections"})
    List<Consumer> findAll();

    @Override
    @EntityGraph(attributePaths = {"connections"})
    Page<Consumer> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"connections"})
    Optional<Consumer> findById(Long id);

    @EntityGraph(attributePaths = {"connections"})
    List<Consumer> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName,
            String lastName
    );

    @EntityGraph(attributePaths = {"connections"})
    List<Consumer> findByPhone(String phone);

    @EntityGraph(attributePaths = {"connections"})
    @Query("SELECT c FROM Consumer c JOIN c.connections ec WHERE ec.meterNumber = :meterNumber")
    List<Consumer> findByMeterNumber(@Param("meterNumber") String meterNumber);

    Optional<Consumer> findByConsumerNumber(String consumerNumber);
    boolean existsByConsumerNumber(String consumerNumber);
    boolean existsByEmail(String email);
}