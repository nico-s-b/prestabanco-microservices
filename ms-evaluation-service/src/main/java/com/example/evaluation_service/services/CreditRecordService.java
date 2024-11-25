package com.example.evaluation_service.services;

import com.example.evaluation_service.repositories.CreditRecordRepository;
import com.example.evaluation_service.entities.CreditRecord;
import com.example.evaluation_service.entities.EmploymentRecord;
import com.example.evaluation_service.entities.Credit;
import com.example.evaluation_service.repositories.CreditRecordRepository;
import org.hibernate.sql.exec.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CreditRecordService {

    @Autowired
    CreditRecordRepository creditRecordRepository;
    @Autowired
    private CreditService creditService;
    @Autowired
    private EmploymentRecordService employmentRecordService;

    public ArrayList<CreditRecord> getClientCreditRecords(){
        return (ArrayList<CreditRecord>) creditRecordRepository.findAll();
    }

    public CreditRecord saveClientCreditRecord(CreditRecord clientCreditRecord){
        return creditRecordRepository.save(clientCreditRecord);
    }

    public CreditRecord getClientCreditRecordById(Long id){
        Optional<CreditRecord> optionalRecord = creditRecordRepository.findById(id);
        return optionalRecord.orElseThrow(() -> new ExecutionException("ClientCreditRecord not found for this id :: " + id));
    }

    public boolean deleteClientCreditRecord(Long id) throws Exception {
        try{
            creditRecordRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    //R4: Relación entre deuda e ingreso
    public boolean hasGoodDebtIncomeRate(CreditRecord clientCreditRecord, CreditRecord clientEmploymentRecord, Credit credit) {
        int totalProjectedDebt = clientCreditRecord.getDebtAmount() + creditService.getCreditInstallment(credit);
        int income = employmentRecordService.getClientMonthlyIncome(clientEmploymentRecord);
        //Rechazar si la suma de deudas (considerando cuota del crédito) es mayor a 50%
        return  ((float) totalProjectedDebt / income)*100 < 51;
    }
}
