package com.example.evaluation_service.controllers;

import com.example.evaluation_service.dtos.EvaluationDTO;
import com.example.evaluation_service.entities.ClientEvaluation;
import com.example.evaluation_service.services.*;
import com.example.evaluation_service.utils.EvaluationMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/evals")
public class EvaluationController {

    private final ClientEvaluationService clientEvaluationService;

    public EvaluationController(ClientEvaluationService clientEvaluationService) {
        this.clientEvaluationService = clientEvaluationService;
    }

    @GetMapping("/{creditId}")
    public ResponseEntity<EvaluationDTO> getEvaluation(@PathVariable Long creditId) {
        ClientEvaluation clientEvaluation = clientEvaluationService.getByCreditId(creditId);
        EvaluationDTO evaluationDTO = EvaluationMapper.toDTO(clientEvaluation.getEvaluation());
        return ResponseEntity.ok(evaluationDTO);
    }

    @PutMapping("/{creditId}")
    public ResponseEntity<Void> updateEvaluation(
            @PathVariable Long creditId,
            @RequestBody EvaluationDTO evaluationDTO) {
        clientEvaluationService.update(creditId, evaluationDTO);
        return ResponseEntity.noContent().build();
    }

    //Se crea en conjunto con cada nuevo crédito
    @PostMapping
    public ResponseEntity<Void> createEvaluation(@RequestParam Long clientId, @RequestParam Long creditId) {
        clientEvaluationService.create(clientId, creditId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
