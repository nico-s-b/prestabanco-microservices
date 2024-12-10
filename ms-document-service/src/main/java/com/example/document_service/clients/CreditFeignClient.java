package com.example.document_service.clients;

import com.example.common_utils.configurations.FeignClientConfig;
import com.example.common_utils.dtos.CreditRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "CREDITS",
        path = "/api/v1/credits",
        configuration = {FeignClientConfig.class})
public interface CreditFeignClient {

    @GetMapping("/{id}")
    CreditRequest getById(@PathVariable Long id);
}
