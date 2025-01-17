package com.example.credit_service.entities;

import com.example.common_utils.enums.CreditState;
import com.example.common_utils.enums.CreditType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "credits")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Credit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    private Long clientId;

    @Enumerated(EnumType.STRING)
    private CreditType creditType;

    private int loanPeriod;
    private float annualRate;
    private int creditMount;
    private int propertyValue;
    private LocalDateTime requestDate;
}
