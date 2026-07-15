package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.entity.ElectricityConnection;
import com.example.electricitybillingsystem.repository.ElectricityConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ElectricityConnectionService {

    @Autowired
    private ElectricityConnectionRepository electricityConnectionRepository;


    public ElectricityConnection saveElectricityConnection(ElectricityConnection electricityConnection) {
        return electricityConnectionRepository.save(electricityConnection);
    }


    public List<ElectricityConnection> getAllConnections() {
        return electricityConnectionRepository.findAll();
    }


    public ElectricityConnection getConnectionById(Long id) {
        return electricityConnectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Connection not found with id " + id));
    }

    public ElectricityConnection updateConnection(Long id, ElectricityConnection updatedConnection) {

        ElectricityConnection connection = getConnectionById(id);

        connection.setConnectionNumber(updatedConnection.getConnectionNumber());
        connection.setMeterNumber(updatedConnection.getMeterNumber());
        connection.setConnectionType(updatedConnection.getConnectionType());
        connection.setStatus(updatedConnection.getStatus());
        connection.setSanctionedLoad(updatedConnection.getSanctionedLoad());
        connection.setPhaseType(updatedConnection.getPhaseType());
        connection.setConsumer(updatedConnection.getConsumer());

        return electricityConnectionRepository.save(connection);
    }


    public void deleteConnection(Long id) {

        ElectricityConnection connection = getConnectionById(id);

        electricityConnectionRepository.delete(connection);
    }
}