
package com.example.electricitybillingsystem.controller;

import com.example.electricitybillingsystem.entity.Consumer;
import com.example.electricitybillingsystem.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/consumers")
public class ConsumerController {

    @Autowired
    private ConsumerService consumerService;

    @PostMapping
    public ResponseEntity<Consumer> saveConsumer(@Valid @RequestBody Consumer consumer) {

        Consumer savedConsumer = consumerService.saveConsumer(consumer);

        return new ResponseEntity<>(savedConsumer, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<Consumer>> getAllConsumers(Pageable pageable) {
        Page<Consumer> consumers = consumerService.getAllConsumers(pageable);
        return new ResponseEntity<>(consumers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Consumer> getConsumerById(@PathVariable Long id){
        Consumer consumer = consumerService.getConsumerById(id);
        return new ResponseEntity<>(consumer, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Consumer> updateConsumer(@PathVariable Long id,
                                                   @Valid @RequestBody Consumer updatedConsumer){
        Consumer consumer = consumerService.updateConsumer(id, updatedConsumer);
        return new ResponseEntity<>(consumer, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteConsumer(@PathVariable Long id){
        consumerService.deleteConsumer(id);
        return new ResponseEntity<>("Consumer Deleted Successfully",HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Consumer>> searchConsumers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String meterNumber) {
        List<Consumer> consumers = consumerService.searchConsumers(name, phone, meterNumber);
        return new ResponseEntity<>(consumers, HttpStatus.OK);
    }
}
