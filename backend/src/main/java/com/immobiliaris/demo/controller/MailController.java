package com.immobiliaris.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.immobiliaris.demo.service.EmailService;
import com.immobiliaris.demo.dto.ValutazioneMailRequest;
import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/api/mail")
public class MailController {

    private final EmailService emailService;

    public MailController(EmailService emailService) {
        this.emailService = emailService;
    }
    
    @PostMapping("/send-valutazione")
    public ResponseEntity<?> sendValutazioneRecap(@RequestBody ValutazioneMailRequest request) {
        try {
            emailService.sendValutazioneRecap(request.getIdValutazione());
            return ResponseEntity.ok("Email riepilogativa inviata con successo");
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body("Errore nell'invio della mail: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body("Errore: " + e.getMessage());
        }
    }
}
