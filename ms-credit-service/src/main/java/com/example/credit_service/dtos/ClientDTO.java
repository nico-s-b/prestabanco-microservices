package com.example.credit_service.dtos;

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
}
