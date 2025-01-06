package com.example.evaluation_service.repositories;

import com.example.evaluation_service.entities.ClientEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientEvaluationRepository extends JpaRepository<ClientEvaluation, Long> {
    ClientEvaluation findByCreditId(Long id);
}