package com.example.evaluation_service.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmploymentRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    private Long clientid;

    private Boolean isWorking;
    private Boolean isEmployee;
    private ZonedDateTime currentWorkStartDate;
    private ZonedDateTime lastWorkEndDate;
    private int monthlyIncome;
    private int lastTwoYearMonthlyIncome;
}
