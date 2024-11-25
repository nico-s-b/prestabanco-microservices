package com.example.evaluation_service.repositories;

import com.example.evaluation_service.entities.EmploymentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmploymentRecordRepository extends JpaRepository<EmploymentRecord, Long> {
}