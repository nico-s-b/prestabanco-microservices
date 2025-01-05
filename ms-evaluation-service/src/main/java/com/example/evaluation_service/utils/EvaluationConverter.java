package com.example.evaluation_service.utils;

import com.example.common_utils.enums.EvaluationStatus;
import com.example.evaluation_service.entities.ClientEvaluation;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class EvaluationConverter implements AttributeConverter<ClientEvaluation.Evaluation, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(ClientEvaluation.Evaluation evaluation) {
        try {
            // Convierte el objeto `Evaluation` a una representación JSON como `String`
            return objectMapper.writeValueAsString(evaluation);
        } catch (Exception e) {
            throw new RuntimeException("Error al convertir la evaluación a JSON", e);
        }
    }

    @Override
    public ClientEvaluation.Evaluation convertToEntityAttribute(String dbData) {
        try {
            // Convierte la representación JSON (String) a un objeto `Evaluation`
            return objectMapper.readValue(dbData, ClientEvaluation.Evaluation.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al convertir JSON a evaluación", e);
        }
    }
}
