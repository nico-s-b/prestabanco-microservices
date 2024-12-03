package com.example.tracking_service.services;

import com.example.common_utils.dtos.DocumentUpdateDTO;
import com.example.common_utils.enums.CreditState;
import com.example.common_utils.enums.CreditType;
import com.example.common_utils.enums.DocumentType;
import com.example.tracking_service.clients.DocumentFeignClient;
import com.example.tracking_service.entities.CreditTrack;
import com.example.tracking_service.repositories.TrackingRepository;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TrackingService {
    @Autowired
    TrackingRepository trackingRepository;

    @Autowired
    DocumentFeignClient documentFeignClient;

    public CreditTrack updateCreditState(Long creditId, CreditState newState) {
        CreditTrack track = trackingRepository.findByCreditId(creditId);
        if (track == null) {
            throw new NotFoundException("CreditTrack not found");
        }

        track.setState(newState);
        track.setLastUpdateDate(LocalDateTime.now());

        // (Opcional) Agregar evento al historial
        //TrackingEvent event = new TrackingEvent(track.getState(), newState, LocalDateTime.now(), "SYSTEM");
        //track.getHistory().add(event);

        return trackingRepository.save(track);
    }

    public void handleDocumentUpdate(DocumentUpdateDTO notification) {
        Long creditId = notification.getCreditId();
        CreditType creditType = notification.getCreditType();
        List<DocumentType> actualDocuments = notification.getDocumentTypes();

        List<DocumentType> missingDocuments = documentFeignClient.whichMissingDocuments(actualDocuments,creditType);

        if (missingDocuments.isEmpty()) {
            updateCreditState(creditId, CreditState.EVALUATING); // E3. En Evaluación
        } else {
            updateCreditState(creditId, CreditState.PENDINGDOCUMENTATION); // E2. Pendiente de Documentación
        }
    }
}
