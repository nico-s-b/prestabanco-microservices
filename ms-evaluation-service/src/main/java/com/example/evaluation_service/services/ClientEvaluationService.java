package com.example.evaluation_service.services;

import com.example.common_utils.dtos.ClientDTO;
import com.example.common_utils.dtos.CreditRequest;
import com.example.common_utils.enums.EvaluationStatus;
import com.example.evaluation_service.clients.CalculationFeignClient;
import com.example.evaluation_service.clients.ClientFeignClient;
import com.example.evaluation_service.clients.CreditFeignClient;
import com.example.evaluation_service.dtos.EvaluationDTO;
import com.example.evaluation_service.entities.ClientEvaluation;
import com.example.evaluation_service.entities.ClientInformation;
import com.example.evaluation_service.repositories.ClientEvaluationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate ;
import java.time.temporal.ChronoUnit;


import static com.example.common_utils.enums.EvaluationStatus.FAIL;
import static com.example.common_utils.enums.EvaluationStatus.SUCCESS;

@Service
public class ClientEvaluationService {
    @Autowired
    ClientEvaluationRepository clientEvaluationRepository;

    @Autowired
    ClientInformationService clientInformationService;

    @Autowired
    CreditFeignClient creditFeignClient;

    @Autowired
    ClientFeignClient clientFeignClient;

    @Autowired
    CalculationFeignClient calculationFeignClient;

    public ClientEvaluation getByCreditId(Long creditId) {
        return clientEvaluationRepository.findByCreditId(creditId);
    }

    public void create(Long creditId, Long clientId){
        ClientEvaluation clientEvaluation = new ClientEvaluation();
        clientEvaluation.setCreditId(creditId);
        clientEvaluation.setClientId(clientId);
        clientEvaluation.setEvaluationDate(LocalDate .now());
        ClientEvaluation clientInfoPENDING = setPENDINGS(clientEvaluation);
        clientEvaluationRepository.save(clientInfoPENDING);
    }

    public void update(Long creditId, EvaluationDTO evaluationDTO) {
        ClientEvaluation clientEvaluation = getByCreditId(creditId);
        if (clientEvaluation == null) {
            throw new EntityNotFoundException("ClientEvaluation no encontrada para creditId " + creditId);
        }
        clientEvaluation.setEvaluationDate(LocalDate.now());
        clientEvaluation.setR1(evaluationDTO.getR1());
        clientEvaluation.setR2(evaluationDTO.getR2());
        clientEvaluation.setR3(evaluationDTO.getR3());
        clientEvaluation.setR4(evaluationDTO.getR4());
        clientEvaluation.setR5(evaluationDTO.getR5());
        clientEvaluation.setR6(evaluationDTO.getR6());
        clientEvaluation.setR7(evaluationDTO.getR7());
        clientEvaluation.setR7_1(evaluationDTO.getR7_1());
        clientEvaluation.setR7_2(evaluationDTO.getR7_2());
        clientEvaluation.setR7_3(evaluationDTO.getR7_3());
        clientEvaluation.setR7_4(evaluationDTO.getR7_4());
        clientEvaluation.setR7_5(evaluationDTO.getR7_5());
        clientEvaluationRepository.save(clientEvaluation);
    }

    public void evaluateClient(Long creditId, EvaluationDTO evaluation) {
        ClientEvaluation clientEvaluation = getByCreditId(creditId);
        if (clientEvaluation == null) {
            clientEvaluation = new ClientEvaluation();
            CreditRequest creditRequest = creditFeignClient.getById(creditId);
            clientEvaluation.setClientId(creditRequest.getClientId());
        }
        clientEvaluation.setEvaluationDate(LocalDate.now());

        clientEvaluationRepository.save(clientEvaluation);
    }

    public void autoEvaluateClientInfo(Long clientId, Long creditId){
        ClientEvaluation clientEvaluation = getByCreditId(creditId);
        ClientInformation clientInfo = clientInformationService.getByClientId(clientId);
        ClientDTO client = clientFeignClient.getById(clientId);
        CreditRequest credit = creditFeignClient.getById(creditId);
         if (calculateR1(clientInfo, credit)){
             clientEvaluation.setR1(SUCCESS);
        } else {
             clientEvaluation.setR1(FAIL);
        }
        if (calculateR3(clientInfo, credit)){
            clientEvaluation.setR3(SUCCESS);
        } else {
            clientEvaluation.setR3(FAIL);
        }
        if (calculateR4(clientInfo, credit)){
            clientEvaluation.setR4(SUCCESS);
        } else {
            clientEvaluation.setR4(FAIL);
        }
        if (calculateR5(credit)){
            clientEvaluation.setR5(SUCCESS);
        } else {
            clientEvaluation.setR5(FAIL);
        }
        if (calculateR6(client, credit)){
            clientEvaluation.setR6(SUCCESS);
        } else {
            clientEvaluation.setR6(FAIL);
        }
    }

