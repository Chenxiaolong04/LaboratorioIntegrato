package com.immobiliaris.demo.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/agent")
public class AgentApiController {

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Benvenuto agente");
        response.put("user", authentication.getName());
        response.put("role", "AGENT");
        
        return ResponseEntity.ok(response);
    }
}
