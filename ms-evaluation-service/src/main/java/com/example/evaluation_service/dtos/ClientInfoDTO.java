package com.example.evaluation_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientInfoDTO {

    private Long clientId;
    private int monthlyIncome;
    private int totalDebt;
    private LocalDateTime lastDebtDate;
    private Boolean isEmployee;
    private LocalDateTime currentJobStartDate;
    private LocalDateTime lastJobEndDate;
    private int lastTwoYearIncome;
    private int accountBalance;
    private LocalDateTime accountStartDate;
}
