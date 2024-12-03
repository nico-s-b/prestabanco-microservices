package com.example.document_service.services;

import com.example.common_utils.dtos.DocumentUpdateDTO;
import com.example.common_utils.enums.CreditType;
import com.example.document_service.clients.CreditFeignClient;
import com.example.document_service.clients.TrackingFeignClient;
import com.example.document_service.entities.DocumentEntity;
import com.example.document_service.repositories.DocumentRepository;
import com.example.common_utils.enums.DocumentType;
import static com.example.common_utils.enums.CreditType.*;
import com.example.common_utils.dtos.CreditRequest;
import org.hibernate.sql.exec.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    CreditFeignClient creditFeignClient;

    @Autowired
    TrackingFeignClient trackingFeignClient;

    public List<DocumentEntity> getAll(){
        return (List<DocumentEntity>) documentRepository.findAll();
    }

    public DocumentEntity save(DocumentEntity document, CreditType creditType){
        document.setUploadDate(LocalDateTime.now());

        Long creditId = document.getCreditId();

        DocumentUpdateDTO notification = new DocumentUpdateDTO();
        notification.setCreditId(creditId);
        notification.setCreditType(creditType);
        notification.setDocumentTypes(getDocumentTypesByCreditId(creditId));
        trackingFeignClient.notifyDocumentsUpdated(notification);

        return documentRepository.save(document);
    }

    public DocumentEntity create(Long creditId, String documentType, MultipartFile fileData) throws IOException {
        CreditType creditType;
        try {
            CreditRequest credit = creditFeignClient.getById(creditId);
            if (credit == null) {
                throw new RuntimeException("Credit not found");
            }
            creditType = CreditType.valueOf(credit.getCreditType());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        DocumentEntity document = new DocumentEntity();
        byte[] fileBytes = fileData.getBytes();
        document.setFileData(fileBytes);
        document.setDocumentType(DocumentType.valueOf(documentType));
        document.setCreditId(creditId);
        return save(document, creditType);
    }

    public DocumentEntity getById(Long id){
        Optional<DocumentEntity> optionalRecord = documentRepository.findById(id);
        return optionalRecord.orElseThrow(() -> new ExecutionException("DocumentEntity not found for this id :: " + id));
    }

    public List<DocumentEntity> getDocumentsByCreditId(Long creditId){
        return documentRepository.findAllByCreditId(creditId);
    }

    private List<DocumentType> getDocumentTypesByCreditId(Long creditId){
        List<DocumentType> documentTypes = new ArrayList<>();
        List<DocumentEntity> documents = getDocumentsByCreditId(creditId);
        for (DocumentEntity document : documents) {
            documentTypes.add(document.getDocumentType());
        }
        return documentTypes;
    }

    public boolean delete(Long id) throws Exception {
        try{
            documentRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public List<DocumentType> whichMissingDocuments(List<DocumentType> actualDocuments, CreditType creditType){
        List<DocumentType> missing = new ArrayList<>();

        if (actualDocuments == null || creditType == null) {
            return missing;
        }

        List<DocumentType> requiredDocuments = new ArrayList<>();
        if (creditType.equals(CreditType.COMERCIAL)) {
            requiredDocuments = List.of(
                    DocumentType.FINANCIALSTATUSREPORT,
                    DocumentType.INCOMECERTIFY,
                    DocumentType.VALUATIONCERTIFY,
                    DocumentType.BUSINESSPLAN
            );
        } else if (creditType.equals(FIRSTHOME)) {
            requiredDocuments = List.of(
                    DocumentType.INCOMECERTIFY,
                    DocumentType.VALUATIONCERTIFY,
                    DocumentType.CREDITREPORT
            );
        } else if (creditType.equals(SECONDHOME)) {
            requiredDocuments = List.of(
                    DocumentType.INCOMECERTIFY,
                    DocumentType.VALUATIONCERTIFY,
                    DocumentType.FIRSTHOUSEDEED,
                    DocumentType.CREDITREPORT
            );
        } else if (creditType.equals(REMODELING)) {
            requiredDocuments = List.of(
                    DocumentType.INCOMECERTIFY,
                    DocumentType.REMODELINGBUDGET,
                    DocumentType.UPDATEDVALUATIONCERTIFY
            );
        }

        for (DocumentType required : requiredDocuments) {
            if (!actualDocuments.contains(required)) {
                missing.add(required);
            }
        }

        return missing;
    }

}
