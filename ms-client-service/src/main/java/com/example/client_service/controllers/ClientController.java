package com.example.client_service.controllers;

import com.example.client_service.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController("/ap1/v1/clients")
public class ClientController {
    @Autowired
    ClientService clientService;


}
