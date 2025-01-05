package com.example.evaluation_service.services;

import com.example.evaluation_service.clients.ClientFeignClient;
import com.example.evaluation_service.dtos.ClientInfoDTO;
import com.example.evaluation_service.entities.ClientInformation;
import com.example.evaluation_service.repositories.ClientInformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientInformationService {
    @Autowired
    ClientInformationRepository clientInformationRepository;
    @Autowired
    ClientFeignClient clientFeignClient;

    public ClientInformation getByClientId(Long clientId) {
        ClientInformation clientInformation = clientInformationRepository.findByClientId(clientId);
        if (clientInformation == null) {
            throw new RuntimeException("ClientInfo not found");
        }
        return clientInformationRepository.findByClientId(clientId);
    }

    public void saveOrUpdate(ClientInfoDTO clientInfoDTO) {
        if (clientFeignClient.getById(clientInfoDTO.getClientId()) == null) {
            throw new RuntimeException("Client not found");
        }
        ClientInformation existingClientInfo = clientInformationRepository.findByClientId(clientInfoDTO.getClientId());

        if (existingClientInfo != null) {
            // Actualizar campos en la entidad existente
            existingClientInfo.setMonthlyIncome(clientInfoDTO.getMonthlyIncome());
            existingClientInfo.setTotalDebt(clientInfoDTO.getTotalDebt());
            existingClientInfo.setLastDebtDate(clientInfoDTO.getLastDebtDate());
            existingClientInfo.setIsEmployee(clientInfoDTO.getIsEmployee());
            existingClientInfo.setCurrentJobStartDate(clientInfoDTO.getCurrentJobStartDate());
            existingClientInfo.setLastJobEndDate(clientInfoDTO.getLastJobEndDate());
            existingClientInfo.setLastTwoYearIncome(clientInfoDTO.getLastTwoYearIncome());
            existingClientInfo.setAccountBalance(clientInfoDTO.getAccountBalance());
            existingClientInfo.setAccountStartDate(clientInfoDTO.getAccountStartDate());
            clientInformationRepository.save(existingClientInfo);
        } else {
            // Crear nueva entidad si no existe
            ClientInformation newClientInfo = new ClientInformation();
            newClientInfo.setClientId(clientInfoDTO.getClientId());
            newClientInfo.setMonthlyIncome(clientInfoDTO.getMonthlyIncome());
            newClientInfo.setTotalDebt(clientInfoDTO.getTotalDebt());
            newClientInfo.setLastDebtDate(clientInfoDTO.getLastDebtDate());
            newClientInfo.setIsEmployee(clientInfoDTO.getIsEmployee());
            newClientInfo.setCurrentJobStartDate(clientInfoDTO.getCurrentJobStartDate());
            newClientInfo.setLastJobEndDate(clientInfoDTO.getLastJobEndDate());
            newClientInfo.setLastTwoYearIncome(clientInfoDTO.getLastTwoYearIncome());
            newClientInfo.setAccountBalance(clientInfoDTO.getAccountBalance());
            newClientInfo.setAccountStartDate(clientInfoDTO.getAccountStartDate());
            clientInformationRepository.save(newClientInfo);
        }
    }

    public void delete(Long clientId) {
        ClientInformation clientInfo = clientInformationRepository.findByClientId(clientId);
        if (clientInfo == null) {
            throw new RuntimeException("Client information not found");
        }
        clientInformationRepository.delete(clientInfo);
    }
}
