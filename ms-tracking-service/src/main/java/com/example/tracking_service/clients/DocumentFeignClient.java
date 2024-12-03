package com.example.tracking_service.clients;

import com.example.common_utils.configurations.FeignClientConfig;
import com.example.common_utils.enums.CreditType;
import com.example.common_utils.enums.DocumentType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "DOCUMENTS",
        path = "/api/v1/documents",
        configuration = {FeignClientConfig.class})
public interface DocumentFeignClient {
    @PostMapping("/missing")
    List<DocumentType> whichMissingDocuments(@RequestParam List<DocumentType> actualDocuments,
                                             @RequestParam CreditType creditType);
}
