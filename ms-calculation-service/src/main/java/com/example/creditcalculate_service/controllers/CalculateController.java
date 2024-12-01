package com.example.creditcalculate_service.controllers;

import com.example.common_utils.enums.CreditType;
import com.example.credit_service.dtos.CreditRequest;
import com.example.credit_service.entities.Credit;
import com.example.creditcalculate_service.services.CalculateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @PostMapping("/restrictions")
    public ResponseEntity<Map<String, Float>> getRestrictions(
            @RequestBody Map<String, Object> requestData) {

        String creditType = (String) requestData.get("creditType");
        int propertyValue = Integer.parseInt(requestData.get("propertyValue").toString());

        Credit credit = new Credit();
        credit.setCreditType(CreditType.valueOf(creditType));
        credit.setPropertyValue(propertyValue);

        Map<String, Float> restrictions = new HashMap<>();
        restrictions.put("maxLoanPeriod", (float) creditService.getMaxLoanPeriod(credit));
        restrictions.put("maxFinancingMount", (float) creditService.getMaxFinancingMount(credit));
        restrictions.put("minAnnualRate", creditService.getMinAnnualRate(credit));
        restrictions.put("maxAnnualRate", creditService.getMaxAnnualRate(credit));

        return ResponseEntity.ok(restrictions);
    }


}
