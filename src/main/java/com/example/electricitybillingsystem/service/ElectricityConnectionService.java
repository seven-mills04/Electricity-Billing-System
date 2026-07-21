package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.dto.ElectricityConnectionDTO;
import com.example.electricitybillingsystem.dto.ElectricityConnectionRequestDTO;
import com.example.electricitybillingsystem.entity.Consumer;
import com.example.electricitybillingsystem.entity.ElectricityConnection;
import com.example.electricitybillingsystem.exception.ResourceNotFoundException;
import com.example.electricitybillingsystem.mapper.ElectricityConnectionMapper;
import com.example.electricitybillingsystem.repository.ConsumerRepository;
import com.example.electricitybillingsystem.repository.ElectricityConnectionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ElectricityConnectionService {

    private final ElectricityConnectionRepository electricityConnectionRepository;
    private final ConsumerRepository consumerRepository;
    private final ElectricityConnectionMapper connectionMapper;

    public ElectricityConnectionService(ElectricityConnectionRepository electricityConnectionRepository,
                                        ConsumerRepository consumerRepository,
                                        ElectricityConnectionMapper connectionMapper) {
        this.electricityConnectionRepository = electricityConnectionRepository;
        this.consumerRepository = consumerRepository;
        this.connectionMapper = connectionMapper;
    }

    @Transactional
    public ElectricityConnectionDTO saveElectricityConnection(ElectricityConnectionRequestDTO requestDTO) {
        log.info("Saving new electricity connection: {}", requestDTO.getConnectionNumber());

        Consumer consumer = null;
        if (requestDTO.getConsumerId() != null) {
            consumer = consumerRepository.findById(requestDTO.getConsumerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Consumer not found with id: " + requestDTO.getConsumerId()));
        }

        ElectricityConnection connection = connectionMapper.toEntity(requestDTO, consumer);
        ElectricityConnection saved = electricityConnectionRepository.save(connection);
        log.info("Successfully created connection ID: {}", saved.getId());
        return connectionMapper.toDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<ElectricityConnectionDTO> getAllConnections() {
        return electricityConnectionRepository.findAll().stream()
                .map(connectionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ElectricityConnectionDTO getConnectionById(Long id) {
        return electricityConnectionRepository.findById(id)
                .map(connectionMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Connection not found with id: " + id));
    }

    @Transactional
    public ElectricityConnectionDTO updateConnection(Long id, ElectricityConnectionRequestDTO updatedDTO) {
        log.info("Updating electricity connection ID: {}", id);

        ElectricityConnection existing = electricityConnectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Connection not found with id: " + id));

        Consumer consumer = null;
        if (updatedDTO.getConsumerId() != null) {
            consumer = consumerRepository.findById(updatedDTO.getConsumerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Consumer not found with id: " + updatedDTO.getConsumerId()));
        }

        connectionMapper.updateEntity(existing, updatedDTO, consumer);
        ElectricityConnection saved = electricityConnectionRepository.save(existing);
        log.info("Successfully updated connection ID: {}", id);
        return connectionMapper.toDTO(saved);
    }

    @Transactional
    public void deleteConnection(Long id) {
        log.info("Deleting electricity connection ID: {}", id);
        ElectricityConnection connection = electricityConnectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Connection not found with id: " + id));
        electricityConnectionRepository.delete(connection);
        log.info("Successfully deleted connection ID: {}", id);
    }
}