package com.example.evaluation_service.utils;

import com.example.evaluation_service.dtos.EvaluationDTO;
import com.example.evaluation_service.entities.ClientEvaluation;

public class EvaluationMapper {
    public static EvaluationDTO toDTO(ClientEvaluation.Evaluation evaluation) {
        return new EvaluationDTO(
                evaluation.getR1(),
                evaluation.getR2(),
                evaluation.getR3(),
                evaluation.getR4(),
                evaluation.getR5(),
                evaluation.getR6(),
                evaluation.getR7()
        );
    }

    public static ClientEvaluation.Evaluation toEntity(EvaluationDTO dto) {
        ClientEvaluation.Evaluation evaluation = new ClientEvaluation.Evaluation();
        evaluation.setR1(dto.getR1());
        evaluation.setR2(dto.getR2());
        evaluation.setR3(dto.getR3());
        evaluation.setR4(dto.getR4());
        evaluation.setR5(dto.getR5());
        evaluation.setR6(dto.getR6());
        evaluation.setR7(dto.getR7());
        return evaluation;
    }
}
