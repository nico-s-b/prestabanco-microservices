package com.example.user_service.services;

import com.example.user_service.entities.Executive;
import com.example.user_service.repositories.ExecutiveRepository;
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
    public Executive getByEmail(String email) {
        return executiveRepository.findByEmail(email);
    }

    public Long pickExecutive(){
        Executive executive = executiveRepository.findRandomExecutive();
        return executive.getId();
    }

    public Executive authenticate(String email, String password){
        Executive executive = getByEmail(email);
        if (executive != null){
            if (executive.getPass().equals(password)){
                return executive;
            }
        }
        return null;
    }

}
