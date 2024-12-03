package com.example.common_utils.dtos;

import com.example.common_utils.enums.CreditType;
import com.example.common_utils.enums.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentUpdateDTO {
    private Long creditId;
    private CreditType creditType;
    private List<DocumentType> documentTypes;
}
