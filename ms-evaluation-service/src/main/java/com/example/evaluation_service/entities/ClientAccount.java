package com.example.evaluation_service.entities;

import com.example.common_utils.enums.SaveCapacityStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    private Long clientid;

    private int accountBalance;
    private ZonedDateTime startDate;
    private SaveCapacityStatus saveCapacityStatus;
}
