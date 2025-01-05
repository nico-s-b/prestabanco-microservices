package com.example.evaluation_service.dtos;

import com.example.common_utils.enums.EvaluationStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationDTO {

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

    // Constructor personalizado para inicializar valores por defecto
    public static EvaluationDTO defaultEvaluation() {
        return new EvaluationDTO(
            EvaluationStatus.PENDING,
            EvaluationStatus.PENDING,
            EvaluationStatus.PENDING,
            EvaluationStatus.PENDING,
            EvaluationStatus.PENDING,
            EvaluationStatus.PENDING,
            new EvaluationStatus[]{
                    EvaluationStatus.PENDING,
                    EvaluationStatus.PENDING,
                    EvaluationStatus.PENDING,
                    EvaluationStatus.PENDING,
                    EvaluationStatus.PENDING}
        );
    }
}
