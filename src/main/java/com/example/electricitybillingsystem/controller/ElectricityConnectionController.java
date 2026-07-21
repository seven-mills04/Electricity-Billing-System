package com.example.electricitybillingsystem.controller;

import com.example.electricitybillingsystem.dto.ElectricityConnectionDTO;
import com.example.electricitybillingsystem.dto.ElectricityConnectionRequestDTO;
import com.example.electricitybillingsystem.service.ElectricityConnectionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/connections")
public class ElectricityConnectionController {

    private final ElectricityConnectionService electricityConnectionService;

    public ElectricityConnectionController(ElectricityConnectionService electricityConnectionService) {
        this.electricityConnectionService = electricityConnectionService;
    }

    @PostMapping
    public ResponseEntity<ElectricityConnectionDTO> saveConnection(
            @Valid @RequestBody ElectricityConnectionRequestDTO connection) {

        ElectricityConnectionDTO savedConnection =
                electricityConnectionService.saveElectricityConnection(connection);

        return new ResponseEntity<>(savedConnection, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ElectricityConnectionDTO>> getAllConnections() {

        List<ElectricityConnectionDTO> connections =
                electricityConnectionService.getAllConnections();

        return ResponseEntity.ok(connections);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ElectricityConnectionDTO> getConnectionById(@PathVariable Long id) {

        ElectricityConnectionDTO connection =
                electricityConnectionService.getConnectionById(id);

        return ResponseEntity.ok(connection);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ElectricityConnectionDTO> updateConnection(
            @PathVariable Long id,
            @Valid @RequestBody ElectricityConnectionRequestDTO updatedConnection) {

        ElectricityConnectionDTO connection =
                electricityConnectionService.updateConnection(id, updatedConnection);

        return ResponseEntity.ok(connection);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteConnection(@PathVariable Long id) {

        electricityConnectionService.deleteConnection(id);

        return ResponseEntity.ok("Connection deleted successfully");
    }
}