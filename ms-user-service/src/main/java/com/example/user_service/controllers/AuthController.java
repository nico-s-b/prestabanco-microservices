package com.example.user_service.controllers;

import com.example.user_service.entities.Excecutive;
import com.example.user_service.services.ClientService;
import com.example.user_service.entities.Client;
import com.example.common_utils.configurations.JwtUtil;
import com.example.user_service.dtos.LoginRequest;
import com.example.user_service.services.ExcecutiveService;
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
    private ExcecutiveService excecutiveService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Object user = authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());

        if (user != null) {
            String token = jwtUtil.generateToken(getEmail(user));
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("userId", String.valueOf(getId(user)));
            response.put("userType", user instanceof Client ? "CLIENT" : "EXECUTIVE");
            response.put("name", getName(user));
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

    private Object authenticateUser(String email, String password) {
        // Intenta autenticar como Cliente
        Client client = clientService.authenticate(email, password);
        if (client != null) {
            return client;
        }
        // Intenta autenticar como Ejecutivo
        return excecutiveService.authenticate(email, password);
    }

    private Object findUserByEmail(String email) {
        // Busca primero en Clientes, luego en Ejecutivos
        Client client = clientService.getByEmail(email);
        if (client != null) {
            return client;
        }
        return excecutiveService.getByEmail(email);
    }

    private String getEmail(Object user) {
        if (user instanceof Client) {
            return ((Client) user).getEmail();
        } else if (user instanceof Excecutive) {
            return ((Excecutive) user).getEmail();
        }
        throw new IllegalArgumentException("Tipo de usuario no soportado");
    }

    private Long getId(Object user) {
        if (user instanceof Client) {
            return ((Client) user).getId();
        } else if (user instanceof Excecutive) {
            return ((Excecutive) user).getId();
        }
        throw new IllegalArgumentException("Tipo de usuario no soportado");
    }

    private String getName(Object user) {
        if (user instanceof Client) {
            return ((Client) user).getName();
        } else if (user instanceof Excecutive) {
            return ((Excecutive) user).getName();
        }
        throw new IllegalArgumentException("Tipo de usuario no soportado");
    }
}
