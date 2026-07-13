package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.entity.ElectricityConnection;
import com.example.electricitybillingsystem.repository.ElectricityConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElectricityConnectionService {

    @Autowired
    private ElectricityConnectionRepository electricityConnectionRepository;

    public ElectricityConnection saveElectricityConnection(ElectricityConnection electricityConnection) {
        return electricityConnectionRepository.save(electricityConnection);
    }
}
