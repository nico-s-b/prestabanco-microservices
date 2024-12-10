package com.example.calculation_service.repositories;

import com.example.calculation_service.entities.TotalCosts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TotalCostRepository extends JpaRepository<TotalCosts, Long> {
    TotalCosts findByCreditId(Long creditId);
}
