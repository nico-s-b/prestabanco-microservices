package com.example.tracking_service.repositories;

import com.example.tracking_service.entities.CreditTrack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackingRepository extends JpaRepository<CreditTrack, Long> {
    public CreditTrack findByCreditId(Long creditId);
}
