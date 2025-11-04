package com.immobiliaris.demo.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminApiController {

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Benvenuto amministratore");
        response.put("user", authentication.getName());
        response.put("role", "ADMIN");
        
        // Qui puoi aggiungere statistiche, dati dashboard, ecc.
        response.put("stats", Map.of(
            "totalUsers", 0,
            "totalProperties", 0,
            "pendingRequests", 0
        ));
        
        return ResponseEntity.ok(response);
    }
}
