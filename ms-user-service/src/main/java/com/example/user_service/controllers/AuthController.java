package com.example.user_service.controllers;

import com.example.user_service.entities.Executive;
import com.example.user_service.services.ClientService;
import com.example.user_service.entities.Client;
import com.example.common_utils.configurations.JwtUtil;
import com.example.user_service.dtos.LoginRequest;
import com.example.user_service.services.ExecutiveService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AuthController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ExecutiveService executiveService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/clients/login")
    public ResponseEntity<?> loginClient(@RequestBody LoginRequest loginRequest) {
        Client client = clientService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());

        if (client != null) {
            String token = jwtUtil.generateToken(client.getEmail());
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("userId", String.valueOf(client.getId()));
            response.put("userType", "CLIENT");
            response.put("name", client.getName());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }


    @PostMapping("/executives/login")
    public ResponseEntity<?> loginExecutive(@RequestBody LoginRequest loginRequest) {
        Executive executive = executiveService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());

        if (executive != null) {
            String token = jwtUtil.generateToken(executive.getEmail());
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("userId", String.valueOf(executive.getId()));
            response.put("userType", "EXECUTIVE");
            response.put("name", executive.getName());
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
            Object user = findUserByEmail(username);
            if (user != null) {
                return ResponseEntity.ok(Map.of(
                        "userId", getId(user),
                        "userType", user instanceof Client ? "CLIENT" : "EXECUTIVE",
                        "name", getName(user)
                ));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logout exitoso");
    }

    private Object findUserByEmail(String email) {
        // Busca primero en Clientes, luego en Ejecutivos
        Client client = clientService.getByEmail(email);
        if (client != null) {
            return client;
        }
        return executiveService.getByEmail(email);
    }

    private Long getId(Object user) {
        if (user instanceof Client) {
            return ((Client) user).getId();
        } else if (user instanceof Executive) {
            return ((Executive) user).getId();
        }
        throw new IllegalArgumentException("Tipo de usuario no soportado");
    }

    private String getName(Object user) {
        if (user instanceof Client) {
            return ((Client) user).getName();
        } else if (user instanceof Executive) {
            return ((Executive) user).getName();
        }
        throw new IllegalArgumentException("Tipo de usuario no soportado");
    }
}