    public void autoEvaluateClientPaymentCapacity(Long clientId, Long creditId){
        ClientEvaluation clientEvaluation = getByCreditId(creditId);
        ClientInformation clientInfo = clientInformationService.getByClientId(clientId);
        CreditRequest credit = creditFeignClient.getById(creditId);
        if (calculateR71(clientInfo, credit)){
            clientEvaluation.setR7_1(SUCCESS);
        } else {
            clientEvaluation.setR7_1(FAIL);
        }
        if (calculateR74(clientInfo, credit)){
            clientEvaluation.setR7_4(SUCCESS);
        } else {
            clientEvaluation.setR7_4(FAIL);
        }
    }

    public boolean calculateR1(ClientInformation clientInfo, CreditRequest credit) {
        int monthlyInstallment = credit.getCreditMount();
        float rate;
        rate = ((float) monthlyInstallment / getClientMonthlyIncome(clientInfo))*100;
        //Aprobar si relación no supera el 35% (o sea, es 35% o menor)
        return rate < 36;
    }

    public boolean calculateR3(ClientInformation clientInfo, CreditRequest credit) {
        LocalDate  start = clientInfo.getCurrentJobStartDate();
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

    public boolean calculateR4(ClientInformation clientInfo, CreditRequest credit) {
        int totalProjectedDebt = clientInfo.getTotalDebt() + calculationFeignClient.getInstallment(credit);
        int income = clientInfo.getMonthlyIncome();
        //Rechazar si la suma de deudas (considerando cuota del crédito) es mayor a 50%
        return  ((float) totalProjectedDebt / income)*100 < 51;
    }

    public boolean calculateR5(CreditRequest credit) {
        int maxFinancingMount = calculationFeignClient.getMaxFinancing(credit);
        int financingMount = credit.getCreditMount();
        return financingMount <= maxFinancingMount;
    }

    public boolean calculateR6(ClientDTO client, CreditRequest credit) {
        int clientAgeAtEndOfPayment = calculateAge(client, credit);
        return clientAgeAtEndOfPayment < 70;
    }

    public int calculateAge(ClientDTO client, CreditRequest credit) {
        LocalDate endOfPaymentDate = credit.getRequestDate().toLocalDate().plusYears(credit.getLoanPeriod());
        LocalDate birthDate = client.getBirthDate().toLocalDate();
        int clientAgeAtEndOfPayment = (int) birthDate.until(endOfPaymentDate, ChronoUnit.YEARS);
        return clientAgeAtEndOfPayment;
    }

    //R71: Verifica si cliente tiene saldo mínimo necesario (al menos 10% del monto del crédito)
    public boolean calculateR71(ClientInformation clientInfo, CreditRequest credit) {
        return clientInfo.getAccountBalance() >= credit.getCreditMount()*0.1;
    }

    //R74 Verificación de relación de saldo y años de cuenta (al menos 20% de préstamo para cuenta de menos de 2 años, 10% para cuenta con 2 o más años)
    public boolean calculateR74(ClientInformation clientInfo, CreditRequest credit) {
        int yearsSinceAccountStart = (int) clientInfo.getAccountStartDate().until(credit.getRequestDate(), ChronoUnit.YEARS);
        if (yearsSinceAccountStart < 2) {
            return clientInfo.getAccountBalance() >= credit.getCreditMount() * 0.2;
        } else {
            return clientInfo.getAccountBalance() >= credit.getCreditMount() * 0.1;
        }
    }

    public int getClientMonthlyIncome(ClientInformation clientInfo) {
        if (clientInfo.getIsEmployee()) {
            return clientInfo.getMonthlyIncome();
        } else {
            return clientInfo.getLastTwoYearIncome() / 24;
        }
    }

    private ClientEvaluation setPENDINGS(ClientEvaluation evaluation) {
        evaluation.setR1(EvaluationStatus.PENDING);
        evaluation.setR2(EvaluationStatus.PENDING);
        evaluation.setR3(EvaluationStatus.PENDING);
        evaluation.setR4(EvaluationStatus.PENDING);
        evaluation.setR5(EvaluationStatus.PENDING);
        evaluation.setR6(EvaluationStatus.PENDING);
        evaluation.setR7(EvaluationStatus.PENDING);
        evaluation.setR7_1(EvaluationStatus.PENDING);
        evaluation.setR7_2(EvaluationStatus.PENDING);
        evaluation.setR7_3(EvaluationStatus.PENDING);
        evaluation.setR7_4(EvaluationStatus.PENDING);
        evaluation.setR7_5(EvaluationStatus.PENDING);
        return evaluation;
    }

}
