package com.example.evaluation_service.clients;

import com.example.common_utils.configurations.FeignClientConfig;
import com.example.common_utils.dtos.CreditRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "CALCULATION",
        path = "/api/v1/calculations",
        configuration = {FeignClientConfig.class})
public interface CalculationFeignClient {

    @PostMapping("/installment")
    Integer getInstallment(@RequestBody CreditRequest request);

    @PostMapping("/maxFinancing")
    Integer getMaxFinancing(@RequestBody CreditRequest request);

}
