package com.example.client_service.controllers;

import com.example.client_service.entities.Client;
import com.example.client_service.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {
    @Autowired
    ClientService clientService;

    @GetMapping("/")
    public ResponseEntity<List<Client>> list() {
        List<Client> users = clientService.getAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getById(@PathVariable Long id) {
        Client user = clientService.getById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<Client> register(@RequestBody Client client) {
        Client savedClient = clientService.saveOrUpdate(client);
        return ResponseEntity.ok(savedClient);
    }

    @PutMapping("/{clientId}")
    public ResponseEntity<Client> update(@PathVariable Long clientId, @RequestBody Client client) {
        client.setId(clientId);
        Client clientUpdated = clientService.saveOrUpdate(client);
        return ResponseEntity.ok(clientUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) throws Exception {
        var isDeleted = clientService.deleteById(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
