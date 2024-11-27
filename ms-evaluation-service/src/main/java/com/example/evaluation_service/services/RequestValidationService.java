package com.example.evaluation_service.services;

import com.example.evaluation_service.clients.CreditRequestFeignClient;
import com.example.evaluation_service.entities.Client;
import com.example.evaluation_service.entities.CreditRequest;
import com.example.common_utils.enums.CreditRequestState;
import com.example.common_utils.enums.DocumentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

@Service
public class RequestValidationService {
    @Autowired
    DocumentService documentService;

    @Autowired
    ClientService clientService;

    @Autowired
    CreditRequestFeignClient creditFeignClient;

    public boolean verifyMaxFinancingMount(CreditRequest credit){
        if (credit.getCreditRequestType() == null) {
            throw new IllegalStateException("CreditRequestType cannot be null");
        }

        int creditMount = credit.getCreditRequestMount();
        int propertyValue = credit.getPropertyValue();
        int financingPercentage = (creditMount / propertyValue)*100;

        switch (credit.getCreditRequestType()){
            case FIRSTHOME -> {
                if (financingPercentage < 80) return true;
            }
            case SECONDHOME -> {
                if (financingPercentage < 70) return true;
            }
            case COMERCIAL -> {
                if (financingPercentage < 60) return true;
            }
            case REMODELING -> {
                if (financingPercentage < 50) return true;
            }
            default -> throw new IllegalStateException("Unexpected value: " + credit.getCreditRequestType());
        }
        return false;
    }

    //R5 Monto máximo de financiamiento
    public boolean isCreditRequestAmountLessThanMaxAmount(CreditRequest credit){
        if (credit.getCreditRequestType() == null) {
            throw new IllegalStateException("CreditRequestType cannot be null");
        }

        switch (credit.getCreditRequestType()){
            case FIRSTHOME -> {
                return credit.getCreditRequestMount() <= credit.getPropertyValue()*0.8;
            }
            case SECONDHOME -> {
                return credit.getCreditRequestMount() <= credit.getPropertyValue()*0.7;
            }
            case COMERCIAL -> {
                return credit.getCreditRequestMount() <= credit.getPropertyValue()*0.6;
            }
            case REMODELING -> {
                return credit.getCreditRequestMount() <= credit.getPropertyValue()*0.5;
            }
            default -> throw new IllegalStateException("Unexpected value: " + credit.getCreditRequestType());
        }
    }

    //R6 Edad del solicitante
    public boolean isClientAgeAllowed(CreditRequest credit, Client client){
        ZonedDateTime endOfPaymentDate = credit.getRequestDate().plusYears((long) credit.getLoanPeriod());
        int clientAgeAtEndOfPayment = (int) client.getBirthDate().until(endOfPaymentDate, ChronoUnit.YEARS);
        return clientAgeAtEndOfPayment < 70;
    }

    public CreditRequest documentRevision(CreditRequest credit){
        ArrayList<DocumentType> docsNeeded = documentService.whichMissingDocuments(credit);
        if (docsNeeded.isEmpty()) {
            credit.setState(CreditRequestState.EVALUATING);
        } else {
            credit.setState(CreditRequestState.PENDINGDOCUMENTATION);
        }
        creditService.saveCreditRequest(credit);
        return credit;
    }

}
