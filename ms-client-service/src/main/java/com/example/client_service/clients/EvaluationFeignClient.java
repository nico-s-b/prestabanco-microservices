package com.example.client_service.clients;

import com.example.common_utils.configurations.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "ms-evaluation-service",
        path = "/api/v1/eval",
        configuration = {FeignClientConfig.class})
public interface EvaluationFeignClient {
    @PostMapping
    String createRecords(@RequestParam Long id);
}
