package com.immobiliaris.demo.controller.api;

import com.immobiliaris.demo.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminApiController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard(Authentication authentication) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Benvenuto amministratore");
        response.put("user", authentication.getName());
        response.put("role", "ADMIN");
        
        // Ottieni statistiche dal Service
        Map<String, Integer> stats = statisticsService.getAdminStatistics();
        response.put("statistics", stats);
        
        return ResponseEntity.ok(response);
    }
}
