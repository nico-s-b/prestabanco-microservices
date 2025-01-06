package com.example.evaluation_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDate ;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientInfoDTO {

    private Long clientId;
    private int monthlyIncome;
    private int totalDebt;
    private LocalDate lastDebtDate;
    private Boolean isEmployee;
    private LocalDate  currentJobStartDate;
    private LocalDate  lastJobEndDate;
    private int lastTwoYearIncome;
    private int accountBalance;
    private LocalDate  accountStartDate;
}
