package com.example.calculation_service.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TotalCosts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    private Long creditId;

    private int installment;
    private int creditInsurance;
    private int fireInsurance;
    private int administrativeFee;
    private int monthlyTotal;
    private int finalTotal;
    private LocalDateTime calculationDate;
}
