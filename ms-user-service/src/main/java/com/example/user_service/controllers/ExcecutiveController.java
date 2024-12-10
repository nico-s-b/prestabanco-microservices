package com.example.user_service.controllers;

import com.example.user_service.entities.Excecutive;
import com.example.user_service.services.ExcecutiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/executives")
public class ExcecutiveController {
    @Autowired
    ExcecutiveService excecutiveService;

    @GetMapping("/")
    public ResponseEntity<List<Excecutive>> list() {
        List<Excecutive> excecutives = excecutiveService.getAll();
        return ResponseEntity.ok(excecutives);
    }

    @PostMapping("/")
    public ResponseEntity<Excecutive> register(@RequestBody Excecutive excecutive) {
        Excecutive savedExcecutive = excecutiveService.save(excecutive);
        return ResponseEntity.ok(savedExcecutive);
    }

    @PutMapping("/{executiveId}")
    public ResponseEntity<Excecutive> update(@PathVariable Long executiveId, @RequestBody Excecutive excecutive) {
        excecutive.setId(executiveId);
        Excecutive excecutiveUpdated = excecutiveService.save(excecutive);
        return ResponseEntity.ok(excecutiveUpdated);
    }

    @GetMapping("/{executiveId}")
    public ResponseEntity<Excecutive> getById(@PathVariable Long executiveId) {
        Excecutive excecutive = excecutiveService.getById(executiveId);
        if (excecutive != null) {
            return ResponseEntity.ok(excecutive);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/assign")
    public ResponseEntity<Long> getExecutiveToAssign() {
        Long execId = excecutiveService.pickExecutive();
        return ResponseEntity.ok(execId);
    }
}