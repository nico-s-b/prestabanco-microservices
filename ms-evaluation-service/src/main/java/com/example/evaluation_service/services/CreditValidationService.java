package com.example.evaluation_service.services;

import com.example.tingeso1.entities.Client;
import com.example.tingeso1.entities.Credit;
import com.example.tingeso1.enums.CreditState;
import com.example.tingeso1.enums.DocumentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

@Service
public class CreditValidationService {
    @Autowired
    DocumentService documentService;

    @Autowired
    ClientService clientService;

    @Autowired
    CreditService creditService;

    public boolean verifyMaxFinancingMount(Credit credit){
        if (credit.getCreditType() == null) {
            throw new IllegalStateException("CreditType cannot be null");
        }

        int creditMount = credit.getCreditMount();
        int propertyValue = credit.getPropertyValue();
        int financingPercentage = (creditMount / propertyValue)*100;

        switch (credit.getCreditType()){
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
            default -> throw new IllegalStateException("Unexpected value: " + credit.getCreditType());
        }
        return false;
    }

    //R5 Monto máximo de financiamiento
    public boolean isCreditAmountLessThanMaxAmount(Credit credit){
        if (credit.getCreditType() == null) {
            throw new IllegalStateException("CreditType cannot be null");
        }

        switch (credit.getCreditType()){
            case FIRSTHOME -> {
                return credit.getCreditMount() <= credit.getPropertyValue()*0.8;
            }
            case SECONDHOME -> {
                return credit.getCreditMount() <= credit.getPropertyValue()*0.7;
            }
            case COMERCIAL -> {
                return credit.getCreditMount() <= credit.getPropertyValue()*0.6;
            }
            case REMODELING -> {
                return credit.getCreditMount() <= credit.getPropertyValue()*0.5;
            }
            default -> throw new IllegalStateException("Unexpected value: " + credit.getCreditType());
        }
    }

    //R6 Edad del solicitante
    public boolean isClientAgeAllowed(Credit credit, Client client){
        ZonedDateTime endOfPaymentDate = credit.getRequestDate().plusYears((long) credit.getLoanPeriod());
        int clientAgeAtEndOfPayment = (int) client.getBirthDate().until(endOfPaymentDate, ChronoUnit.YEARS);
        return clientAgeAtEndOfPayment < 70;
    }

    public Credit documentRevision(Credit credit){
        ArrayList<DocumentType> docsNeeded = documentService.whichMissingDocuments(credit);
        if (docsNeeded.isEmpty()) {
            credit.setState(CreditState.EVALUATING);
        } else {
            credit.setState(CreditState.PENDINGDOCUMENTATION);
        }
        creditService.saveCredit(credit);
        return credit;
    }

}
