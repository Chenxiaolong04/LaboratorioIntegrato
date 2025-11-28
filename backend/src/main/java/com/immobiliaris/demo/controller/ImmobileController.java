package com.immobiliaris.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.immobiliaris.demo.entity.Immobile;
import com.immobiliaris.demo.repository.ImmobileJpaRepository;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/immobili")
@CrossOrigin
public class ImmobileController {

    @Autowired
    private ImmobileJpaRepository immobileJpaRepository;

    // Salva un nuovo immobile e ritorna id, email proprietario e immobile
    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> saveImmobile(@RequestBody Immobile immobile) {
        // Imposta la provincia in base alla città
        if (immobile.getCitta() != null) {
            immobile.setProvincia(switch (immobile.getCitta().trim().toLowerCase()) {
                case "torino" -> "TO";
                case "cuneo" -> "CN";
                case "alessandria" -> "AL";
                case "asti" -> "AT";
                default -> null;
            });
        }
        Immobile saved = immobileJpaRepository.save(immobile);
        
        // Crea risposta con email proprietario e immobile
        Map<String, Object> response = new HashMap<>();
        response.put("email", saved.getProprietario() != null ? saved.getProprietario().getEmail() : null);
        response.put("immobile", saved);
        
        return ResponseEntity.ok(response);
    }
}
