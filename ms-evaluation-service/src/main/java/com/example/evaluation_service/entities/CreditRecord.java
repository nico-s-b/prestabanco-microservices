package com.example.evaluation_service.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    private Long clientid;

    private int debtAmount;                 //Ingresado por cliente
    private ZonedDateTime lastDebtDate;     //Ingresado por cliente
    private ZonedDateTime oldestUnpaidInstallmentDate;  //Ingresado por cliente
    private Boolean isSlowpayer;
    private Boolean hasSeriousDebts;
}
