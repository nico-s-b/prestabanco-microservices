package com.example.evaluation_service.dtos;

import com.example.common_utils.enums.CreditType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditRequest {
    private CreditType creditType;
    private int loanPeriod;
    private int creditMount;
    private int propertyValue;
    private float annualRate;
    private LocalDateTime requestDate;
}
