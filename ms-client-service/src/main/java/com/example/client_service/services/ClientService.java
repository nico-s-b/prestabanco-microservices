package com.example.client_service.services;

import com.example.client_service.entities.*;
import com.example.client_service.repositories.ClientRepository;
import org.hibernate.sql.exec.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    ClientRepository clientRepository;

    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    public Client getById(Long id){
        Optional<Client> optionalRecord = clientRepository.findById(id);
        return optionalRecord.orElseThrow(() -> new ExecutionException("Client not found for this id :: " + id));
    }

    public Client getByRut(String rut) {return clientRepository.findByRut(rut);}

    public Client getByEmail(String email) {return clientRepository.findByEmail(email);}

    public Client save(Client client){
        return clientRepository.save(client);
    }

    public void update(Client client){

    }

    public void deleteById(Long id){clientRepository.deleteById(id);}

}
