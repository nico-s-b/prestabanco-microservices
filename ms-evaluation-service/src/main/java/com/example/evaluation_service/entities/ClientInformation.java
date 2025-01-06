package com.example.evaluation_service.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate ;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    private Long clientId;
    private int monthlyIncome;
    private int totalDebt;
    private LocalDate  lastDebtDate;
    private Boolean isEmployee;
    private LocalDate  currentJobStartDate;
    private LocalDate  lastJobEndDate;
    private int lastTwoYearIncome;
    private int accountBalance;
    private LocalDate  accountStartDate;
}
