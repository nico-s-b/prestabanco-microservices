package com.example.user_service.controllers;

import com.example.user_service.entities.Executive;
import com.example.user_service.services.ExecutiveService;
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
        List<Executive> executives = executiveService.getAll();
        return ResponseEntity.ok(executives);
    }

    @PostMapping("/")
    public ResponseEntity<Executive> register(@RequestBody Executive executive) {
        Executive savedExecutive = executiveService.save(executive);
        return ResponseEntity.ok(savedExecutive);
    }

    @PutMapping("/{executiveId}")
    public ResponseEntity<Executive> update(@PathVariable Long executiveId, @RequestBody Executive executive) {
        executive.setId(executiveId);
        Executive executiveUpdated = executiveService.save(executive);
        return ResponseEntity.ok(executiveUpdated);
    }

    @GetMapping("/{executiveId}")
    public ResponseEntity<Executive> getById(@PathVariable Long executiveId) {
        Executive executive = executiveService.getById(executiveId);
        if (executive != null) {
            return ResponseEntity.ok(executive);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/assign")
    public ResponseEntity<Long> getExecutiveToAssign() {
        Long execId = executiveService.pickExecutive();
        return ResponseEntity.ok(execId);
    }
}