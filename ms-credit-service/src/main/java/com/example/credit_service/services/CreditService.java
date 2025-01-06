package com.example.credit_service.services;

import com.example.common_utils.dtos.TrackingRequest;
import com.example.credit_service.clients.ClientFeignClient;
import com.example.common_utils.dtos.ClientDTO;
import com.example.credit_service.clients.EvaluationFeignClient;
import com.example.credit_service.clients.TrackingFeignClient;
import com.example.credit_service.entities.Credit;
import com.example.common_utils.enums.CreditType;
import com.example.credit_service.repositories.CreditRepository;
import com.example.common_utils.dtos.CreditRequest;
import jakarta.transaction.Transactional;
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

    @Autowired
    TrackingFeignClient trackingFeignClient;

    @Autowired
    EvaluationFeignClient evaluationFeignClient;

    public ArrayList<Credit> getAll(){
        return (ArrayList<Credit>) creditRepository.findAll();
    }

    @Transactional
    public Credit save(Credit credit) {
        Credit savedCredit = creditRepository.save(credit);

        TrackingRequest trackingRequest = new TrackingRequest();
        trackingRequest.setCreditId(savedCredit.getId());
        trackingRequest.setLastUpdateDate(LocalDateTime.now());

        trackingFeignClient.createTracking(trackingRequest);
        evaluationFeignClient.create(savedCredit.getClientId(), savedCredit.getId());

        return savedCredit;
    }

    @Transactional
    public Credit create(CreditRequest request, Long clientId) {
        ClientDTO client = clientFeignClient.getById(clientId);
        if (client == null) {
            throw new RuntimeException("Client not found");
        }

        Credit credit = buildCredit(request, clientId);
        return save(credit);
    }

    public Credit getById(Long id){
        Optional<Credit> optionalRecord = creditRepository.findById(id);
        return optionalRecord.orElseThrow(() -> new ExecutionException("DocumentEntity not found for this id :: " + id));
    }

    public ArrayList<Credit>  getByClientId(Long id){
        return (ArrayList<Credit>) creditRepository.findAllByClientId(id);
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
        credit.setClientId(clientId);
        return credit;
    }
}
