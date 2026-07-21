package com.example.electricitybillingsystem.controller;

import com.example.electricitybillingsystem.dto.ConsumerCreateDTO;
import com.example.electricitybillingsystem.dto.ConsumerDTO;
import com.example.electricitybillingsystem.service.ConsumerService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consumers")
public class ConsumerController {

    private final ConsumerService consumerService;

    public ConsumerController(ConsumerService consumerService) {
        this.consumerService = consumerService;
    }

    @PostMapping
    public ResponseEntity<ConsumerDTO> saveConsumer(@Valid @RequestBody ConsumerCreateDTO dto) {
        ConsumerDTO savedConsumer = consumerService.saveConsumer(dto);
        return new ResponseEntity<>(savedConsumer, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<ConsumerDTO>> getAllConsumers(Pageable pageable) {
        Page<ConsumerDTO> consumers = consumerService.getAllConsumers(pageable);
        return ResponseEntity.ok(consumers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsumerDTO> getConsumerById(@PathVariable Long id) {
        ConsumerDTO consumer = consumerService.getConsumerById(id);
        return ResponseEntity.ok(consumer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsumerDTO> updateConsumer(@PathVariable Long id,
                                                        @Valid @RequestBody ConsumerCreateDTO updatedConsumer) {
        ConsumerDTO consumer = consumerService.updateConsumer(id, updatedConsumer);
        return ResponseEntity.ok(consumer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteConsumer(@PathVariable Long id) {
        consumerService.deleteConsumer(id);
        return ResponseEntity.ok("Consumer Deleted Successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<List<ConsumerDTO>> searchConsumers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String meterNumber) {
        List<ConsumerDTO> consumers = consumerService.searchConsumers(name, phone, meterNumber);
        return ResponseEntity.ok(consumers);
    }
}
