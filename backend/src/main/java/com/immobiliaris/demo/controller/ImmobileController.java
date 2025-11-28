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

    // Salva un nuovo immobile e ritorna email proprietario e immobile
    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> saveImmobile(@RequestBody ImmobileRequest immobileRequest) {
        try {
            System.out.println("\n=== INIZIO SALVATAGGIO IMMOBILE ===");
            
            // Converti DTO in Immobile
            System.out.println("STEP 1: Converti DTO in Immobile");
            Immobile immobile = immobileRequest.toImmobile();
            System.out.println("STEP 1 OK: Immobile creato, via=" + immobile.getVia());
            
            // Imposta la provincia in base alla città
            System.out.println("STEP 2: Imposta provincia");
            if (immobile.getCitta() != null) {
                immobile.setProvincia(switch (immobile.getCitta().trim().toLowerCase()) {
                    case "torino" -> "TO";
                    case "cuneo" -> "CN";
                    case "alessandria" -> "AL";
                    case "asti" -> "AT";
                    default -> null;
                });
            }
            System.out.println("STEP 2 OK: Provincia=" + immobile.getProvincia());
            
            // Se proprietario è presente, cerca l'utente per email o crealo
            System.out.println("STEP 3: Controlla se proprietario è presente");
            String emailProprietario = null;
            Long idProprietario = null;
            
            if (immobile.getProprietario() != null && immobile.getProprietario().getEmail() != null) {
                String email = immobile.getProprietario().getEmail();
                System.out.println("STEP 3: Email proprietario trovata: " + email);
                
                System.out.println("STEP 3a: Carica email in variabile locale PRIMA di eventuali operazioni");
                emailProprietario = email; // SALVA EMAIL SUBITO
                
                System.out.println("STEP 3b: Cercando utente con email in database...");
                User utente = userRepository.findByEmail(email).orElse(null);
                
                if (utente == null) {
                    System.out.println("STEP 3c: Utente NON trovato, creando nuovo utente...");
                    // Crea nuovo utente come cliente
                    User nuovoUtente = new User();
                    nuovoUtente.setEmail(email);
                    System.out.println("  - Email impostata: " + email);
                    
                    String nome = immobile.getProprietario().getNome();
                    String cognome = immobile.getProprietario().getCognome();
                    String telefono = immobile.getProprietario().getTelefono();
                    
                    nuovoUtente.setNome(nome);
                    System.out.println("  - Nome impostato: " + nome);
                    
                    nuovoUtente.setCognome(cognome);
                    System.out.println("  - Cognome impostato: " + cognome);
                    
                    nuovoUtente.setTelefono(telefono);
                    System.out.println("  - Telefono impostato: " + telefono);
                    
                    // Imposta una password di default (email hashata)
                    org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder encoder = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
                    String passwordHash = encoder.encode(email);
                    nuovoUtente.setPassword(passwordHash);
                    System.out.println("  - Password impostata (hash dell'email)");
                    
                    // Imposta tipo utente "cliente" se esiste
                    System.out.println("  - Cercando TipoUtente 'cliente'...");
                    TipoUtente tipoCliente = null;
                    for (TipoUtente t : tipoUtenteRepository.findAll()) {
                        if (t.getNome().equalsIgnoreCase("cliente")) {
                            tipoCliente = t;
                            break;
                        }
                    }
                    
                    if (tipoCliente != null) {
                        nuovoUtente.setTipoUtente(tipoCliente);
                        System.out.println("  - TipoUtente impostato: " + tipoCliente.getNome());
                    } else {
                        System.out.println("  - WARN: TipoUtente 'cliente' non trovato!");
                    }
                    
                    System.out.println("STEP 3d: Salvo nuovo utente nel database...");
                    utente = userRepository.save(nuovoUtente);
                    idProprietario = utente.getIdUtente();
                    System.out.println("STEP 3d OK: Nuovo utente salvato con ID: " + idProprietario);
                } else {
                    idProprietario = utente.getIdUtente();
                    System.out.println("STEP 3c: Utente TROVATO nel database con ID: " + idProprietario);
                }
                
                System.out.println("STEP 3e: Assegna utente all'immobile");
                // Cerca l'utente dal database per assicurarti di avere un'istanza gestita
                User utenteGestito = userRepository.findById(idProprietario).orElse(null);
                if (utenteGestito != null) {
                    immobile.setProprietario(utenteGestito);
                    System.out.println("STEP 3e OK: Proprietario assegnato all'immobile");
                } else {
                    System.out.println("STEP 3e ERROR: Utente gestito non trovato con ID " + idProprietario);
                }
            } else {
                System.out.println("STEP 3: Nessun proprietario presente nel request");
            }
            
            System.out.println("STEP 4: Email memorizzata in locale: " + emailProprietario);
            
            System.out.println("STEP 5: Salva immobile nel database...");
            Immobile saved = immobileJpaRepository.save(immobile);
            System.out.println("STEP 5 OK: Immobile salvato con ID: " + saved.getId());
            
            System.out.println("STEP 6: Costruisci risposta (SENZA accedere al proprietario)");
            Map<String, Object> response = new HashMap<>();
            response.put("email", emailProprietario); // Usa il valore salvato, non saved.getProprietario()
            response.put("idImmobile", saved.getId());
            response.put("via", saved.getVia());
            response.put("citta", saved.getCitta());
            response.put("tipologia", saved.getTipologia());
            System.out.println("STEP 6 OK: Risposta costruita");
            
            System.out.println("=== FINE SALVATAGGIO IMMOBILE - SUCCESSO ===\n");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("\n!!! ERRORE DURANTE SALVATAGGIO !!!");
            System.out.println("Messaggio: " + e.getMessage());
            if (e.getCause() != null) {
                System.out.println("Causa: " + e.getCause().getMessage());
            }
            // noinspection CallToPrintStackTrace
            e.printStackTrace(); // OK per debugging
            System.out.println("!!! FINE ERRORE !!!\n");
            throw e;
        }
    }
}
