package com.example.evaluation_service.clients;

import com.example.common_utils.dtos.CreditRequest;
import com.example.common_utils.configurations.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "CREDITS",
        path = "/api/v1/credit",
        configuration = {FeignClientConfig.class})
public interface CreditFeignClient {

    @GetMapping("/{id}")
    public CreditRequest getById(@PathVariable Long id);
}
