package com.example.document_service.services;

import com.example.tingeso1.entities.Credit;
import com.example.tingeso1.entities.DocumentEntity;
import com.example.tingeso1.enums.DocumentType;
import com.example.tingeso1.repositories.DocumentRepository;
import org.hibernate.sql.exec.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.tingeso1.enums.CreditType.*;

@Service
public class DocumentService {

    @Autowired
    DocumentRepository documentRepository;

    public ArrayList<DocumentEntity> getDocuments(){
        return (ArrayList<DocumentEntity>) documentRepository.findAll();
    }

    public DocumentEntity saveDocument(DocumentEntity document){
        document.setUploadDate(ZonedDateTime.now());
        return documentRepository.save(document);
    }

    public DocumentEntity getDocumentById(Long id){
        Optional<DocumentEntity> optionalRecord = documentRepository.findById(id);
        return optionalRecord.orElseThrow(() -> new ExecutionException("DocumentEntity not found for this id :: " + id));
    }

    public List<DocumentEntity> getDocumentsByCreditId(Long creditId){
        return documentRepository.findAllByCreditId(creditId);
    }

    public boolean deleteDocument(Long id) throws Exception {
        try{
            documentRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    ArrayList<DocumentType> whichMissingDocuments(Credit credit){
        ArrayList<DocumentEntity> documents = (ArrayList<DocumentEntity>) credit.getDocuments();
        ArrayList<DocumentType> missing = new ArrayList<>();

        if (documents == null || credit == null || credit.getCreditType() == null) {
            return missing;
        }

        if (credit.getCreditType().equals(COMERCIAL)){
            if (documents.stream().noneMatch(documentEntity -> DocumentType.FINANCIALSTATUSREPORT.equals(documentEntity.getDocumentType()))) {
                missing.add(DocumentType.FINANCIALSTATUSREPORT);
            }
            if (documents.stream().noneMatch(documentEntity -> DocumentType.INCOMECERTIFY.equals(documentEntity.getDocumentType()))) {
                missing.add(DocumentType.INCOMECERTIFY);
            }
            if (documents.stream().noneMatch(documentEntity -> DocumentType.VALUATIONCERTIFY.equals(documentEntity.getDocumentType()))) {
                missing.add(DocumentType.VALUATIONCERTIFY);
            }
            if (documents.stream().noneMatch(documentEntity -> DocumentType.BUSINESSPLAN.equals(documentEntity.getDocumentType()))) {
                missing.add(DocumentType.BUSINESSPLAN);
            }
        } else if (credit.getCreditType().equals(FIRSTHOME)) {
            if (documents.stream().noneMatch(documentEntity -> DocumentType.INCOMECERTIFY.equals(documentEntity.getDocumentType()))) {
                missing.add(DocumentType.INCOMECERTIFY);
            }
            if (documents.stream().noneMatch(documentEntity -> DocumentType.VALUATIONCERTIFY.equals(documentEntity.getDocumentType()))) {
                missing.add(DocumentType.VALUATIONCERTIFY);
            }
            if (documents.stream().noneMatch(documentEntity -> DocumentType.CREDITREPORT.equals(documentEntity.getDocumentType()))) {
                missing.add(DocumentType.CREDITREPORT);
            }
        } else if (credit.getCreditType().equals(SECONDHOME)) {
            if (documents.stream().noneMatch(documentEntity -> DocumentType.INCOMECERTIFY.equals(documentEntity.getDocumentType()))) {
                missing.add(DocumentType.INCOMECERTIFY);
            }
            if (documents.stream().noneMatch(documentEntity -> DocumentType.VALUATIONCERTIFY.equals(documentEntity.getDocumentType()))) {
                missing.add(DocumentType.VALUATIONCERTIFY);
            }
            if (documents.stream().noneMatch(documentEntity -> DocumentType.FIRSTHOUSEDEED.equals(documentEntity.getDocumentType()))) {
                missing.add(DocumentType.FIRSTHOUSEDEED);
            }
            if (documents.stream().noneMatch(documentEntity -> DocumentType.CREDITREPORT.equals(documentEntity.getDocumentType()))) {
                missing.add(DocumentType.CREDITREPORT);
            }
        } else if (credit.getCreditType().equals(REMODELING)) {
            if (documents.stream().noneMatch(documentEntity -> DocumentType.INCOMECERTIFY.equals(documentEntity.getDocumentType()))) {
                missing.add(DocumentType.INCOMECERTIFY);
            }
            if (documents.stream().noneMatch(documentEntity -> DocumentType.REMODELINGBUDGET.equals(documentEntity.getDocumentType()))) {
                missing.add(DocumentType.REMODELINGBUDGET);
            }
            if (documents.stream().noneMatch(documentEntity -> DocumentType.UPDATEDVALUATIONCERTIFY.equals(documentEntity.getDocumentType()))) {
                missing.add(DocumentType.UPDATEDVALUATIONCERTIFY);
            }
        }
        return missing;
    }
}
