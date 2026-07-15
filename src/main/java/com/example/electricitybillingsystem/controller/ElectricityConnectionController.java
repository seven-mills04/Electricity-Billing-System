package com.example.electricitybillingsystem.controller;

import com.example.electricitybillingsystem.entity.ElectricityConnection;
import com.example.electricitybillingsystem.service.ElectricityConnectionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/connections")
public class ElectricityConnectionController {

    @Autowired
    private ElectricityConnectionService electricityConnectionService;


    @PostMapping
    public ResponseEntity<ElectricityConnection> saveConnection(
            @Valid @RequestBody ElectricityConnection connection) {

        ElectricityConnection savedConnection =
                electricityConnectionService.saveElectricityConnection(connection);

        return new ResponseEntity<>(savedConnection, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<ElectricityConnection>> getAllConnections() {

        List<ElectricityConnection> connections =
                electricityConnectionService.getAllConnections();

        return new ResponseEntity<>(connections, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ElectricityConnection> getConnectionById(
            @PathVariable Long id) {

        ElectricityConnection connection =
                electricityConnectionService.getConnectionById(id);

        return new ResponseEntity<>(connection, HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ElectricityConnection> updateConnection(
            @PathVariable Long id,
            @Valid @RequestBody ElectricityConnection updatedConnection) {

        ElectricityConnection connection =
                electricityConnectionService.updateConnection(id, updatedConnection);

        return new ResponseEntity<>(connection, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteConnection(
            @PathVariable Long id) {

        electricityConnectionService.deleteConnection(id);

        return new ResponseEntity<>("Connection deleted successfully", HttpStatus.OK);
    }
}