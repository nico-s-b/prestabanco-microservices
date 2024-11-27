package com.example.credit_service.controllers;

import com.example.credit_service.entities.Client;
import com.example.credit_service.entities.Credit;
import com.example.common_utils.enums.CreditType;
import com.example.credit_service.services.ClientService;
import com.example.credit_service.services.CreditService;
import com.example.credit_service.services.CreditValidationService;
import com.example.credit_service.dtos.CreditRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/credits")
public class CreditController {
    @Autowired
    CreditService creditService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private CreditValidationService creditValidationService;

    @GetMapping("/")
    public ResponseEntity<List<Credit>> list() {
        List<Credit> credits = creditService.getCredits();
        return ResponseEntity.ok(credits);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Credit> getById(@PathVariable Long id) {
        Credit credit = creditService.getCreditById(id);
        if (credit != null) {
            return ResponseEntity.ok(credit);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{clientId}/credits")
    public ResponseEntity<List<Credit>> getClientCredits(@PathVariable Long clientId) {
        List<Credit> credits = creditService.getCreditsById(clientId);
        if (credits.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(credits);
    }

    @PostMapping("/")
    public ResponseEntity<Credit> create(@RequestBody Credit credit) {
        Credit creditCreated = creditService.saveCredit(credit);
        return ResponseEntity.ok(creditCreated);
    }

    @PutMapping("/{creditId}")
    public ResponseEntity<Credit> update(@PathVariable Long creditId, @RequestBody Credit credit) {
        credit.setId(creditId);
        Credit creditUpdated = creditService.saveCredit(credit);
        return ResponseEntity.ok(creditUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) throws Exception {
        var isDeleted = creditService.deleteCredit(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/simulate")
    public ResponseEntity<List<Integer>> simulate(@RequestBody CreditRequest request) {
        if (request.getCreditType() == null) {
            return ResponseEntity.badRequest().body(null);
        }

        List<Integer> installments = creditService.simulateCreditInstallments(request);
        return ResponseEntity.ok(installments);
    }

    @PostMapping("/request")
    public ResponseEntity<Credit> request(@RequestBody CreditRequest request, @RequestParam Long clientId) {
        if (request.getCreditType() == null) {
            return ResponseEntity.badRequest().body(null);
        }

        Client client = clientService.getClientById(clientId);
        if (client == null) {
            return ResponseEntity.badRequest().body(null);
        }

        Credit creditCreated = creditService.createCredit(request, client);
        return ResponseEntity.ok(creditCreated);
    }

    @GetMapping("/docvalid")
    public ResponseEntity<Credit> documentValidation(@RequestParam Long id) {
        Credit credit = creditService.getCreditById(id);
        if (credit == null) {
            return ResponseEntity.badRequest().body(null);
        }
        Credit creditChecked = creditValidationService.documentRevision(credit);
        return ResponseEntity.ok(creditChecked);
    }

    @GetMapping("/cancel")
    public ResponseEntity<Credit> cancel(@RequestParam Long id) {
        Credit credit = creditService.getCreditById(id);
        if (credit == null) {
            return ResponseEntity.badRequest().body(null);
        }
        Credit canceledCredit = creditService.cancelCredit(credit);
        return ResponseEntity.ok(canceledCredit);
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
