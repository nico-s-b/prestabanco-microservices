package com.example.calculation_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditRestrictionsDTO {
    private float maxLoanPeriod;
    private float maxFinancingMount;
    private float minAnnualRate;
    private float maxAnnualRate;
}
