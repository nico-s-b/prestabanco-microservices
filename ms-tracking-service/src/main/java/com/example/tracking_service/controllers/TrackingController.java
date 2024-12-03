package com.example.tracking_service.controllers;

import com.example.common_utils.dtos.DocumentUpdateDTO;
import com.example.tracking_service.services.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/tracking")
public class TrackingController {
    @Autowired
    TrackingService trackingService;

    @PostMapping("/doc-update")
    public ResponseEntity<Void> documentsUpdated(@RequestBody DocumentUpdateDTO notification) {
        trackingService.handleDocumentUpdate(notification);
        return ResponseEntity.ok().build();
    }
}