package com.example.evaluation_service.services;

import com.example.evaluation_service.entities.Credit;
import com.example.evaluation_service.entities.EmploymentRecord;
import com.example.evaluation_service.repositories.EmploymentRecordRepository;
import org.hibernate.sql.exec.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class EmploymentRecordService {

    @Autowired
    EmploymentRecordRepository employmentRecordRepository;

    @Autowired
    CreditService creditService;

    public ArrayList<EmploymentRecord> getAll(){
        return (ArrayList<EmploymentRecord>) employmentRecordRepository.findAll();
    }

    public EmploymentRecord saveRecord(EmploymentRecord clientEmploymentRecord){
        return employmentRecordRepository.save(clientEmploymentRecord);
    }

    public EmploymentRecord getClientEmploymentRecordById(Long id){
        Optional<EmploymentRecord> optionalRecord = employmentRecordRepository.findById(id);
        return optionalRecord.orElseThrow(() -> new ExecutionException("ClientEmploymentRecord not found for this id :: " + id));
    }

    public boolean deleteClientEmploymentRecord(Long id) throws Exception {
        try{
            employmentRecordRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    //Entrega el ingreso mensual del cliente, dependiendo si es empleado o si es independiente
    public int getClientMonthlyIncome(EmploymentRecord employmentRecord){
        if (employmentRecord.getIsEmployee()) {
            return employmentRecord.getMonthlyIncome();
        }else{
            return employmentRecord.getLastTwoYearIncome() / 24;
        }
    }

    //R1
    public boolean hasEnoughIncomeInstallmentRate(EmploymentRecord employmentRecord, Credit credit){
        int monthlyInstallment = creditService.getCreditInstallment(credit);
        float rate;
        rate = ((float) monthlyInstallment / getClientMonthlyIncome(employmentRecord))*100;
        //Aprobar si relación no supera el 35% (o sea, es 35% o menor)
        return rate < 36;
    }

    //R3 Calcula años de servicio de un empleado a partir de fecha de inicio de su trabajo actual
    public boolean hasEnoughYearsOfService(EmploymentRecord employmentRecord, Credit credit){
        ZonedDateTime start = employmentRecord.getCurrentWorkStartDate();
        int yearsOfService = (int) start.until(credit.getRequestDate(), ChronoUnit.YEARS);
        // Antigüedad menor a un año es rechazada
        if (yearsOfService == 0){
            return false;
        }
        // Aprobar si al menos tiene 1 año de antigüedad
        else {
            return true;
        }
    }



}