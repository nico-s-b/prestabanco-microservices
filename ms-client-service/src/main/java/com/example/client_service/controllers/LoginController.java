package com.example.client_service.controllers;

import com.example.client_service.services.ClientService;
import com.example.client_service.entities.Client;
import com.example.common_utils.configurations.JwtUtil;
import com.example.client_service.dtos.LoginRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author nic_s
 */
@RestController
@RequestMapping("/api/auth")
@SessionAttributes("userId")
public class LoginController {

    private final ClientService clientService;
    private final JwtUtil jwtUtil;

    public LoginController(ClientService clientService, JwtUtil jwtUtil) {
        this.clientService = clientService;
        this.jwtUtil = jwtUtil;
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Client user = clientService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());

        if (user != null) {
            String token = jwtUtil.generateToken(user.getEmail());
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("userId", String.valueOf(user.getId()));
            response.put("userType", user instanceof Client ? "CLIENT" : "EXECUTIVE");
            response.put("name", user.getName());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }

    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no proporcionado o mal formado");
        }

        String token = authHeader.substring(7);
        try {
            String username = jwtUtil.extractUsername(token);
            Client user = clientService.getByEmail(username);
            return ResponseEntity.ok(user != null ? user.getId() : "Usuario no encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logout exitoso");
    }
}
