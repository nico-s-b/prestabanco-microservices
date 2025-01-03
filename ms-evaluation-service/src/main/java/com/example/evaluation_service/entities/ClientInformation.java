package com.example.evaluation_service.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    private int monthlyIncome;
    private int totalDebt;
    private LocalDateTime lastDebtDate;
    private Boolean isDependentWorker;
    private LocalDateTime lastJobStartDate;
    private LocalDateTime lastJobEndDate;
    private int lastTwoYearIncome;
    private int accountBalance;
    private LocalDateTime accountStartDate;
}
