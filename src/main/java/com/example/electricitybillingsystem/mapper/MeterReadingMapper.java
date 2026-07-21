package com.example.electricitybillingsystem.mapper;

import com.example.electricitybillingsystem.dto.MeterReadingDTO;
import com.example.electricitybillingsystem.dto.MeterReadingRequestDTO;
import com.example.electricitybillingsystem.entity.ElectricityConnection;
import com.example.electricitybillingsystem.entity.MeterReading;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MeterReadingMapper {

    private final ElectricityConnectionMapper connectionMapper;

    public MeterReadingMapper(ElectricityConnectionMapper connectionMapper) {
        this.connectionMapper = connectionMapper;
    }

    public MeterReadingDTO toDTO(MeterReading entity) {
        if (entity == null) {
            return null;
        }

        return MeterReadingDTO.builder()
                .id(entity.getId())
                .readingDate(entity.getReadingDate())
                .previousReading(entity.getPreviousReading())
                .currentReading(entity.getCurrentReading())
                .unitsConsumed(entity.getUnitsConsumed())
                .remarks(entity.getRemarks())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .connection(connectionMapper.toDTO(entity.getConnection()))
                .build();
    }

    public MeterReading toEntity(MeterReadingRequestDTO dto, ElectricityConnection connection) {
        if (dto == null) {
            return null;
        }

        MeterReading entity = new MeterReading();
        entity.setReadingDate(dto.getReadingDate());
        entity.setPreviousReading(dto.getPreviousReading());
        entity.setCurrentReading(dto.getCurrentReading());
        entity.setRemarks(dto.getRemarks());
        entity.setConnection(connection);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }

    public void updateEntity(MeterReading entity, MeterReadingRequestDTO dto, ElectricityConnection connection) {
        if (entity == null || dto == null) {
            return;
        }

        if (dto.getReadingDate() != null) {
            entity.setReadingDate(dto.getReadingDate());
        }
        if (dto.getPreviousReading() != null) {
            entity.setPreviousReading(dto.getPreviousReading());
        }
        if (dto.getCurrentReading() != null) {
            entity.setCurrentReading(dto.getCurrentReading());
        }
        if (dto.getRemarks() != null) {
            entity.setRemarks(dto.getRemarks());
        }
        if (connection != null) {
            entity.setConnection(connection);
        }
        entity.setUpdatedAt(LocalDateTime.now());
    }
}
