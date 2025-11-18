package com.immobiliaris.demo.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.immobiliaris.demo.entity.Contratto;
import com.immobiliaris.demo.service.ContrattoService;
import com.immobiliaris.demo.service.EmailService;

@RestController
@RequestMapping("/api/contratti")
@CrossOrigin
public class ContrattoController {

    @Autowired
    private ContrattoService contrattoService;

    @Autowired
    private EmailService emailService;

    private static final Logger logger = LoggerFactory.getLogger(ContrattoController.class);

    // Genera PDF e invia per email (ADMIN e AGENT)
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    @PostMapping("/{id}/genera-pdf-email")
    public ResponseEntity<?> generaPDFEmail(@PathVariable Integer id) {
        try {
            Contratto contratto = contrattoService.getContrattoById(id);
            logger.info("Generazione PDF per contratto: {}", contratto.getNumeroContratto());

            String html = contrattoService.generaHTMLContratto(contratto);
            byte[] pdfBytes = contrattoService.generaPDF(html);

            String emailDestinatario = contratto.getUtente().getEmail();
            String oggetto = "Contratto Immobiliare - " + contratto.getNumeroContratto();
            String corpo = "Gentile " + contratto.getUtente().getNome() + ",\n\n" +
                          "Le allego il contratto immobiliare.\n\n" +
                          "Cordiali saluti,\n" +
                          "Immobiliaris";

            emailService.inviaEmailConAllegato(
                emailDestinatario,
                oggetto,
                corpo,
                pdfBytes,
                "contratto_" + contratto.getNumeroContratto() + ".pdf"
            );

            logger.info("Email inviata a: {}", emailDestinatario);
            return ResponseEntity.ok().body("{ \"success\": true, \"message\": \"PDF generato e inviato\" }");

        } catch (Exception e) {
            logger.error("Errore: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{ \"success\": false, \"message\": \"" + e.getMessage() + "\" }");
        }
    }

    // Scarica PDF (ADMIN e AGENT)
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    @PostMapping("/{id}/scarica-pdf")
    public ResponseEntity<?> scaricaPDF(@PathVariable Integer id) {
        try {
            Contratto contratto = contrattoService.getContrattoById(id);
            String html = contrattoService.generaHTMLContratto(contratto);
            byte[] pdfBytes = contrattoService.generaPDF(html);

            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                    "attachment; filename=\"contratto_" + contratto.getNumeroContratto() + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);

        } catch (Exception e) {
            logger.error("Errore: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Recupera contratto
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getContratto(@PathVariable Integer id) {
        try {
            Contratto contratto = contrattoService.getContrattoById(id);
            return ResponseEntity.ok(contratto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}