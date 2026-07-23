package com.example.electricitybillingsystem.mapper;

import com.example.electricitybillingsystem.dto.ConsumerDTO;
import com.example.electricitybillingsystem.dto.ElectricityConnectionDTO;
import com.example.electricitybillingsystem.dto.ElectricityConnectionRequestDTO;
import com.example.electricitybillingsystem.entity.Consumer;
import com.example.electricitybillingsystem.entity.ElectricityConnection;
import org.springframework.stereotype.Component;

@Component
public class ElectricityConnectionMapper {

    public ElectricityConnectionDTO toDTO(ElectricityConnection entity) {
        if (entity == null) {
            return null;
        }

        Consumer consumer = entity.getConsumer();
        ConsumerDTO consumerDTO = null;
        if (consumer != null) {
            consumerDTO = ConsumerDTO.builder()
                    .id(consumer.getId())
                    .consumerNumber(consumer.getConsumerNumber())
                    .firstName(consumer.getFirstName())
                    .lastName(consumer.getLastName())
                    .email(consumer.getEmail())
                    .phone(consumer.getPhone())
                    .build();
        }

        return ElectricityConnectionDTO.builder()
                .id(entity.getId())
                .connectionNumber(entity.getConnectionNumber())
                .meterNumber(entity.getMeterNumber())
                .connectionType(entity.getConnectionType())
                .status(entity.getStatus())
                .sanctionedLoad(entity.getSanctionedLoad())
                .phaseType(entity.getPhaseType())
                .consumerId(consumer != null ? consumer.getId() : null)
                .consumer(consumerDTO)
                .build();
    }

    public ElectricityConnection toEntity(ElectricityConnectionRequestDTO dto, Consumer consumer) {
        if (dto == null) {
            return null;
        }

        ElectricityConnection entity = new ElectricityConnection();
        entity.setConnectionNumber(dto.getConnectionNumber());
        entity.setMeterNumber(dto.getMeterNumber());
        entity.setConnectionType(dto.getConnectionType());
        entity.setStatus(dto.getStatus());
        entity.setSanctionedLoad(dto.getSanctionedLoad());
        entity.setPhaseType(dto.getPhaseType());
        entity.setConsumer(consumer);
        return entity;
    }

    public void updateEntity(ElectricityConnection entity, ElectricityConnectionRequestDTO dto, Consumer consumer) {
        if (entity == null || dto == null) {
            return;
        }

        if (dto.getConnectionNumber() != null) {
            entity.setConnectionNumber(dto.getConnectionNumber());
        }
        if (dto.getMeterNumber() != null) {
            entity.setMeterNumber(dto.getMeterNumber());
        }
        if (dto.getConnectionType() != null) {
            entity.setConnectionType(dto.getConnectionType());
        }
        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }
        if (dto.getSanctionedLoad() != null) {
            entity.setSanctionedLoad(dto.getSanctionedLoad());
        }
        if (dto.getPhaseType() != null) {
            entity.setPhaseType(dto.getPhaseType());
        }
        if (consumer != null) {
            entity.setConsumer(consumer);
        }
    }
}
