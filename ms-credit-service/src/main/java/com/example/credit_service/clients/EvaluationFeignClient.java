package com.example.credit_service.clients;

import com.example.common_utils.configurations.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "EVALUATION",
        path = "/api/v1/evals",
        configuration = {FeignClientConfig.class})
public interface EvaluationFeignClient {
    @PostMapping
    Void create(@RequestParam Long creditId, @RequestParam Long clientId);
}
