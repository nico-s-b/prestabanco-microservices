package com.example.document_service.controllers;

import com.example.common_utils.enums.CreditType;
import com.example.common_utils.enums.DocumentType;
import com.example.document_service.entities.DocumentEntity;
import com.example.document_service.services.DocumentService;
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

    @GetMapping("/")
    public ResponseEntity<List<DocumentEntity>> listDocuments() {
        List<DocumentEntity> documents = documentService.getAll();
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentEntity> getById(@PathVariable Long id) {
        DocumentEntity document = documentService.getById(id);
        return ResponseEntity.ok(document);
    }

    @GetMapping("/credit/{id}")
    public ResponseEntity<List<DocumentEntity>> getDocumentsByCreditId(@PathVariable Long id) {
        List<DocumentEntity> documents = documentService.getDocumentsByCreditId(id);
        return ResponseEntity.ok(documents);
    }

    @PutMapping("/")
    public ResponseEntity<DocumentEntity> save(
            @RequestParam("id") Long creditId,
            @RequestParam("documentType") String documentType,
            @RequestParam("fileData") MultipartFile fileData) throws IOException {
        DocumentEntity savedDocument = documentService.create(creditId, documentType, fileData);
        return ResponseEntity.ok(savedDocument);
    }

    @PutMapping("/replace")
    public ResponseEntity<Void> update(
            @RequestParam("id") Long id,
            @RequestParam("fileData") MultipartFile fileData) throws IOException {
        documentService.replace(id, fileData);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocumentById(@PathVariable Long id) throws Exception {
        documentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/missing")
    public List<DocumentType> whichMissingDocuments(@RequestParam List<DocumentType> actualDocuments,
                                                    @RequestParam CreditType creditType) {
        return documentService.whichMissingDocuments(actualDocuments, creditType);
    }

}

