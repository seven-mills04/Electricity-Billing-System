package com.example.electricitybillingsystem.mapper;

import com.example.electricitybillingsystem.dto.UserDTO;
import com.example.electricitybillingsystem.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(User entity) {
        if (entity == null) {
            return null;
        }

        return UserDTO.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .role(entity.getRole())
                .consumerId(entity.getConsumer() != null ? entity.getConsumer().getId() : null)
                .build();
    }
}
