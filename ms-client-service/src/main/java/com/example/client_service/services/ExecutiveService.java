package com.example.client_service.services;

import com.example.client_service.entities.Executive;
import com.example.client_service.repositories.ExecutiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExecutiveService {

    @Autowired
    ExecutiveRepository executiveRepository;

    public List<Executive> getAll() {return executiveRepository.findAll();}
    public Executive getById(Long id){
        return executiveRepository.findById(id).get();
    }
    public Executive save(Executive executive) {
        return executiveRepository.save(executive);
    }
        
}
