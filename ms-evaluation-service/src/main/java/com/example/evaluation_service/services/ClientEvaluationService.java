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
import com.example.evaluation_service.utils.EvaluationMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        clientEvaluation.setEvaluation(new ClientEvaluation.Evaluation());
        clientEvaluation.setEvaluationDate(LocalDateTime.now());
        clientEvaluationRepository.save(clientEvaluation);
    }

    public void update(Long creditId, EvaluationDTO evaluationDTO) {
        ClientEvaluation clientEvaluation = getByCreditId(creditId);
        if (clientEvaluation == null) {
            throw new EntityNotFoundException("ClientEvaluation no encontrada para creditId " + creditId);
        }
        clientEvaluation.setEvaluationDate(LocalDateTime.now());
        clientEvaluation.setEvaluation(EvaluationMapper.toEntity(evaluationDTO));
        clientEvaluationRepository.save(clientEvaluation);
    }

    public void evaluateClient(Long creditId, ClientEvaluation.Evaluation evaluation) {
        ClientEvaluation clientEvaluation = getByCreditId(creditId);
        if (clientEvaluation == null) {
            clientEvaluation = new ClientEvaluation();
            CreditRequest creditRequest = creditFeignClient.getById(creditId);
            clientEvaluation.setClientId(creditRequest.getClientId());
        }
        clientEvaluation.setEvaluationDate(LocalDateTime.now());
        clientEvaluation.setEvaluation(evaluation);
        clientEvaluationRepository.save(clientEvaluation);
    }

    public void autoEvaluateClientInfo(Long clientId, Long creditId){
        ClientEvaluation clientEvaluation = getByCreditId(creditId);
        ClientInformation clientInfo = clientInformationService.getByClientId(clientId);
        ClientDTO client = clientFeignClient.getById(clientId);
        CreditRequest credit = creditFeignClient.getById(creditId);
        ClientEvaluation.Evaluation evaluation = clientEvaluation.getEvaluation();
        if (calculateR1(clientInfo, credit)){
            evaluation.setR1(SUCCESS);
        } else {
            evaluation.setR1(FAIL);
        }
        if (calculateR3(clientInfo, credit)){
            evaluation.setR3(SUCCESS);
        } else {
            evaluation.setR3(FAIL);
        }
        if (calculateR4(clientInfo, credit)){
            evaluation.setR4(SUCCESS);
        } else {
            evaluation.setR4(FAIL);
        }
        if (calculateR5(credit)){
            evaluation.setR5(SUCCESS);
        } else {
            evaluation.setR5(FAIL);
        }
        if (calculateR6(client, credit)){
            evaluation.setR6(SUCCESS);
        } else {
            evaluation.setR6(FAIL);
        }
    }

    public void autoEvaluateClientPaymentCapacity(Long clientId, Long creditId){
        ClientEvaluation clientEvaluation = getByCreditId(creditId);
        ClientInformation clientInfo = clientInformationService.getByClientId(clientId);
        CreditRequest credit = creditFeignClient.getById(creditId);
        ClientEvaluation.Evaluation evaluation = clientEvaluation.getEvaluation();
        EvaluationStatus[] r7 = evaluation.getR7();
        if (calculateR71(clientInfo, credit)){
            r7[0] = SUCCESS;
        } else {
            r7[0] = FAIL;
        }
        if (calculateR74(clientInfo, credit)){
            r7[3] = SUCCESS;
        } else {
            r7[3] = FAIL;
        }
        evaluation.setR7(r7);
    }

    private boolean calculateR1(ClientInformation clientInfo, CreditRequest credit) {
        int monthlyInstallment = credit.getCreditMount();
        float rate;
        rate = ((float) monthlyInstallment / getClientMonthlyIncome(clientInfo))*100;
        //Aprobar si relación no supera el 35% (o sea, es 35% o menor)
        return rate < 36;
    }

    private boolean calculateR3(ClientInformation clientInfo, CreditRequest credit) {
        LocalDateTime start = clientInfo.getCurrentJobStartDate();
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

    private boolean calculateR4(ClientInformation clientInfo, CreditRequest credit) {
        int totalProjectedDebt = clientInfo.getTotalDebt() + calculationFeignClient.getInstallment(credit);
        int income = clientInfo.getMonthlyIncome();
        //Rechazar si la suma de deudas (considerando cuota del crédito) es mayor a 50%
        return  ((float) totalProjectedDebt / income)*100 < 51;
    }

    private boolean calculateR5(CreditRequest credit) {
        int maxFinancingMount = calculationFeignClient.getMaxFinancing(credit);
        int financingMount = credit.getCreditMount();
        return financingMount <= maxFinancingMount;
    }

    private boolean calculateR6(ClientDTO client, CreditRequest credit) {
        LocalDateTime endOfPaymentDate;
        endOfPaymentDate = credit.getRequestDate().plusYears(credit.getLoanPeriod());
        int clientAgeAtEndOfPayment = (int) client.getBirthDate().until(endOfPaymentDate, ChronoUnit.YEARS);
        return clientAgeAtEndOfPayment < 70;
    }

    //R71: Verifica si cliente tiene saldo mínimo necesario (al menos 10% del monto del crédito)
    private boolean calculateR71(ClientInformation clientInfo, CreditRequest credit) {
        return clientInfo.getAccountBalance() >= credit.getCreditMount()*0.1;
    }

    //R74 Verificación de relación de saldo y años de cuenta (al menos 20% de préstamo para cuenta de menos de 2 años, 10% para cuenta con 2 o más años)
    private boolean calculateR74(ClientInformation clientInfo, CreditRequest credit) {
        int yearsSinceAccountStart = (int) clientInfo.getAccountStartDate().until(credit.getRequestDate(), ChronoUnit.YEARS);
        if (yearsSinceAccountStart < 2) {
            return clientInfo.getAccountBalance() >= credit.getCreditMount() * 0.2;
        } else {
            return clientInfo.getAccountBalance() >= credit.getCreditMount() * 0.1;
        }
    }

    private int getClientMonthlyIncome(ClientInformation clientInfo) {
        if (clientInfo.getIsEmployee()) {
            return clientInfo.getMonthlyIncome();
        } else {
            return clientInfo.getLastTwoYearIncome() / 24;
        }
    }

}
