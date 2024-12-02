package com.example.common_utils.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClientDTO {
    private Long id;
    private String name;
    private String paternalLastname;
    private String maternalLastname;
    private String email;
    private String phone;
    private LocalDateTime birthDate;
}
