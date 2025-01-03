package com.example.evaluation_service.entities;

import com.example.evaluation_service.utils.EvaluationConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

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

    @Column(name = "evaluation", columnDefinition = "jsonb", nullable = false)
    @Convert(converter = EvaluationConverter.class)
    private Map<String, Object> evaluation;

    @Column(name = "evaluation_date")
    private LocalDateTime evaluationDate;
}
