package com.immobiliaris.demo.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.immobiliaris.demo.repository.ValutazioneJpaRepository;
import com.immobiliaris.demo.entity.Valutazione;
import com.immobiliaris.demo.entity.StatoValutazione;
import com.immobiliaris.demo.repository.StatoValutazioneRepository;
import com.immobiliaris.demo.entity.Immobile;
import com.immobiliaris.demo.repository.ImmobileJpaRepository;
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
    private ImmobileJpaRepository immobileRepository;

    @Autowired
    private com.immobiliaris.demo.repository.ZonaRepository zonaRepository;

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
            return ResponseEntity.ok(val.get());
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
            if(immobileId == null || immobileId <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("error", "ID immobile obbligatorio", null));
            }
            var immobileOpt = immobileRepository.findById(immobileId);
            if(immobileOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Response("error", "Immobile non trovato", null));
            }
            Immobile immobile = immobileOpt.get();

            List<Valutazione> valutazioniEsistenti = repository.findByImmobile(immobile);
            boolean giaValutato = !valutazioniEsistenti.isEmpty();
            if(giaValutato) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new Response("error", "Immobile già valutato", null));
            }

            Integer metratura = immobile.getMetratura();
            Integer piano = immobile.getPiano();
            Integer stanze = immobile.getStanze();
            Integer bagni = immobile.getBagni();

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
            if(bagni == null || bagni <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("error", "Bagni deve essere > 0", null));
            }

            String cap = immobile.getCap();
            var zonaOpt = zonaRepository.findByCap(cap);
            if(zonaOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Response("error", "Zona non trovata per il CAP: " + cap, null));
            }
            Double quotazioneBase = zonaOpt.get().getPrezzoMedioMq().doubleValue();

            // ===== FASE 0: ANALISI DI EFFICIENZA FUNZIONALE =====
            // Calcolo Coefficiente di Penalità Funzionale (C_Funzionale) basato su Metratura, Stanze, Bagni
            
            // 1. Verifica Adeguatezza Bagni (Criticità)
            // Bagni Minimi Richiesti = ceil(Metratura / 70)
            Integer bagniMinimiRichiesti = (int) Math.ceil(metratura / 70.0);
            Double moltiplicatoreBagni = 1.00;
            if(bagni < bagniMinimiRichiesti) {
                moltiplicatoreBagni = 0.95; // Penalità -5%
            }
            
            // 2. Verifica Efficienza Stanze (Sottodivisione/Sovradivisione)
            // Stanze Massime Funzionali = floor(Metratura / 20)
            Integer stanzeMaxFunzionali = (int) Math.floor(metratura / 20.0);
            Double moltiplicatoreStanze = 1.00;
            if(stanze > stanzeMaxFunzionali) {
                moltiplicatoreStanze = 0.97; // Penalità -3%
            }
            
            // 3. Calcolo C_Funzionale = Moltiplicatore Bagni × Moltiplicatore Stanze
            Double coefficienteFunzionale = moltiplicatoreBagni * moltiplicatoreStanze;

            // Coefficiente per condizioni dell'immobile
            Double coefficienteCondizioni = 1.00;
            String condizioni = immobile.getCondizioni();
            if(condizioni != null) {
                if("Nuovo".equalsIgnoreCase(condizioni)) {
                    coefficienteCondizioni = 1.15;
                } else if("Ottimo".equalsIgnoreCase(condizioni)) {
                    coefficienteCondizioni = 1.10;
                } else if("Buono".equalsIgnoreCase(condizioni)) {
                    coefficienteCondizioni = 1.00;
                } else if("Da ristrutturare".equalsIgnoreCase(condizioni) || "Da Ristrutturare".equalsIgnoreCase(condizioni)) {
                    coefficienteCondizioni = 0.75;
                }
            }

            // Coefficiente per tipologia immobile
            Double coefficienteTipologia = 1.00;
            String tipologia = immobile.getTipologia();
            if(tipologia != null) {
                if("Villa".equalsIgnoreCase(tipologia)) {
                    coefficienteTipologia = 1.30;
                } else if("Attico".equalsIgnoreCase(tipologia) || "Loft".equalsIgnoreCase(tipologia)) {
                    coefficienteTipologia = 1.12;
                } else if("Appartamento".equalsIgnoreCase(tipologia)) {
                    coefficienteTipologia = 1.00;
                }
            }

            Double coefficienteQualitativo = coefficienteCondizioni * coefficienteTipologia;

            // Percentuali accessori e caratteristiche aggiuntive
            Double percentualeAccessori = 0.0;
            if(immobile.getAscensore() != null && immobile.getAscensore()) percentualeAccessori += 0.08;
            if(immobile.getBalcone() != null && immobile.getBalcone()) percentualeAccessori += 0.05;
            if(immobile.getTerrazzo() != null && immobile.getTerrazzo()) percentualeAccessori += 0.12;
            if(immobile.getGiardino() != null && immobile.getGiardino()) percentualeAccessori += 0.10;
            if(immobile.getCantina() != null && immobile.getCantina()) percentualeAccessori += 0.03;
            if(immobile.getGarage() != null && immobile.getGarage()) percentualeAccessori += 0.15;
            
            // Per le ville: bonus fisso +10%; per altri: +2% per piano attuale
            Double percentualePiano = "Villa".equalsIgnoreCase(tipologia) ? 0.10 : (piano * 0.02);
            Double moltiplicatoreFinale = 1.0 + percentualeAccessori + percentualePiano;

            // Calcolo prezzo stimato: PFS = (Metratura × QuotazioneBase) × C_Funzionale × CoefficienteQualitativo × MoltiplicatoreFinale
            Double prezzo = (metratura * quotazioneBase) * coefficienteFunzionale * coefficienteQualitativo * moltiplicatoreFinale;

            Integer prezzoFinale = prezzo.intValue();

            Valutazione valutazione = new Valutazione();
            valutazione.setPrezzoAI(prezzoFinale);
            // Non impostare dataValutazione qui - verrà impostata quando l'agente prende in carico
            valutazione.setImmobile(immobile);

            StatoValutazione stato = statoRepository.findByNome("solo_AI")
                .orElse(null);
            if(stato == null) {
                stato = new StatoValutazione();
                stato.setNome("solo_AI");
                stato.setDescrizione("Valutazione generata dall'AI");
                stato = statoRepository.save(stato);
            }
            valutazione.setStatoValutazione(stato);
            valutazione.setDescrizione(request.descrizione != null ? 
                request.descrizione : 
                "Valutazione automatica - CAP: " + cap);

            Valutazione saved = repository.save(valutazione);

            Map<String, Object> result = new HashMap<>();
            result.put("prezzo", prezzoFinale);
            result.put("valutazioneId", saved.getId());
            result.put("stato", "solo_AI");
            result.put("cap", cap);
            result.put("quotazioneBase", quotazioneBase);
            result.put("coefficienteFunzionale", coefficienteFunzionale);
            result.put("bagniMinimiRichiesti", bagniMinimiRichiesti);
            result.put("stanzeMaxFunzionali", stanzeMaxFunzionali);
            result.put("coefficienteQualitativo", coefficienteQualitativo);
            result.put("moltiplicatoreFinale", moltiplicatoreFinale);
            result.put("immobileId", immobileId);

            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Response("success", "Valutazione calcolata e salvata", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Response("error", "Errore calcolo valutazione: " + e.getMessage(), null));
        }
    }

    public static class CalcoloRequest {
        public String descrizione;
    }

    public static class Response {
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