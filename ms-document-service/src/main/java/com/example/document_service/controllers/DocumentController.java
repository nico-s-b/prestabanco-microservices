package com.example.document_service.controllers;

import com.example.tingeso1.entities.Credit;
import com.example.tingeso1.entities.DocumentEntity;
import com.example.tingeso1.enums.DocumentType;
import com.example.tingeso1.services.CreditService;
import com.example.tingeso1.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/documents")
public class DocumentController {
    @Autowired
    DocumentService documentService;
    @Autowired
    private CreditService creditService;

    @GetMapping("/")
    public ResponseEntity<List<DocumentEntity>> listDocuments() {
        List<DocumentEntity> documents = documentService.getDocuments();
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentEntity> getDocumentById(@PathVariable Long id) {
        DocumentEntity document = documentService.getDocumentById(id);
        if (document != null) {
            return ResponseEntity.ok(document);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/credit/{id}")
    public ResponseEntity<List<DocumentEntity>> getDocumentByCreditId(@PathVariable Long id) {
        List<DocumentEntity> documents = documentService.getDocumentsByCreditId(id);
        if (!documents.isEmpty()) {
            return ResponseEntity.ok(documents);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/")
    public ResponseEntity<DocumentEntity> saveDocument(
            @RequestParam("id") Long id,
            @RequestParam("documentType") String documentType,
            @RequestParam("fileData") MultipartFile fileData) throws IOException {

        Credit credit = creditService.getCreditById(id);
        if (credit == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] fileBytes = fileData.getBytes();

        DocumentEntity document = new DocumentEntity();
        document.setCredit(credit);
        document.setDocumentType(DocumentType.valueOf(documentType));
        document.setFileData(fileBytes);

        DocumentEntity savedDocument = documentService.saveDocument(document);
        creditService.saveCredit(credit);

        return ResponseEntity.ok(savedDocument);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocumentById(@PathVariable Long id) throws Exception {
        var isDeleted = documentService.deleteDocument(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/docvalid")
    public ResponseEntity<Credit> documentValidation(@RequestParam Long id) {
        Credit credit = creditService.getById(id);
        if (credit == null) {
            return ResponseEntity.badRequest().body(null);
        }
        Credit creditChecked = creditValidationService.documentRevision(credit);
        return ResponseEntity.ok(creditChecked);
    }

}

