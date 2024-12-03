package com.example.document_service.clients;

import com.example.common_utils.configurations.FeignClientConfig;
import com.example.common_utils.dtos.DocumentUpdateDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "TRACKING",
        path = "/api/v1/tracking",
        configuration = {FeignClientConfig.class})
public interface TrackingFeignClient {
    @PostMapping("/doc-update")
    void notifyDocumentsUpdated(@RequestBody DocumentUpdateDTO notification);

}
