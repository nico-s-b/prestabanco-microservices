package com.example.evaluation_service.entities;

import com.example.common_utils.enums.EvaluationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientEvaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(name = "credit_id", nullable = false)
    private Long creditId;

    @Column(name = "evaluation_date")
    private LocalDate evaluationDate;

    @Column(name = "R1", nullable = false)
    @Enumerated(EnumType.STRING)
    private EvaluationStatus R1;

    @Column(name = "R2", nullable = false)
    @Enumerated(EnumType.STRING)
    private EvaluationStatus R2;

    @Column(name = "R3", nullable = false)
    @Enumerated(EnumType.STRING)
    private EvaluationStatus R3;

    @Column(name = "R4", nullable = false)
    @Enumerated(EnumType.STRING)
    private EvaluationStatus R4;

    @Column(name = "R5", nullable = false)
    @Enumerated(EnumType.STRING)
    private EvaluationStatus R5;

    @Column(name = "R6", nullable = false)
    @Enumerated(EnumType.STRING)
    private EvaluationStatus R6;

    @Column(name = "R7", nullable = false)
    @Enumerated(EnumType.STRING)
    private EvaluationStatus R7;

    @Column(name = "R7_1", nullable = false)
    @Enumerated(EnumType.STRING)
    private EvaluationStatus R7_1;

    @Column(name = "R7_2", nullable = false)
    @Enumerated(EnumType.STRING)
    private EvaluationStatus R7_2;

    @Column(name = "R7_3", nullable = false)
    @Enumerated(EnumType.STRING)
    private EvaluationStatus R7_3;

    @Column(name = "R7_4", nullable = false)
    @Enumerated(EnumType.STRING)
    private EvaluationStatus R7_4;

    @Column(name = "R7_5", nullable = false)
    @Enumerated(EnumType.STRING)
    private EvaluationStatus R7_5;
}
