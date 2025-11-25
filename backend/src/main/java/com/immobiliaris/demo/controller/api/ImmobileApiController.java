package com.immobiliaris.demo.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.immobiliaris.demo.repository.ImmobileJpaRepository;
import com.immobiliaris.demo.repository.StatoImmobileRepository;
import com.immobiliaris.demo.entity.Immobile;
import com.immobiliaris.demo.entity.StatoImmobile;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/immobili")
@CrossOrigin(origins = "*")
public class ImmobileApiController {
    
    @Autowired
    private ImmobileJpaRepository repository;
    
    @Autowired
    private StatoImmobileRepository statoRepository;

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        try {
            List<Immobile> list = repository.findAll();
            if(list.isEmpty()) {
                return ResponseEntity.ok(new Response("error", "Nessun immobile trovato", null));
            }
            return ResponseEntity.ok(new Response("success", "Immobili caricati", list));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Response("error", "Errore recupero immobili: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        try {
            if(id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("error", "ID non valido", null));
            }
            var immobile = repository.findById(id);
            if(immobile.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Response("error", "Immobile non trovato", null));
            }
            return ResponseEntity.ok(new Response("success", "Immobile trovato", immobile.get()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Response("error", "Errore recupero immobile: " + e.getMessage(), null));
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody ImmobileRequest request) {
        try {
            // Validazioni
            if(request.via == null || request.via.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("error", "Via obbligatoria", null));
            }
            if(request.cap == null || request.cap.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("error", "CAP obbligatorio", null));
            }
            if(request.citta == null || request.citta.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("error", "Città obbligatoria", null));
            }
            if(request.metratura == null || request.metratura <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("error", "Metratura deve essere > 0", null));
            }
            if(request.stanze == null || request.stanze <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("error", "Stanze deve essere > 0", null));
            }
            if(request.piano == null || request.piano < 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("error", "Piano deve essere >= 0", null));
            }

            // Crea immobile
            Immobile immobile = new Immobile();
            immobile.setVia(request.via);
            immobile.setCap(request.cap);
            immobile.setCitta(request.citta);
            immobile.setProvincia(request.provincia);
            immobile.setMetratura(request.metratura);
            immobile.setStanze(request.stanze);
            immobile.setPiano(request.piano);
            immobile.setAscensore(request.ascensore != null ? request.ascensore : false);
            immobile.setBalcone(request.balcone != null ? request.balcone : false);
            immobile.setGiardino(request.giardino != null ? request.giardino : false);
            immobile.setCantina(request.cantina != null ? request.cantina : false);
            immobile.setTerrazzo(request.terrazzo != null ? request.terrazzo : false);
            immobile.setGarage(request.garage != null ? request.garage : false);
            immobile.setTipologia(request.tipologia);
            immobile.setCondizioni(request.condizioni);
            immobile.setBagni(request.bagni);
            immobile.setRiscaldamento(request.riscaldamento);
            immobile.setDescrizione(request.descrizione);
            immobile.setDataInserimento(LocalDate.now());
            
            // Assegna stato di default "disponibile"
            StatoImmobile stato = statoRepository.findByNome("disponibile")
                .orElse(null);
            if(stato == null) {
                stato = new StatoImmobile();
                stato.setNome("disponibile");
                stato.setDescrizione("Immobile disponibile");
                stato = statoRepository.save(stato);
            }
            immobile.setStatoImmobile(stato);
            
            Immobile saved = repository.save(immobile);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Response("success", "Immobile salvato", saved));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Response("error", "Errore salvataggio immobile: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody ImmobileRequest request) {
        try {
            if(id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("error", "ID non valido", null));
            }
            
            var immobileOpt = repository.findById(id);
            if(immobileOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Response("error", "Immobile non trovato", null));
            }
            
            Immobile immobile = immobileOpt.get();
            
            // Aggiorna i campi
            if(request.via != null && !request.via.trim().isEmpty()) {
                immobile.setVia(request.via);
            }
            if(request.cap != null && !request.cap.trim().isEmpty()) {
                immobile.setCap(request.cap);
            }
            if(request.citta != null && !request.citta.trim().isEmpty()) {
                immobile.setCitta(request.citta);
            }
            if(request.provincia != null && !request.provincia.trim().isEmpty()) {
                immobile.setProvincia(request.provincia);
            }
            if(request.metratura != null && request.metratura > 0) {
                immobile.setMetratura(request.metratura);
            }
            if(request.stanze != null && request.stanze > 0) {
                immobile.setStanze(request.stanze);
            }
            if(request.piano != null && request.piano >= 0) {
                immobile.setPiano(request.piano);
            }
            if(request.ascensore != null) immobile.setAscensore(request.ascensore);
            if(request.balcone != null) immobile.setBalcone(request.balcone);
            if(request.giardino != null) immobile.setGiardino(request.giardino);
            if(request.cantina != null) immobile.setCantina(request.cantina);
            if(request.terrazzo != null) immobile.setTerrazzo(request.terrazzo);
            if(request.garage != null) immobile.setGarage(request.garage);
            if(request.tipologia != null) immobile.setTipologia(request.tipologia);
            if(request.condizioni != null) immobile.setCondizioni(request.condizioni);
            if(request.bagni != null) immobile.setBagni(request.bagni);
            if(request.riscaldamento != null) immobile.setRiscaldamento(request.riscaldamento);
            if(request.descrizione != null) immobile.setDescrizione(request.descrizione);
            
            Immobile updated = repository.save(immobile);
            return ResponseEntity.ok(new Response("success", "Immobile aggiornato", updated));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Response("error", "Errore aggiornamento immobile: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            if(id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("error", "ID non valido", null));
            }
            
            var immobile = repository.findById(id);
            if(immobile.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Response("error", "Immobile non trovato", null));
            }
            
            repository.deleteById(id);
            return ResponseEntity.ok(new Response("success", "Immobile eliminato", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Response("error", "Errore eliminazione immobile: " + e.getMessage(), null));
        }
    }

    static class ImmobileRequest {
        public String via;
        public String cap;
        public String citta;
        public String provincia;
        public String tipologia;
        public Integer metratura;
        public String condizioni;
        public Integer stanze;
        public Integer bagni;
        public String riscaldamento;
        public Integer piano;
        public Boolean ascensore;
        public Boolean garage;
        public Boolean giardino;
        public Boolean balcone;
        public Boolean terrazzo;
        public Boolean cantina;
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