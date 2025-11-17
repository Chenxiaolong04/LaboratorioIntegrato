package com.immobiliaris.demo.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.immobiliaris.demo.repository.ValutazioneJpaRepository;
import com.immobiliaris.demo.entity.Valutazione;
import java.util.List;

@RestController
@RequestMapping("/api/valutazioni")
@CrossOrigin(origins = "*")
public class ValutazioneApiController {
    
    @Autowired
    private ValutazioneJpaRepository repository;

    @GetMapping("/all")
    public Object getAll() {
        List<Valutazione> list = repository.findAll();
        if(list.isEmpty()) {
            return new Response("error", "Nessuna valutazione trovata", null);
        }
        return new Response("success", "Valutazioni caricate", list);
    }

    @PostMapping("/save")
    public Object save(@RequestBody Valutazione valutazione) {
        Valutazione saved = repository.save(valutazione);
        return new Response("success", "Valutazione salvata", saved);
    }

    @GetMapping("/{id}")
    public Object getById(@PathVariable Integer id) {
        var val = repository.findById(id);
        if(val.isEmpty()) {
            return new Response("error", "Valutazione non trovata", null);
        }
        return new Response("success", "Valutazione trovata", val.get());
    }

    @PostMapping("/calcola")
    public Object calcolaValutazione(@RequestBody CalcoloRequest request) {
        try {
            // Calcolo semplice: quotazione media * metratura
            Double prezzo = request.quotazionMedia * request.metratura;
            
            // Aggiungi bonus/malus per caratteristiche
            if(request.ascensore) prezzo += prezzo * 0.08;
            if(request.balcone) prezzo += prezzo * 0.05;
            if(request.giardino) prezzo += prezzo * 0.10;
            if(request.cantina) prezzo -= prezzo * 0.03;
            
            prezzo += prezzo * (request.piano * 0.02);
            prezzo += prezzo * (request.stanze * 0.05);
            
            Integer prezzoFinale = prezzo.intValue();
            
            return new Response("success", "Valutazione calcolata", prezzoFinale);
        } catch (Exception e) {
            return new Response("error", e.getMessage(), null);
        }
    }

    static class CalcoloRequest {
        public Double quotazionMedia;
        public Integer metratura;
        public Integer piano;
        public Integer stanze;
        public boolean ascensore;
        public boolean balcone;
        public boolean giardino;
        public boolean cantina;
    }

    static class Response {
        public String status;
        public String message;
        public Object data;

        public Response(String status, String message, Object data) {
            this.status = status;
            this.message = message;
            this.data = data;
        }
    }
}