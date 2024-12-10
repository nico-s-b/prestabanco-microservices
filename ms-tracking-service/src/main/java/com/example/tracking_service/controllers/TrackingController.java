package com.example.tracking_service.controllers;

import com.example.common_utils.dtos.DocumentUpdateDTO;
import com.example.common_utils.dtos.TrackingRequest;
import com.example.common_utils.enums.CreditState;
import com.example.tracking_service.entities.CreditTrack;
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

    @GetMapping("/{creditId}")
    public ResponseEntity<CreditTrack> getTracking(@PathVariable Long creditId) {
        CreditTrack track = trackingService.getByCreditId(creditId);
        return ResponseEntity.ok().body(track);
    }

    @PostMapping("/")
    public ResponseEntity<Void> createTracking(@RequestBody TrackingRequest request) {
        trackingService.createAndSave(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("cancel/{creditId}")
    public ResponseEntity<Void> cancelState(@PathVariable Long creditId) {
        trackingService.updateCreditState(creditId, CreditState.CANCELLED);
        return ResponseEntity.ok().build();
    }

    @PutMapping("update/{creditId}")
    public ResponseEntity<Void> updateState(@PathVariable Long creditId, @RequestParam String state) {
        trackingService.updateCreditState(creditId, CreditState.valueOf(state));
        return ResponseEntity.ok().build();
    }

    @PostMapping("updateComments/{creditId}")
    public ResponseEntity<Void> updateComments(@PathVariable Long creditId,
                                               @RequestParam String comments,
                                                @RequestParam String state) {
        trackingService.updateCreditWithComments(creditId, CreditState.valueOf(state), comments);
        return ResponseEntity.ok().build();
    }
}