package com.example.user_service.services;

import com.example.user_service.clients.InformationFeignClient;
import com.example.user_service.entities.Client;
import com.example.user_service.repositories.ClientRepository;
import org.hibernate.sql.exec.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    ClientRepository clientRepository;
    @Autowired
    InformationFeignClient informationFeignClient;

    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    public Client getById(Long id){
        Optional<Client> optionalRecord = clientRepository.findById(id);
        return optionalRecord.orElseThrow(() -> new ExecutionException("Client not found for this id :: " + id));
    }

    public Client getByRut(String rut) {return clientRepository.findByRut(rut);}

    public Client getByEmail(String email) {return clientRepository.findByEmail(email);}

    public Client saveOrUpdate(Client client) {
        boolean isNewClient = client.getId() == null;

        if (!isNewClient && !clientRepository.existsById(client.getId())) {
            throw new ExecutionException("Client not found for this id :: " + client.getId());
        }
        Client savedClient = clientRepository.save(client);

        if (isNewClient) {
            try {
                informationFeignClient.create(savedClient.getId());
            } catch (Exception e) {
                throw new ExecutionException("Failed to create ClientInformation for clientId :: " + savedClient.getId(), e);
            }
        }
        return savedClient;
    }


    public boolean deleteById(Long id) throws Exception {
        try{
            clientRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public Client authenticate(String email, String password){
        Client client = clientRepository.findByEmail(email);
        if (client != null){
            if (client.getPass().equals(password)){
                return client;
            }
        }
        return null;
    }

}
