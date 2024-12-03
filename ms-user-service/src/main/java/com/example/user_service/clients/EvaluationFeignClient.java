package com.example.user_service.clients;

import com.example.common_utils.configurations.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "EVALUATION",
        path = "/api/v1/eval",
        configuration = {FeignClientConfig.class})
public interface EvaluationFeignClient {
    @PostMapping
    String createRecords(@RequestParam Long id);
}
