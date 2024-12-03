package com.example.common_utils.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {
    private Long id;
    private String name;
    private String paternalLastname;
    private String maternalLastname;
    private String email;
    private String phone;
    private LocalDateTime birthDate;
}
