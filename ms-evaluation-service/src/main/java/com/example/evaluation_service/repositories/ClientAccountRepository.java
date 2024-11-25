package com.example.evaluation_service.repositories;

import com.example.evaluation_service.entities.ClientAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientAccountRepository extends JpaRepository<ClientAccount, Long> {
    ClientAccount findByClientId(Long id);
}