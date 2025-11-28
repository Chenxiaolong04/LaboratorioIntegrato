package com.immobiliaris.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.immobiliaris.demo.entity.Immobile;
import com.immobiliaris.demo.repository.ImmobileJpaRepository;
import com.immobiliaris.demo.entity.User;
import com.immobiliaris.demo.repository.UserRepository;
import com.immobiliaris.demo.entity.TipoUtente;
import com.immobiliaris.demo.repository.TipoUtenteRepository;

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

    /**
     * Salva un nuovo immobile. Se il proprietario (nome, cognome, email, telefono) non esiste nella tabella utenti,
     * viene creato come utente di tipo "cliente" (senza password). Se l'utente esiste già, viene associato all'immobile.
     * L'id utente viene inserito nella colonna proprietario dell'immobile.
     *
     * @param immobile Immobile da salvare, con dati proprietario nel JSON
     * @return Immobile salvato con proprietario associato
     */
    @PostMapping("/save")
    public ResponseEntity<Immobile> saveImmobile(@RequestBody Immobile immobile) {
        // Gestione proprietario
        User propRequest = immobile.getProprietario();
        if (propRequest != null && propRequest.getEmail() != null) {
            // Cerca utente per email
            User utente = userRepository.findByEmail(propRequest.getEmail()).orElse(null);
            if (utente == null) {
                // Crea nuovo utente proprietario
                User nuovo = new User();
                nuovo.setNome(propRequest.getNome());
                nuovo.setCognome(propRequest.getCognome());
                nuovo.setEmail(propRequest.getEmail());
                nuovo.setTelefono(propRequest.getTelefono());
                // NON impostare password
                // Imposta tipo utente 'cliente' se esiste
                TipoUtente tipoCliente = null;
                for (TipoUtente t : tipoUtenteRepository.findAll()) {
                    if (t.getNome().equalsIgnoreCase("cliente")) {
                        tipoCliente = t;
                        break;
                    }
                }
                nuovo.setTipoUtente(tipoCliente);
                utente = userRepository.save(nuovo);
            }
            immobile.setProprietario(utente);
        }
        // Imposta la provincia in base alla città
        if (immobile.getCitta() != null) {
            switch (immobile.getCitta().trim().toLowerCase()) {
                case "torino":
                    immobile.setProvincia("TO");
                    break;
                case "cuneo":
                    immobile.setProvincia("CN");
                    break;
                case "alessandria":
                    immobile.setProvincia("AL");
                    break;
                case "asti":
                    immobile.setProvincia("AT");
                    break;
                default:
                    immobile.setProvincia(null); // oppure puoi gestire errore/altro
            }
        }
        Immobile saved = immobileJpaRepository.save(immobile);
        return ResponseEntity.ok(saved);
    }
}
