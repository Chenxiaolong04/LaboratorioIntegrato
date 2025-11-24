package com.immobiliaris.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.immobiliaris.demo.entity.User;
import com.immobiliaris.demo.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    // Registrazione nuovo utente (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            logger.info("JSON ricevuto: {}", user);
            logger.info("TipoUtente: {}", user.getTipoUtente());
            User registered = userService.registerUser(user, user.getTipoUtente().getIdTipo());
            return ResponseEntity.status(HttpStatus.CREATED).body(registered);
        } catch (RuntimeException e) {
            if(e.getMessage().contains("Email già registrata")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(errorResponse("Email già registrata", "DUPLICATE_EMAIL"));
            }
            if(e.getMessage().contains("Tipo utente non trovato")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(errorResponse("Tipo utente non trovato", "INVALID_USER_TYPE"));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorResponse(e.getMessage(), "REGISTRATION_ERROR"));
        }
    }

    // Recupera profilo personale (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/profile")
    public ResponseEntity<?> getOwnProfile(Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = userService.getUserByEmail(email);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorResponse("Utente non trovato", "USER_NOT_FOUND"));
        }
    }

    // Modifica profilo personale (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/profile")
    public ResponseEntity<?> updateOwnProfile(@RequestBody User updatedUser, Authentication authentication) {
        try {
            String email = authentication.getName();
            User updated = userService.updateOwnProfile(email, updatedUser);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            if(e.getMessage().contains("Email già in uso")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(errorResponse("Email già in uso", "EMAIL_IN_USE"));
            }
            if(e.getMessage().contains("Utente non trovato")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(errorResponse("Utente non trovato", "USER_NOT_FOUND"));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorResponse(e.getMessage(), "UPDATE_ERROR"));
        }
    }

    // Modifica utente per ID (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserAsAdmin(@PathVariable Long id, @RequestBody User updatedUser) {
        try {
            User updated = userService.updateUserAsAdmin(id, updatedUser);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            if(e.getMessage().contains("Email già in uso")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(errorResponse("Email già in uso", "EMAIL_IN_USE"));
            }
            if(e.getMessage().contains("Utente non trovato")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(errorResponse("Utente non trovato", "USER_NOT_FOUND"));
            }
            if(e.getMessage().contains("Tipo utente non trovato")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(errorResponse("Tipo utente non trovato", "INVALID_USER_TYPE"));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorResponse(e.getMessage(), "UPDATE_ERROR"));
        }
    }

    // Lista utenti (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse("Errore recupero utenti", "FETCH_ERROR"));
        }
    }

    // Recupero singolo utente (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            if(e.getMessage().contains("Utente non trovato")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(errorResponse("Utente non trovato", "USER_NOT_FOUND"));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse(e.getMessage(), "FETCH_ERROR"));
        }
    }

    // Elimina utente per ID (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(Map.of("success", true, "message", "Utente eliminato con successo"));
        } catch (RuntimeException e) {
            if(e.getMessage().contains("Utente non trovato")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(errorResponse("Utente non trovato", "USER_NOT_FOUND"));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse(e.getMessage(), "DELETE_ERROR"));
        }
    }

    // Helper per risposta errore standardizzata
    private Map<String, Object> errorResponse(String message, String code) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        response.put("code", code);
        return response;
    }
}
