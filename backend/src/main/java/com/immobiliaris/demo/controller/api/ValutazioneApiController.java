package com.immobiliaris.demo.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.immobiliaris.demo.repository.ValutazioneJpaRepository;
import com.immobiliaris.demo.entity.Valutazione;
import com.immobiliaris.demo.entity.StatoValutazione;
import com.immobiliaris.demo.entity.MacroareaUrbana;
import com.immobiliaris.demo.repository.StatoValutazioneRepository;
import com.immobiliaris.demo.repository.MacroareaUrbanaRepository;
import com.immobiliaris.demo.entity.Immobile;
import com.immobiliaris.demo.repository.ImmobileJpaRepository;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/valutazioni")
@CrossOrigin(origins = "*")
public class ValutazioneApiController {
    
    @Autowired
    private ValutazioneJpaRepository repository;
    
    @Autowired
    private StatoValutazioneRepository statoRepository;
    
    @Autowired
    private MacroareaUrbanaRepository macroareaRepository;

    @Autowired
    private ImmobileJpaRepository immobileRepository;

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        try {
            List<Valutazione> list = repository.findAll();
            if(list.isEmpty()) {
                return ResponseEntity.ok(new Response("error", "Nessuna valutazione trovata", null));
            }
            return ResponseEntity.ok(new Response("success", "Valutazioni caricate", list));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Response("error", "Errore recupero valutazioni: " + e.getMessage(), null));
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Valutazione valutazione) {
        try {
            if(valutazione == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("error", "Valutazione non può essere vuota", null));
            }
            Valutazione saved = repository.save(valutazione);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Response("success", "Valutazione salvata", saved));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Response("error", "Errore salvataggio valutazione: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        try {
            if(id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("error", "ID non valido", null));
            }
            var val = repository.findById(id);
            if(val.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Response("error", "Valutazione non trovata", null));
            }
            return ResponseEntity.ok(new Response("success", "Valutazione trovata", val.get()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Response("error", "Errore recupero valutazione: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            if(id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("error", "ID non valido", null));
            }
            var val = repository.findById(id);
            if(val.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Response("error", "Valutazione non trovata", null));
            }
            repository.deleteById(id);
            return ResponseEntity.ok(new Response("success", "Valutazione eliminata", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Response("error", "Errore eliminazione valutazione: " + e.getMessage(), null));
        }
    }

    @PostMapping("/calcola/{immobileId}")
    public ResponseEntity<?> calcolaValutazione(@PathVariable Integer immobileId, @RequestBody CalcoloRequest request) {
        try {
            // Valida immobileId
            if(immobileId == null || immobileId <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("error", "ID immobile obbligatorio", null));
            }
            
            // Recupera l'immobile
            var immobileOpt = immobileRepository.findById(immobileId);
            if(immobileOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Response("error", "Immobile non trovato", null));
            }
            Immobile immobile = immobileOpt.get();
            
            // Usa i dati dell'immobile
            Integer metratura = immobile.getMetratura();
            Integer piano = immobile.getPiano();
            Integer stanze = immobile.getStanze();
            
            if(metratura == null || metratura <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("error", "Metratura deve essere > 0", null));
            }
            if(piano == null || piano < 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("error", "Piano deve essere >= 0", null));
            }
            if(stanze == null || stanze <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("error", "Stanze deve essere > 0", null));
            }

            // Recupera la macroarea dal DB usando il CAP dell'immobile
            var macroareaOpt = macroareaRepository.findByCapRange(immobile.getCap());
            if(macroareaOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Response("error", "Macroarea non trovata per il CAP: " + immobile.getCap(), null));
            }
            MacroareaUrbana macroarea = macroareaOpt.get();
            
            // Usa la quotazione media della macroarea
            if(macroarea.getQuotazione_media() == null || macroarea.getQuotazione_media() <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("error", "Quotazione macroarea non valida", null));
            }

            // Calcolo: quotazione macroarea * metratura
            Double prezzo = macroarea.getQuotazione_media() * metratura;
            
            // Aggiungi bonus/malus per caratteristiche (usa valori dell'immobile)
            if(immobile.getAscensore() != null && immobile.getAscensore()) prezzo += prezzo * 0.08;
            if(immobile.getBalcone() != null && immobile.getBalcone()) prezzo += prezzo * 0.05;
            if(immobile.getGiardino() != null && immobile.getGiardino()) prezzo += prezzo * 0.10;
            if(immobile.getCantina() != null && immobile.getCantina()) prezzo -= prezzo * 0.03;
            
            // Bonus per piano
            prezzo += prezzo * (piano * 0.02);
            
            // Bonus per stanze
            prezzo += prezzo * (stanze * 0.05);
            
            Integer prezzoFinale = prezzo.intValue();
            
            // Crea e salva la valutazione nel DB
            Valutazione valutazione = new Valutazione();
            valutazione.setPrezzoAI(prezzoFinale);
            valutazione.setDataValutazione(LocalDate.now());
            valutazione.setImmobile(immobile); // ← COLLEGATO ALL'IMMOBILE!
            
            // Assegna stato "IN_VERIFICA" automaticamente
            StatoValutazione stato = statoRepository.findByNome("IN_VERIFICA")
                .orElse(null);
            if(stato == null) {
                stato = new StatoValutazione();
                stato.setNome("IN_VERIFICA");
                stato.setDescrizione("Valutazione in attesa di verifica");
                stato = statoRepository.save(stato);
            }
            valutazione.setStatoValutazione(stato);
            valutazione.setDescrizione(request.descrizione != null ? 
                request.descrizione : 
                "Valutazione automatica - Zona: " + macroarea.getNome_macroarea() + " (CAP: " + immobile.getCap() + ")");
            
            Valutazione saved = repository.save(valutazione);
            
            // Prepara risposta
            Map<String, Object> result = new HashMap<>();
            result.put("prezzo", prezzoFinale);
            result.put("valutazioneId", saved.getId());
            result.put("stato", "IN_VERIFICA");
            result.put("macroarea", macroarea.getNome_macroarea());
            result.put("quotazioneZona", macroarea.getQuotazione_media());
            result.put("immobileId", immobileId);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Response("success", "Valutazione calcolata e salvata", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Response("error", "Errore calcolo valutazione: " + e.getMessage(), null));
        }
    }

    static class CalcoloRequest {
        public String descrizione;
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