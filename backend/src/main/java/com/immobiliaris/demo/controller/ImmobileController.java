package com.immobiliaris.demo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.immobiliaris.demo.model.Immobile;
import com.immobiliaris.demo.service.ImmobileService;

@RestController
@RequestMapping("/api/immobili")
@CrossOrigin(origins = "http://localhost:3000")
public class ImmobileController {
    
    private final ImmobileService service;

    public ImmobileController (ImmobileService service){
        this.service = service;
    }

    @PostMapping
    public Immobile aggiungiImmobile (@RequestBody Immobile immobile){
        return service.salvaImmobile(immobile);
    }
    
}
