package com.example.common_utils.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrackingRequest {
    private Long creditId;
    private LocalDateTime lastUpdateDate;
}
