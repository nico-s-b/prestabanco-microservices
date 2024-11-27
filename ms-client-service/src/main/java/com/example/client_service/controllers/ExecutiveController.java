package com.example.client_service.controllers;

import com.example.client_service.entities.Executive;
import com.example.client_service.services.ExecutiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/executives")
public class ExecutiveController {
    @Autowired
    ExecutiveService executiveService;

    @GetMapping("/")
    public ResponseEntity<List<Executive>> list() {
        List<Executive> executives = executiveService.getAllExecutives();
        return ResponseEntity.ok(executives);
    }

    @PostMapping("/")
    public ResponseEntity<Executive> register(@RequestBody Executive executive) {
        Executive savedExecutive = executiveService.saveExecutive(executive);
        return ResponseEntity.ok(savedExecutive);
    }

    @PutMapping("/{executiveId}")
    public ResponseEntity<Executive> update(@PathVariable Long executiveId, @RequestBody Executive executive) {
        executive.setId(executiveId);
        Executive executiveUpdated = executiveService.saveExecutive(executive);
        return ResponseEntity.ok(executiveUpdated);
    }

    @GetMapping("/{executiveId}")
    public ResponseEntity<Executive> getById(@PathVariable Long executiveId) {
        Executive executive = executiveService.getExecutiveById(executiveId);
        if (executive != null) {
            return ResponseEntity.ok(executive);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}