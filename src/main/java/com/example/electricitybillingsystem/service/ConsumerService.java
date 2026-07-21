package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.dto.ConsumerCreateDTO;
import com.example.electricitybillingsystem.dto.ConsumerDTO;
import com.example.electricitybillingsystem.entity.Consumer;
import com.example.electricitybillingsystem.exception.DuplicateResourceException;
import com.example.electricitybillingsystem.exception.ResourceNotFoundException;
import com.example.electricitybillingsystem.mapper.ConsumerMapper;
import com.example.electricitybillingsystem.repository.ConsumerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ConsumerService {

    private final ConsumerRepository consumerRepository;
    private final ConsumerMapper consumerMapper;

    public ConsumerService(ConsumerRepository consumerRepository, ConsumerMapper consumerMapper) {
        this.consumerRepository = consumerRepository;
        this.consumerMapper = consumerMapper;
    }

    @Transactional
    public ConsumerDTO saveConsumer(ConsumerCreateDTO dto) {
        log.info("Saving new consumer: {} {}", dto.getFirstName(), dto.getLastName());
        
        if (dto.getConsumerNumber() != null && consumerRepository.existsByConsumerNumber(dto.getConsumerNumber())) {
            throw new DuplicateResourceException("Consumer number already exists: " + dto.getConsumerNumber());
        }

        Consumer consumer = consumerMapper.toEntity(dto);
        Consumer savedConsumer = consumerRepository.save(consumer);
        log.info("Successfully created consumer ID: {}", savedConsumer.getId());
        return consumerMapper.toDTO(savedConsumer);
    }

    @Transactional(readOnly = true)
    public Page<ConsumerDTO> getAllConsumers(Pageable pageable) {
        return consumerRepository.findAll(pageable)
                .map(consumerMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public List<ConsumerDTO> getAllConsumers() {
        return consumerRepository.findAll().stream()
                .map(consumerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ConsumerDTO getConsumerById(Long id) {
        return consumerRepository.findById(id)
                .map(consumerMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Consumer not found with id: " + id));
    }

    @Transactional
    public ConsumerDTO updateConsumer(Long id, ConsumerCreateDTO updatedConsumerDto) {
        log.info("Updating consumer ID: {}", id);
        Consumer existingConsumer = consumerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consumer not found with id: " + id));

        consumerMapper.updateEntity(existingConsumer, updatedConsumerDto);
        Consumer saved = consumerRepository.save(existingConsumer);
        log.info("Successfully updated consumer ID: {}", id);
        return consumerMapper.toDTO(saved);
    }

    @Transactional
    public void deleteConsumer(Long id) {
        log.info("Deleting consumer ID: {}", id);
        Consumer consumer = consumerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consumer doesn't exist with id: " + id));
        consumerRepository.delete(consumer);
        log.info("Successfully deleted consumer ID: {}", id);
    }

    @Transactional(readOnly = true)
    public List<ConsumerDTO> searchConsumers(String name, String phone, String meterNumber) {
        List<Consumer> consumers;
        if (name != null && !name.trim().isEmpty()) {
            consumers = consumerRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
        } else if (phone != null && !phone.trim().isEmpty()) {
            consumers = consumerRepository.findByPhone(phone);
        } else if (meterNumber != null && !meterNumber.trim().isEmpty()) {
            consumers = consumerRepository.findByMeterNumber(meterNumber);
        } else {
            consumers = consumerRepository.findAll();
        }
        return consumers.stream()
                .map(consumerMapper::toDTO)
                .collect(Collectors.toList());
    }
}