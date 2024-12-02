package com.example.calculation_service.controllers;

import com.example.calculation_service.dtos.CreditRestrictionsDTO;
import com.example.calculation_service.entities.TotalCosts;
import com.example.common_utils.dtos.CreditRequest;
import com.example.calculation_service.services.CalculateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        if (request.getCreditType() == null) {
            return ResponseEntity.badRequest().body(null);
        }
        TotalCosts totalcosts = calculateService.calculateAndSaveTotalCost(request);
        return ResponseEntity.ok(totalcosts);
    }

    @PostMapping("/restrictions")
    public ResponseEntity<CreditRestrictionsDTO> getRestrictions(@RequestBody CreditRequest request) {
        CreditRestrictionsDTO restrictions = calculateService.calculateRestrictions(request);
        return ResponseEntity.ok(restrictions);
    }


}
