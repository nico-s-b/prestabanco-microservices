package com.example.evaluation_service.services;

import com.example.evaluation_service.clients.ClientFeignClient;
import com.example.evaluation_service.dtos.ClientDTO;
import com.example.evaluation_service.entities.ClientAccount;
import com.example.common_utils.*;
import com.example.evaluation_service.repositories.ClientAccountRepository;
import org.hibernate.sql.exec.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class ClientAccountService {

    @Autowired
    ClientAccountRepository clientAccountRepository;

    @Autowired
    ClientFeignClient clientFeignClient;

    public ArrayList<ClientAccount> getAll(){
        return (ArrayList<ClientAccount>) clientAccountRepository.findAll();
    }

    public ClientAccount getByClientId(Long clientId) {
        ClientDTO client = clientFeignClient.getById(clientId);
        if (client == null) {
            throw new ExecutionException("Client not found");
        }
        return clientAccountRepository.findByClientId(clientId);
    }

    public void create(Long clientId){
        ClientAccount account = new ClientAccount();
        account.setClientid(clientId);
        clientAccountRepository.save(account);
    }

    public ClientAccount save(ClientAccount clientAccount){
        return clientAccountRepository.save(clientAccount);
    }

    public ClientAccount getById(Long id){
        Optional<ClientAccount> optionalRecord = clientAccountRepository.findById(id);
        return optionalRecord.orElseThrow(() -> new ExecutionException("ClientAccount not found for this id :: " + id));
    }

    public boolean delete(Long id) throws Exception {
        try{
            clientAccountRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    //R71: Verifica si cliente tiene saldo mínimo necesario (al menos 10% del monto del crédito)
    void hasR1MinimumBalance(ClientAccount clientAccount, Credit credit){
        clientAccount.setR1MinimumBalance(clientAccount.getAccountBalance() >= credit.getCreditMount()*0.1);
    }

    //R74 Verificación de relación de saldo y años de cuenta (al menos 20% de préstamo para cuenta de menos de 2 años, 10% para cuenta con 2 o más años)
    void hasR4GoodBalanceYearsRelation(Credit credit, ClientAccount clientAccount) {
        int yearsSinceAccountStart = (int) clientAccount.getStartDate().until(credit.getRequestDate(), ChronoUnit.YEARS);
        if (yearsSinceAccountStart < 2){
            clientAccount.setR4BalanceYearsOfAccountRelation(clientAccount.getAccountBalance() >= credit.getCreditMount()*0.2);
        }else{
            clientAccount.setR4BalanceYearsOfAccountRelation(clientAccount.getAccountBalance() >= credit.getCreditMount()*0.1);
        }
    }

    //Evaluar reglas para un crédito dado.
    public void evaluateAccountCreditRules(Credit credit) {
        ClientAccount clientAccount = credit.getClient().getAccount();

        hasR1MinimumBalance(clientAccount, credit);

        hasR4GoodBalanceYearsRelation(credit, clientAccount);

        saveClientAccount(clientAccount);
    }

    void evaluateSaveCapacity(ClientAccount clientAccount) {
        int rulesApproved = rulesApprovedFromSaveCapacity(clientAccount);

        if (rulesApproved == 5) {
            clientAccount.setSaveCapacityStatus(SaveCapacityStatus.SOLID);
        }else if (rulesApproved == 4 || rulesApproved == 3) {
            clientAccount.setSaveCapacityStatus(SaveCapacityStatus.MODERATE);
        }else {
            clientAccount.setSaveCapacityStatus(SaveCapacityStatus.INSUFFICIENT);
        }
    }
}
