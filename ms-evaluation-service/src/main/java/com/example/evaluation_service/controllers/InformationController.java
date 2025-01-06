package com.example.evaluation_service.controllers;

import com.example.evaluation_service.dtos.ClientInfoDTO;
import com.example.evaluation_service.entities.ClientInformation;
import com.example.evaluation_service.services.ClientInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/info")
public class InformationController {
    @Autowired
    ClientInformationService clientInformationService;

    @GetMapping("/{clientId}")
    public ResponseEntity<ClientInformation> get(@PathVariable Long clientId) {
        ClientInformation clientInfo = clientInformationService.getByClientId(clientId);
        return ResponseEntity.ok(clientInfo);
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestParam Long clientId){
        clientInformationService.create(clientId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/save")
    public ResponseEntity<Void> saveOrUpdate(@RequestBody ClientInfoDTO clientInfo) {
        clientInformationService.saveOrUpdate(clientInfo);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{clientId}")
    public ResponseEntity<Void> delete(@PathVariable Long clientId) {
        clientInformationService.delete(clientId);
        return ResponseEntity.ok().build();
    }

}
