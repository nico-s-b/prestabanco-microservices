package com.example.evaluation_service.repositories;

import com.example.evaluation_service.entities.ClientInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientInformationRepository extends JpaRepository<ClientInformation, Long> {
    ClientInformation findByClientId(Long clientId);
}