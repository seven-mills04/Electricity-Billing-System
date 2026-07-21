package com.example.electricitybillingsystem.mapper;

import com.example.electricitybillingsystem.dto.ConsumerCreateDTO;
import com.example.electricitybillingsystem.dto.ConsumerDTO;
import com.example.electricitybillingsystem.entity.Consumer;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class ConsumerMapper {

    private final ElectricityConnectionMapper connectionMapper;

    public ConsumerMapper(ElectricityConnectionMapper connectionMapper) {
        this.connectionMapper = connectionMapper;
    }

    public ConsumerDTO toDTO(Consumer entity) {
        if (entity == null) {
            return null;
        }

        return ConsumerDTO.builder()
                .id(entity.getId())
                .consumerNumber(entity.getConsumerNumber())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .connections(entity.getConnections() != null
                        ? entity.getConnections().stream()
                        .map(connectionMapper::toDTO)
                        .collect(Collectors.toList())
                        : Collections.emptyList())
                .build();
    }

    public Consumer toEntity(ConsumerCreateDTO dto) {
        if (dto == null) {
            return null;
        }

        Consumer entity = new Consumer();
        entity.setConsumerNumber(dto.getConsumerNumber());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        return entity;
    }

    public void updateEntity(Consumer entity, ConsumerCreateDTO dto) {
        if (entity == null || dto == null) {
            return;
        }

        if (dto.getConsumerNumber() != null) {
            entity.setConsumerNumber(dto.getConsumerNumber());
        }
        if (dto.getFirstName() != null) {
            entity.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            entity.setLastName(dto.getLastName());
        }
        if (dto.getEmail() != null) {
            entity.setEmail(dto.getEmail());
        }
        if (dto.getPhone() != null) {
            entity.setPhone(dto.getPhone());
        }
    }
}
