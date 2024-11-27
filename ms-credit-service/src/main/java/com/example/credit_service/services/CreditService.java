package com.example.credit_service.services;

import com.example.CreditService.entities.Client;
import com.example.credit_service.entities.Credit;
import com.example.common_utils.enums.CreditState;
import com.example.common_utils.enums.CreditType;
import com.example.credit_service.repositories.CreditRepository;
import com.example.credit_service.dtos.CreditRequest;
import org.hibernate.sql.exec.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class CreditService {

    @Autowired
    CreditRepository creditRepository;

    @Autowired
    DocumentService documentService;

    @Autowired
    ClientService clientService;

    public ArrayList<Credit> getCredits(){
        return (ArrayList<Credit>) creditRepository.findAll();
    }

    public Credit saveCredit(Credit credit){
        credit.setLastUpdateDate(LocalDateTime.now());
        return creditRepository.save(credit);
    }

    public Credit cancelCredit(Credit credit){
        credit.setState(CreditState.CANCELLED);
        return creditRepository.save(credit);
    }

    public Credit getCreditById(Long id){
        Optional<Credit> optionalRecord = creditRepository.findById(id);
        return optionalRecord.orElseThrow(() -> new ExecutionException("DocumentEntity not found for this id :: " + id));
    }

    public ArrayList<Credit>  getCreditsById(Long id){
        return (ArrayList<Credit>) creditRepository.findAllByClientId(id);
    }

    public Credit createCredit(CreditRequest request, Client client) {
        Credit credit = buildCredit(request);
        credit.setClient(client);
        client.getCredits().add(credit);
        credit.setDocuments(new ArrayList<>());

        clientService.saveClient(client);
        return creditRepository.save(credit);
    }

    public boolean deleteCredit(Long id) throws Exception {
        try{
            creditRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public Credit buildCredit(CreditRequest request){
        Credit credit = new Credit();

        switch (request.getCreditType()) {
            case "FIRSTHOME" -> credit.setCreditType(CreditType.FIRSTHOME);
            case "SECONDHOME" -> credit.setCreditType(CreditType.SECONDHOME);
            case "COMERCIAL" -> credit.setCreditType(CreditType.COMERCIAL);
            case "REMODELING" -> credit.setCreditType(CreditType.REMODELING);
            default -> throw new IllegalArgumentException("Tipo de crédito no válido: " + request.getCreditType());
        }

        credit.setLoanPeriod(request.getLoanPeriod());
        credit.setCreditMount(request.getCreditMount());
        credit.setPropertyValue(request.getPropertyValue());
        credit.setAnnualRate(request.getAnnualRate());
        credit.setRequestDate(ZonedDateTime.now());
        credit.setLastUpdateDate(ZonedDateTime.now());
        credit.setState(CreditState.INITIALREV);
        return credit;
    }
}
