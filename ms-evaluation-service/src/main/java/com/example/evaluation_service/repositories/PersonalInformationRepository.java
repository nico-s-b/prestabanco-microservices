package com.example.evaluation_service.repositories;

import com.example.evaluation_service.entities.EmploymentRecord;
import com.example.evaluation_service.entities.PersonalInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalInformationRepository extends JpaRepository<PersonalInformation, Long> {
    PersonalInformation findByClientId(Long clientId);
}
