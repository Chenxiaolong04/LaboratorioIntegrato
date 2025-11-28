package com.immobiliaris.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.immobiliaris.demo.entity.Immobile;
import com.immobiliaris.demo.entity.User;
import com.immobiliaris.demo.entity.TipoUtente;
import com.immobiliaris.demo.dto.ImmobileRequest;
import com.immobiliaris.demo.repository.ImmobileJpaRepository;
import com.immobiliaris.demo.repository.UserRepository;
import com.immobiliaris.demo.repository.TipoUtenteRepository;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/immobili")
@CrossOrigin
public class ImmobileController {

    @Autowired
    private ImmobileJpaRepository immobileJpaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TipoUtenteRepository tipoUtenteRepository;
    @Autowired
    private com.immobiliaris.demo.service.ValutazioneService valutazioneService;

    @Autowired
    private com.immobiliaris.demo.service.EmailService emailService;

    @PostMapping("/save")
    public ResponseEntity<?> saveImmobile(@RequestBody ImmobileRequest immobileRequest) {
        try {
            Immobile immobile = immobileRequest.toImmobile();
            if (immobile.getCitta() != null) {
                immobile.setProvincia(switch (immobile.getCitta().trim().toLowerCase()) {
                    case "torino" -> "TO";
                    case "cuneo" -> "CN";
                    case "alessandria" -> "AL";
                    case "asti" -> "AT";
                    default -> null;
                });
            }
            String emailProprietario = null;
            if (immobile.getProprietario() != null && immobile.getProprietario().getEmail() != null) {
                String email = immobile.getProprietario().getEmail();
                emailProprietario = email;
                User utente = userRepository.findByEmail(email).orElse(null);
                if (utente == null) {
                    User nuovoUtente = new User();
                    nuovoUtente.setEmail(email);
                    nuovoUtente.setNome(immobile.getProprietario().getNome());
                    nuovoUtente.setCognome(immobile.getProprietario().getCognome());
                    nuovoUtente.setTelefono(immobile.getProprietario().getTelefono());
                    org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder encoder = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
                    String passwordHash = encoder.encode(email);
                    nuovoUtente.setPassword(passwordHash);
                    TipoUtente tipoCliente = null;
                    for (TipoUtente t : tipoUtenteRepository.findAll()) {
                        if (t.getNome().equalsIgnoreCase("cliente")) {
                            tipoCliente = t;
                            break;
                        }
                    }
                    if (tipoCliente != null) {
                        nuovoUtente.setTipoUtente(tipoCliente);
                    }
                    utente = userRepository.save(nuovoUtente);
                }
                User utenteGestito = userRepository.findById(utente.getIdUtente()).orElse(null);
                if (utenteGestito != null) {
                    immobile.setProprietario(utenteGestito);
                }
            }
            Immobile saved = immobileJpaRepository.save(immobile);

            com.immobiliaris.demo.entity.Valutazione valutazione = valutazioneService.valutaImmobile(saved);

            emailService.sendValutazioneRecap(valutazione.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("idImmobile", saved.getId());
            response.put("email", emailProprietario);
            response.put("prezzoAI", valutazione.getPrezzoAI());
            response.put("mailInviata", true);
            return ResponseEntity.ok(response);
        } catch (jakarta.mail.MessagingException e) {
            return ResponseEntity.status(500).body("Errore nell'invio email: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body("Errore: " + e.getMessage());
        }
    }
}