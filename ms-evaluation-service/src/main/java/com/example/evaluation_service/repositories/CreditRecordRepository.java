package com.example.evaluation_service.repositories;

import com.example.evaluation_service.entities.CreditRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditRecordRepository extends JpaRepository<CreditRecord, Long> {
    CreditRecord findByClientId(Long clientId);
}