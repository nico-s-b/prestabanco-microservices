package com.example.evaluation_service.controllers;

import com.example.evaluation_service.services.ClientAccountService;
import com.example.evaluation_service.services.CreditRecordService;
import com.example.evaluation_service.services.EmploymentRecordService;
import com.example.evaluation_service.services.PersonalInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/evals")
public class EvaluationController {

    private final ClientAccountService clientAccountService;
    private final CreditRecordService creditRecordService;
    private final EmploymentRecordService employmentRecordService;
    private final PersonalInformationService personalInformationService;

    public EvaluationController(ClientAccountService clientAccountService,
                                CreditRecordService creditRecordService,
                                EmploymentRecordService employmentRecordService) {
        this.clientAccountService = clientAccountService;
        this.creditRecordService = creditRecordService;
        this.employmentRecordService = employmentRecordService;
        this.personalInformationService = new PersonalInformationService();
    }

    @PostMapping
    public ResponseEntity<String> createRecords(@RequestParam Long id) {
        clientAccountService.create(id);
        creditRecordService.create(id);
        employmentRecordService.create(id);
        return ResponseEntity.ok("Records created");
    }

}
