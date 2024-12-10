package com.example.user_service.services;

import com.example.user_service.entities.Excecutive;
import com.example.user_service.repositories.ExcecutiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExcecutiveService {

    @Autowired
    ExcecutiveRepository excecutiveRepository;

    public List<Excecutive> getAll() {return excecutiveRepository.findAll();}
    public Excecutive getById(Long id){
        return excecutiveRepository.findById(id).get();
    }
    public Excecutive save(Excecutive excecutive) {
        return excecutiveRepository.save(excecutive);
    }
    public Excecutive getByEmail(String email) {
        return excecutiveRepository.findByEmail(email);
    }

    public Long pickExecutive(){
        Excecutive excecutive = excecutiveRepository.findRandomExecutive();
        return excecutive.getId();
    }

    public Excecutive authenticate(String email, String password){
        Excecutive excecutive = getByEmail(email);
        if (excecutive != null){
            if (excecutive.getPass().equals(password)){
                return excecutive;
            }
        }
        return null;
    }

}
