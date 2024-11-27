package com.example.credit_service.services;

import com.example.credit_service.clients.ClientFeignClient;
import com.example.credit_service.dtos.ClientDTO;
import com.example.credit_service.entities.Credit;
import com.example.common_utils.enums.CreditState;
import com.example.common_utils.enums.CreditType;
import com.example.credit_service.repositories.CreditRepository;
import com.example.credit_service.dtos.CreditRequest;
import org.hibernate.sql.exec.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class CreditService {

    @Autowired
    CreditRepository creditRepository;

    @Autowired
    ClientFeignClient clientFeignClient;

    public ArrayList<Credit> getAll(){
        return (ArrayList<Credit>) creditRepository.findAll();
    }

    public Credit save(Credit credit){
        credit.setLastUpdateDate(LocalDateTime.now());
        return creditRepository.save(credit);
    }

    public Credit cancel(Credit credit){
        credit.setState(CreditState.CANCELLED);
        return creditRepository.save(credit);
    }

    public Credit getById(Long id){
        Optional<Credit> optionalRecord = creditRepository.findById(id);
        return optionalRecord.orElseThrow(() -> new ExecutionException("DocumentEntity not found for this id :: " + id));
    }

    public ArrayList<Credit>  getByClientId(Long id){
        return (ArrayList<Credit>) creditRepository.findAllByClientId(id);
    }

    public Credit create(CreditRequest request, Long clientId) {
        //Verificar si existe Cliente
        ClientDTO client = clientFeignClient.getById(clientId);;
        if (client == null) {
            throw new ExecutionException("Client not found");
        }

        Credit credit = buildCredit(request, clientId);
        //credit.setDocuments(new ArrayList<>());

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

    public Credit buildCredit(CreditRequest request, Long clientId){
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
        credit.setRequestDate(LocalDateTime.now());
        credit.setLastUpdateDate(LocalDateTime.now());
        credit.setState(CreditState.INITIALREV);
        credit.setClientId(clientId);
        return credit;
    }
}
