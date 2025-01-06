package com.example.evaluation_service.controllers;

import com.example.common_utils.dtos.ClientDTO;
import com.example.common_utils.dtos.CreditRequest;
import com.example.evaluation_service.dtos.ClientAndCreditDTO;
import com.example.evaluation_service.dtos.EvaluationDTO;
import com.example.evaluation_service.entities.ClientEvaluation;
import com.example.evaluation_service.entities.ClientInformation;
import com.example.evaluation_service.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/evals")
public class EvaluationController {

    private final ClientEvaluationService clientEvaluationService;

    @Autowired
    ClientInformationService clientInformationService;

    public EvaluationController(ClientEvaluationService clientEvaluationService) {
        this.clientEvaluationService = clientEvaluationService;
    }

    @GetMapping("/{creditId}")
    public ResponseEntity<EvaluationDTO> getEvaluation(@PathVariable Long creditId) {
        System.out.println(creditId);
        ClientEvaluation clientEvaluation = clientEvaluationService.getByCreditId(creditId);
        System.out.println(clientEvaluation);
        EvaluationDTO evaluationDTO = new EvaluationDTO();
        evaluationDTO.setR1(clientEvaluation.getR1());
        evaluationDTO.setR2(clientEvaluation.getR2());
        evaluationDTO.setR3(clientEvaluation.getR3());
        evaluationDTO.setR4(clientEvaluation.getR4());
        evaluationDTO.setR5(clientEvaluation.getR5());
        evaluationDTO.setR6(clientEvaluation.getR6());
        evaluationDTO.setR7(clientEvaluation.getR7());
        evaluationDTO.setR7_1(clientEvaluation.getR7_1());
        evaluationDTO.setR7_2(clientEvaluation.getR7_2());
        evaluationDTO.setR7_3(clientEvaluation.getR7_3());
        evaluationDTO.setR7_4(clientEvaluation.getR7_4());
        evaluationDTO.setR7_5(clientEvaluation.getR7_5());
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
    public ResponseEntity<Void> create(@RequestParam Long creditId, @RequestParam Long clientId) {
        clientEvaluationService.create(creditId, clientId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/income/{clientId}")
    public ResponseEntity<Integer> getClientMonthlyIncome(@PathVariable Long clientId ) {
        ClientInformation clientInformation = clientInformationService.getByClientId(clientId);
        int income = clientEvaluationService.getClientMonthlyIncome(clientInformation);
        return ResponseEntity.ok(income);
    }

    @PostMapping("/age")
    public ResponseEntity<Integer> getClientAge(@RequestBody ClientAndCreditDTO data) {
        int age = clientEvaluationService.calculateAge(data.getClient(), data.getCredit());
        return ResponseEntity.ok(age);
    }
}
