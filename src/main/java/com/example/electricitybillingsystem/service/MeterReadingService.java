package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.dto.MeterReadingDTO;
import com.example.electricitybillingsystem.dto.MeterReadingRequestDTO;
import com.example.electricitybillingsystem.entity.ElectricityConnection;
import com.example.electricitybillingsystem.entity.MeterReading;
import com.example.electricitybillingsystem.exception.ResourceNotFoundException;
import com.example.electricitybillingsystem.mapper.MeterReadingMapper;
import com.example.electricitybillingsystem.repository.ElectricityConnectionRepository;
import com.example.electricitybillingsystem.repository.MeterReadingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MeterReadingService {

    private final MeterReadingRepository meterReadingRepository;
    private final ElectricityConnectionRepository connectionRepository;
    private final BillService billService;
    private final MeterReadingMapper meterReadingMapper;

    public MeterReadingService(MeterReadingRepository meterReadingRepository,
                               ElectricityConnectionRepository connectionRepository,
                               BillService billService,
                               MeterReadingMapper meterReadingMapper) {
        this.meterReadingRepository = meterReadingRepository;
        this.connectionRepository = connectionRepository;
        this.billService = billService;
        this.meterReadingMapper = meterReadingMapper;
    }

    @Transactional
    public MeterReadingDTO saveMeterReading(MeterReadingRequestDTO requestDTO) {
        Long connectionId = requestDTO.getConnection() != null ? requestDTO.getConnection().getId() : null;
        log.info("Saving meter reading for connection ID: {}", connectionId);

        if (connectionId == null) {
            throw new IllegalArgumentException("Connection ID is required for meter reading.");
        }

        if (requestDTO.getCurrentReading().compareTo(requestDTO.getPreviousReading()) < 0) {
            throw new IllegalArgumentException("Current reading cannot be less than previous reading.");
        }

        ElectricityConnection connection = connectionRepository.findById(connectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Electricity Connection not found with id: " + connectionId));

        MeterReading meterReading = meterReadingMapper.toEntity(requestDTO, connection);
        meterReading.setUnitsConsumed(requestDTO.getCurrentReading().subtract(requestDTO.getPreviousReading()));

        MeterReading savedReading = meterReadingRepository.save(meterReading);

        log.info("Meter reading successfully saved. ID: {}, Units Consumed: {}",
                savedReading.getId(),
                savedReading.getUnitsConsumed());

        billService.generateBill(savedReading);

        return meterReadingMapper.toDTO(savedReading);
    }

    @Transactional(readOnly = true)
    public List<MeterReadingDTO> getAllMeterReadings() {
        return meterReadingRepository.findAll().stream()
                .map(meterReadingMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MeterReadingDTO getMeterReadingById(Long id) {
        return meterReadingRepository.findById(id)
                .map(meterReadingMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Meter Reading not found with id " + id));
    }

    @Transactional
    public MeterReadingDTO updateMeterReading(Long id, MeterReadingRequestDTO updatedRequestDto) {
        log.info("Updating meter reading ID: {}", id);

        MeterReading reading = meterReadingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meter Reading not found with id " + id));

        if (updatedRequestDto.getCurrentReading().compareTo(updatedRequestDto.getPreviousReading()) < 0) {
            throw new IllegalArgumentException("Current reading cannot be less than previous reading.");
        }

        ElectricityConnection connection = reading.getConnection();
        if (updatedRequestDto.getConnection() != null && updatedRequestDto.getConnection().getId() != null) {
            Long connId = updatedRequestDto.getConnection().getId();
            connection = connectionRepository.findById(connId)
                    .orElseThrow(() -> new ResourceNotFoundException("Electricity Connection not found with id: " + connId));
        }

        meterReadingMapper.updateEntity(reading, updatedRequestDto, connection);
        reading.setUnitsConsumed(updatedRequestDto.getCurrentReading().subtract(updatedRequestDto.getPreviousReading()));

        MeterReading savedReading = meterReadingRepository.save(reading);

        billService.generateBill(savedReading);

        log.info("Meter reading successfully updated. ID: {}", savedReading.getId());
        return meterReadingMapper.toDTO(savedReading);
    }

    @Transactional
    public void deleteMeterReading(Long id) {
        log.info("Deleting meter reading ID: {}", id);
        MeterReading reading = meterReadingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meter Reading not found with id " + id));

        meterReadingRepository.delete(reading);
        log.info("Successfully deleted meter reading ID: {}", id);
    }
}