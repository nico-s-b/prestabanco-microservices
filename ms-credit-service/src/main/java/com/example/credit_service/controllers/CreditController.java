package com.example.credit_service.controllers;

import com.example.credit_service.entities.Credit;
import com.example.credit_service.services.CreditService;
import com.example.common_utils.dtos.CreditRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/credits")
public class CreditController {
    @Autowired
    CreditService creditService;

    @GetMapping("/")
    public ResponseEntity<List<Credit>> list() {
        List<Credit> credits = creditService.getAll();
        return ResponseEntity.ok(credits);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Credit> getById(@PathVariable Long id) {
        Credit credit = creditService.getById(id);
        return ResponseEntity.ok(credit);
    }

    @GetMapping("/{clientId}/credits")
    public ResponseEntity<List<Credit>> getClientCredits(@PathVariable Long clientId) {
        List<Credit> credits = creditService.getByClientId(clientId);
        if (credits.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(credits);
    }

    @PostMapping("/")
    public ResponseEntity<Credit> create(@RequestBody Credit credit) {
        Credit creditCreated = creditService.save(credit);
        return ResponseEntity.ok(creditCreated);
    }

    @PutMapping("/{creditId}")
    public ResponseEntity<Credit> update(@PathVariable Long creditId, @RequestBody Credit credit) {
        credit.setId(creditId);
        Credit creditUpdated = creditService.save(credit);
        return ResponseEntity.ok(creditUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) throws Exception {
        creditService.deleteCredit(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/request")
    public ResponseEntity<Credit> request(@RequestBody CreditRequest request, @RequestParam Long clientId) {
        if (request.getCreditType() == null) {
            return ResponseEntity.badRequest().body(null);
        }

        Credit creditCreated = creditService.create(request, clientId);
        return ResponseEntity.ok(creditCreated);
    }

}
