package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.entity.Consumer;
import com.example.electricitybillingsystem.repository.ConsumerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.example.electricitybillingsystem.exception.ResourceNotFoundException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class ConsumerService {

    private final ConsumerRepository consumerRepository;

    public ConsumerService(ConsumerRepository consumerRepository) {
        this.consumerRepository = consumerRepository;
    }

    public Consumer saveConsumer(Consumer consumer) {
        return consumerRepository.save(consumer);
    }

    public Page<Consumer> getAllConsumers(Pageable pageable) {
        return consumerRepository.findAll(pageable);
    }

    public List<Consumer> getAllConsumers() {
        return consumerRepository.findAll();
    }

    public Consumer getConsumerById(Long id){
        return consumerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consumer not found with id: " + id));
    }

    public Consumer updateConsumer(Long id,Consumer updatedConsumer){
        Consumer existingConsumer = getConsumerById(id);
        existingConsumer.setConsumerNumber(updatedConsumer.getConsumerNumber());
        existingConsumer.setFirstName(updatedConsumer.getFirstName());
        existingConsumer.setLastName(updatedConsumer.getLastName());
        existingConsumer.setEmail(updatedConsumer.getEmail());
        existingConsumer.setPhone(updatedConsumer.getPhone());

        consumerRepository.save(existingConsumer);

        return existingConsumer;
    }

    public void deleteConsumer(Long id){
        Consumer consumer = consumerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consumer doesn't exist: " + id));
        consumerRepository.deleteById(id);
    }

    public List<Consumer> searchConsumers(String name, String phone, String meterNumber) {
        if (name != null && !name.trim().isEmpty()) {
            return consumerRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
        } else if (phone != null && !phone.trim().isEmpty()) {
            return consumerRepository.findByPhone(phone);
        } else if (meterNumber != null && !meterNumber.trim().isEmpty()) {
            return consumerRepository.findByMeterNumber(meterNumber);
        }
        return consumerRepository.findAll();
    }
}