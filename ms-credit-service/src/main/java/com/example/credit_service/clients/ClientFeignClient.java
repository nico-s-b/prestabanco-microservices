package com.example.credit_service.clients;

import com.example.common_utils.configurations.FeignClientConfig;
import com.example.common_utils.dtos.ClientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "CLIENTS",
        path = "/api/v1/clients",
        configuration = {FeignClientConfig.class})
public interface ClientFeignClient {

    @GetMapping("/{id}")
    ClientDTO getById(@PathVariable Long id);

}
