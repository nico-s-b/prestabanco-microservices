package com.example.evaluation_service.services;

import com.example.evaluation_service.entities.ClientAccount;
import com.example.evaluation_service.entities.Credit;
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

    public ArrayList<ClientAccount> getClientAccounts(){
        return (ArrayList<ClientAccount>) clientAccountRepository.findAll();
    }

    public ClientAccount getClientAccountByClient(Long id) {
        return clientAccountRepository.findByClientId(id);
    }

    public ClientAccount saveClientAccount(ClientAccount clientAccount){
        return clientAccountRepository.save(clientAccount);
    }

    public ClientAccount getClientAccountById(Long id){
        Optional<ClientAccount> optionalRecord = clientAccountRepository.findById(id);
        return optionalRecord.orElseThrow(() -> new ExecutionException("ClientAccount not found for this id :: " + id));
    }

    public boolean deleteClientAccount(Long id) throws Exception {
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

    //Verifica cuántas reglas cumple el cliente
    int rulesApprovedFromSaveCapacity(ClientAccount clientAccount){
        int rules = 0;

        if (clientAccount.getR1MinimumBalance()){
            rules++;
        }
        if (clientAccount.getR2ConsistentSaves()){
            rules++;
        }
        if (clientAccount.getR3PeriodicDeposits()){
            rules++;
        }
        if (clientAccount.getR4BalanceYearsOfAccountRelation()){
            rules++;
        }
        if (clientAccount.getR5RecentWithdrawals()) {
            rules++;
        }
        return rules;
    }

    //Verifica si se han evaluado todas las reglas del cliente
    boolean hasAllRulesEvaluated(ClientAccount clientAccount){
        if (clientAccount.getR1MinimumBalance() == null){
            return false;
        }
        if (clientAccount.getR2ConsistentSaves() == null){
            return false;
        }
        if (clientAccount.getR3PeriodicDeposits() == null){
            return false;
        }
        if (clientAccount.getR4BalanceYearsOfAccountRelation() == null){
            return false;
        }
        if (clientAccount.getR5RecentWithdrawals() == null) {
            return false;
        }
        return true;
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
