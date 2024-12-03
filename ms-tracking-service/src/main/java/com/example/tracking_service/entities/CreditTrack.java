package com.example.tracking_service.entities;

import com.example.common_utils.enums.CreditState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "trackings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditTrack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    private Long creditId;

    @Enumerated(EnumType.STRING)
    private CreditState state;
    private LocalDateTime lastUpdateDate;
    private Long executiveId;
}
