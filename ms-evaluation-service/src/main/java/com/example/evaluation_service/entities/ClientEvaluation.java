package com.example.evaluation_service.entities;

import com.example.common_utils.enums.EvaluationStatus;
import com.example.evaluation_service.utils.EvaluationConverter;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @Column(name = "evaluation", columnDefinition = "jsonb", nullable = false)
    @Convert(converter = EvaluationConverter.class)
    private Evaluation evaluation;

    @Column(name = "evaluation_date")
    private LocalDateTime evaluationDate;

    // Clase interna para representar la evaluación
    @Getter
    @Setter
    public static class Evaluation {

        @JsonProperty("R1")
        private EvaluationStatus R1;

        @JsonProperty("R2")
        private EvaluationStatus R2;

        @JsonProperty("R3")
        private EvaluationStatus R3;

        @JsonProperty("R4")
        private EvaluationStatus R4;

        @JsonProperty("R5")
        private EvaluationStatus R5;

        @JsonProperty("R6")
        private EvaluationStatus R6;

        @JsonProperty("R7")
        private EvaluationStatus[] R7;

        // Constructor por defecto
        public Evaluation() {
            this.R1 = EvaluationStatus.PENDING;
            this.R2 = EvaluationStatus.PENDING;
            this.R3 = EvaluationStatus.PENDING;
            this.R4 = EvaluationStatus.PENDING;
            this.R5 = EvaluationStatus.PENDING;
            this.R6 = EvaluationStatus.PENDING;
            this.R7 = new EvaluationStatus[]{
                    EvaluationStatus.PENDING,
                    EvaluationStatus.PENDING,
                    EvaluationStatus.PENDING,
                    EvaluationStatus.PENDING,
                    EvaluationStatus.PENDING};
        }
    }
}
