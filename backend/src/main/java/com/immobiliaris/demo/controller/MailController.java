package com.immobiliaris.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.immobiliaris.demo.service.EmailService;
import com.immobiliaris.demo.dto.EmailRequest;

@RestController
@RequestMapping("/api/mail")
public class MailController {

    private final EmailService emailService;

    MailController(EmailService emailService) {
        this.emailService = emailService;
    }
    
    @GetMapping("/test")
    public ResponseEntity<?> testEndpoint() {
        return ResponseEntity.ok("Mail endpoint raggiungibile! ✅");
    }
    
    @PostMapping("/send")
    public ResponseEntity<?> sendEmail(@RequestBody EmailRequest request) {
        emailService.send(request.getTo(), request.getSubject(), request.getMessage());
        return ResponseEntity.ok("Email inviata con successo ✅");
    }
}
