package com.example.evaluation_service.services;

import com.example.evaluation_service.clients.CreditFeignClient;
import com.example.evaluation_service.entities.ClientAccount;
import com.example.evaluation_service.repositories.CreditRecordRepository;
import com.example.evaluation_service.entities.CreditRecord;
import com.example.evaluation_service.entities.EmploymentRecord;
//import com.example.evaluation_service.entities.Credit;
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
    CreditFeignClient creditFeignClient;
    @Autowired
    private EmploymentRecordService employmentRecordService;

    public ArrayList<CreditRecord> getAll(){
        return (ArrayList<CreditRecord>) creditRecordRepository.findAll();
    }

    public void create(Long clientId){
        CreditRecord record = new CreditRecord();
        record.setClientid(clientId);
        creditRecordRepository.save(record);
    }

    public CreditRecord save(CreditRecord clientCreditRecord){
        return creditRecordRepository.save(clientCreditRecord);
    }

    public CreditRecord getById(Long id){
        Optional<CreditRecord> optionalRecord = creditRecordRepository.findById(id);
        return optionalRecord.orElseThrow(() -> new ExecutionException("ClientCreditRecord not found for this id :: " + id));
    }

    public boolean deleteById(Long id) throws Exception {
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
