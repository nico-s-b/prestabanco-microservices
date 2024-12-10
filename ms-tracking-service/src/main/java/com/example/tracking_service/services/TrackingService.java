package com.example.tracking_service.services;

import com.example.common_utils.dtos.DocumentUpdateDTO;
import com.example.common_utils.dtos.TrackingRequest;
import com.example.common_utils.enums.CreditState;
import com.example.common_utils.enums.CreditType;
import com.example.common_utils.enums.DocumentType;
import com.example.tracking_service.clients.DocumentFeignClient;
import com.example.tracking_service.clients.ExecutiveFeignClient;
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

    @Autowired
    ExecutiveFeignClient executiveFeignClient;

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
        CreditTrack track = trackingRepository.findByCreditId(creditId);
        int docsNeeded = docsNeeded(creditType);
        track.setDocsUploaded( docsNeeded - missingDocuments.size() );
        if (missingDocuments.isEmpty()) {
            updateCreditState(creditId, CreditState.EVALUATING);// E3. En Evaluación
            assignExecutiveToTrack(creditId);
        } else {
            updateCreditState(creditId, CreditState.PENDINGDOCUMENTATION); // E2. Pendiente de Documentación
        }
    }

    public void assignExecutiveToTrack(Long creditId){
        CreditTrack track = trackingRepository.findByCreditId(creditId);
        Long pickedExecutive = executiveFeignClient.getExecutiveToAssign();
        if (track.getExecutiveId() == null) {
            track.setLastUpdateDate(LocalDateTime.now());
            track.setExecutiveId(pickedExecutive);
            trackingRepository.save(track);
        }
    }

    public CreditTrack getByCreditId(Long creditId) {
        return trackingRepository.findByCreditId(creditId);
    }

    private int docsNeeded(CreditType creditType) {
        switch (creditType) {
            case COMERCIAL, SECONDHOME -> {
                return 4;
            }
            case FIRSTHOME, REMODELING -> {
                return 3;
            }
        }
        return 0;
    }

    public void createAndSave(TrackingRequest request) {
        CreditTrack tracking = new CreditTrack();
        tracking.setCreditId(request.getCreditId());
        tracking.setLastUpdateDate(request.getLastUpdateDate());
        tracking.setState(CreditState.INITIALREV);
        tracking.setDocsUploaded(0);
        trackingRepository.save(tracking);
    }
}
