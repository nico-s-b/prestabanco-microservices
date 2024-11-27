package com.example.evaluation_service.clients;

import com.example.credit_service.entities.Credit;
import com.example.common_utils.configurations.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "ms-credit-service",
        path = "/api/v1/credit",
        configuration = {FeignClientConfig.class})
public interface CreditFeignClient {

    @GetMapping("/{id}")
    public ResponseEntity<Credit> getById(@PathVariable Long id);
}
