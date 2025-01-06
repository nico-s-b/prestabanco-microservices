package com.example.evaluation_service.dtos;

import com.example.common_utils.dtos.ClientDTO;
import com.example.common_utils.dtos.CreditRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientAndCreditDTO {
    private ClientDTO client;
    private CreditRequest credit;
}
