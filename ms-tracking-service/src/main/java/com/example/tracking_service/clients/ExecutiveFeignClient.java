package com.example.tracking_service.clients;

import com.example.common_utils.configurations.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "USERS",
        path = "/api/v1/executives",
        configuration = {FeignClientConfig.class})
public interface ExecutiveFeignClient {
    @GetMapping("/assign")
    Long getExecutiveToAssign();
}
