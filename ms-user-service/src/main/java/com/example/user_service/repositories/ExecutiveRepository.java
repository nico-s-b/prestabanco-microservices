package com.example.user_service.repositories;

import com.example.user_service.entities.Executive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ExecutiveRepository extends JpaRepository<Executive, Long> {
    Executive findByEmail(String email);

    @Query(value = "SELECT * FROM executive ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Executive findRandomExecutive();
}