package com.example.credit_service.dtos;

import lombok.Data;

@Data
public class ClientDTO {
    private Long id;
    private String name;
    private String paternalLastname;
    private String maternalLastname;
}
