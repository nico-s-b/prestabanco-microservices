package com.example.calculation_service.controllers;

import com.example.calculation_service.dtos.CreditRestrictionsDTO;
import com.example.calculation_service.entities.TotalCosts;
import com.example.common_utils.dtos.CreditRequest;
import com.example.calculation_service.services.CalculateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/calculations")
public class CalculateController {
    @Autowired
    CalculateService calculateService;

    @PostMapping("/simulate")
    public ResponseEntity<List<Integer>> simulate(@RequestBody CreditRequest request) {
        if (request.getCreditType() == null) {
            return ResponseEntity.badRequest().body(null);
        }

        List<Integer> installments = calculateService.simulateCreditInstallments(request);
        return ResponseEntity.ok(installments);
    }

    @PostMapping("/total")
    public ResponseEntity<TotalCosts> total(@RequestBody CreditRequest request) {
        System.out.println(request);
        if (request.getCreditType() == null) {
            return ResponseEntity.badRequest().body(null);
        }
        TotalCosts totalcosts = calculateService.calculateAndSaveTotalCost(request);
        return ResponseEntity.ok(totalcosts);
    }

    @GetMapping("/installment")
    public ResponseEntity<Integer> getInstallment(@RequestBody CreditRequest request) {
        if (request.getCreditType() == null) {
            return ResponseEntity.badRequest().body(null);
        }
        int installment = calculateService.getCreditInstallment(request);
        return ResponseEntity.ok(installment);
    }

    @PostMapping("/restrictions")
    public ResponseEntity<CreditRestrictionsDTO> getRestrictions(@RequestBody CreditRequest request) {
        CreditRestrictionsDTO restrictions = calculateService.calculateRestrictions(request);
        return ResponseEntity.ok(restrictions);
    }

    @GetMapping("/maxFinancing")
    public ResponseEntity<Integer> getMaxFinancing(@RequestBody CreditRequest request) {
        if (request.getCreditType() == null) {
            return ResponseEntity.badRequest().body(null);
        }
        int maxF = calculateService.getMaxFinancingMount(request);
        return ResponseEntity.ok(maxF);
    }

    @GetMapping("/{creditId}")
    public ResponseEntity<TotalCosts> getTotalCostsByCreditId(@PathVariable Long creditId) {
        TotalCosts totals = calculateService.getTotalCostByCreditId(creditId);
        if (totals == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(totals);
    }
}
