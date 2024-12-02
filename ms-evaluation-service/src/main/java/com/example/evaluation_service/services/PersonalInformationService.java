package com.example.evaluation_service.services;

import com.example.common_utils.dtos.ClientDTO;
import com.example.evaluation_service.clients.ClientFeignClient;
import com.example.common_utils.dtos.CreditRequest;
import com.example.evaluation_service.entities.PersonalInformation;
import com.example.evaluation_service.repositories.PersonalInformationRepository;
import org.hibernate.sql.exec.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class PersonalInformationService {
    @Autowired
    PersonalInformationRepository personalInformationRepository;

    @Autowired
    ClientFeignClient clientFeignClient;

    public List<PersonalInformation> getAll(){
        return personalInformationRepository.findAll();
    }

    public PersonalInformation getById(Long id){
        return personalInformationRepository.getById(id);
    }

    public PersonalInformation getByClientId(Long clientId){
        ClientDTO client = clientFeignClient.getById(clientId);
        if (client == null) {
            throw new ExecutionException("Client not found");
        }
        return personalInformationRepository.findByClientId(clientId);
    }

    public void create(Long clientId) {
        PersonalInformation personalInformation = new PersonalInformation();

        ClientDTO client = clientFeignClient.getById(clientId);
        if (client == null) {
            throw new ExecutionException("Client not found");
        }
        personalInformation.setClientid(clientId);
        personalInformation.setBirthDate(client.getBirthDate());
        personalInformationRepository.save(personalInformation);
    }

    public void save(PersonalInformation personalInformation) {
        personalInformationRepository.save(personalInformation);
    }

    public boolean delete(Long id){
        try {
            personalInformationRepository.deleteById(id);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public boolean isClientAgeAllowed(CreditRequest credit, ClientDTO client){
        LocalDateTime endOfPaymentDate;
        endOfPaymentDate = credit.getRequestDate().plusYears(credit.getLoanPeriod());
        int clientAgeAtEndOfPayment = (int) client.getBirthDate().until(endOfPaymentDate, ChronoUnit.YEARS);
        return clientAgeAtEndOfPayment < 70;
    }
}
