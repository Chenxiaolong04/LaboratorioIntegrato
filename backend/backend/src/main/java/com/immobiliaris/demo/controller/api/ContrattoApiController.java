package com.immobiliaris.demo.controller.api;

import com.immobiliaris.demo.entity.Contratto;
import com.immobiliaris.demo.entity.Valutazione;
import com.immobiliaris.demo.repository.ContrattoJpaRepository;
import com.immobiliaris.demo.repository.ValutazioneJpaRepository;
import com.immobiliaris.demo.service.PdfContrattoService;
import com.immobiliaris.demo.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/contratti")
@CrossOrigin(origins = "*")
public class ContrattoApiController {

    @Autowired
    private ContrattoJpaRepository contrattoRepository;
    
    @Autowired
    private ValutazioneJpaRepository valutazioneRepository;

    @Autowired
    private PdfContrattoService pdfContrattoService;
    
    @Autowired
    private EmailService emailService;

    /**
     * GET /api/contratti/test
     * Endpoint di test per verificare che il controller funzioni
     */
    @GetMapping("/test")
    public ResponseEntity<?> test() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "ok");
        response.put("message", "ContrattoApiController è attivo!");
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/contratti/valutazione/{idValutazione}/pdf
     * Crea il contratto dalla valutazione, genera il PDF e lo invia via email
     */
    @GetMapping("/valutazione/{idValutazione}/pdf")
    public ResponseEntity<?> generaContrattoPdf(@PathVariable Integer idValutazione) {
        try {
            // Trova la valutazione
            Optional<Valutazione> valutazioneOpt = valutazioneRepository.findById(idValutazione);
            
            if (valutazioneOpt.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Valutazione non trovata con id: " + idValutazione);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            
            Valutazione valutazione = valutazioneOpt.get();
            
            // Verifica che la valutazione abbia immobile e agente
            if (valutazione.getImmobile() == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "La valutazione non ha un immobile associato");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            if (valutazione.getAgente() == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "La valutazione non ha un agente assegnato. Assegnare prima un agente.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            // Cerca o crea il contratto associato a questa valutazione
            Contratto contratto = contrattoRepository.findByValutazioneId(idValutazione)
                .orElseGet(() -> {
                    // Crea un nuovo contratto
                    Contratto nuovoContratto = new Contratto();
                    nuovoContratto.setImmobile(valutazione.getImmobile());
                    nuovoContratto.setUtente(valutazione.getImmobile().getProprietario());
                    nuovoContratto.setAgente(valutazione.getAgente());
                    nuovoContratto.setValutazione(valutazione);
                    nuovoContratto.setDataInizio(LocalDateTime.now());
                    nuovoContratto.setDataFine(LocalDateTime.now().plusMonths(6)); // Default 6 mesi
                    nuovoContratto.setPercentualeCommissione(3.0); // Default 3%
                    // Assumiamo che esista uno stato "attivo" con ID 1
                    // Se hai StatoContratto entity, dovresti recuperarlo dal repository
                    return contrattoRepository.save(nuovoContratto);
                });
            
            // Genera il PDF
            byte[] pdfBytes = pdfContrattoService.generaContrattoPdf(contratto);
            
            // Invia email con PDF allegato a proprietario e agente
            try {
                emailService.sendContrattoPdf(contratto, pdfBytes);
                
                // Restituisce messaggio di successo
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Contratto generato e inviato via email con successo");
                response.put("destinatari", Map.of(
                    "proprietario", contratto.getUtente().getEmail(),
                    "agente", contratto.getAgente().getEmail()
                ));
                response.put("valutazioneId", idValutazione);
                response.put("contrattoId", contratto.getId());
                return ResponseEntity.ok(response);
                
            } catch (Exception emailEx) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Errore invio email: " + emailEx.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
            }
                    
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Errore nella generazione del PDF: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * GET /api/contratti/valutazione/{idValutazione}/pdf/preview
     * Genera il PDF basato sulla valutazione (stesso comportamento di /pdf)
     */
    @GetMapping("/valutazione/{idValutazione}/pdf/preview")
    public ResponseEntity<?> visualizzaContrattoPdf(@PathVariable Integer idValutazione) {
        try {
            // Trova la valutazione
            Optional<Valutazione> valutazioneOpt = valutazioneRepository.findById(idValutazione);
            
            if (valutazioneOpt.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Valutazione non trovata con id: " + idValutazione);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            
            // Cerca il contratto associato a questa valutazione
            Contratto contratto = contrattoRepository.findByValutazioneId(idValutazione)
                .orElseThrow(() -> new RuntimeException(
                    "Nessun contratto trovato per la valutazione ID: " + idValutazione + 
                    ". Creare prima un contratto associato a questa valutazione."
                ));
            
            byte[] pdfBytes = pdfContrattoService.generaContrattoPdf(contratto);
            
            // Invia email con PDF allegato a proprietario e agente
            try {
                emailService.sendContrattoPdf(contratto, pdfBytes);
                
                // Restituisce messaggio di successo
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Contratto generato e inviato via email con successo");
                response.put("destinatari", Map.of(
                    "proprietario", contratto.getUtente().getEmail(),
                    "agente", contratto.getAgente().getEmail()
                ));
                response.put("valutazioneId", idValutazione);
                response.put("contrattoId", contratto.getId());
                return ResponseEntity.ok(response);
                
            } catch (Exception emailEx) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Errore invio email: " + emailEx.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
            }
                    
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Errore nella generazione del PDF: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
