package com.immobiliaris.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.immobiliaris.demo.entity.Immobile;
import com.immobiliaris.demo.repository.ImmobileJpaRepository;

@RestController
@RequestMapping("/api/immobili")
@CrossOrigin
public class ImmobileController {

    @Autowired
    private ImmobileJpaRepository immobileJpaRepository;

    // Salva un nuovo immobile
    @PostMapping("/save")
    public ResponseEntity<Immobile> saveImmobile(@RequestBody Immobile immobile) {
        // Imposta la provincia in base alla città
        if (immobile.getCitta() != null) {
            switch (immobile.getCitta().trim().toLowerCase()) {
                case "torino":
                    immobile.setProvincia("TO");
                    break;
                case "cuneo":
                    immobile.setProvincia("CN");
                    break;
                case "alessandria":
                    immobile.setProvincia("AL");
                    break;
                case "asti":
                    immobile.setProvincia("AT");
                    break;
                default:
                    immobile.setProvincia(null); // oppure puoi gestire errore/altro
            }
        }
        Immobile saved = immobileJpaRepository.save(immobile);
        return ResponseEntity.ok(saved);
    }
}
