package com.example.common_utils.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditRequest {
    private String creditType;
    private int loanPeriod;
    private int creditMount;
    private int propertyValue;
    private float annualRate;
    private LocalDateTime requestDate;
}
