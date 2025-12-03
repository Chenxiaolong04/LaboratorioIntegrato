/**
 * Service per operazioni su Utenti.
 * 
 * Gestisce:
 * - Registrazione nuovi utenti (solo ADMIN)
 * - Aggiornamento profilo personale (qualsiasi utente autenticato)
 * - Modifica utenti per ID (solo ADMIN)
 * - Recupero utente per email
 * - Eliminazione utenti (solo ADMIN)
 * - Hash password con BCrypt
 * - Validazione email duplicata
 * - Assegnazione tipi utente (ADMIN, AGENT, CLIENT)
 * 
 * Implementa logica di business separata dai controller.
 * Tutte le password sono crittografate con BCryptPasswordEncoder.
 * 
 * @author Sistema IMMOBILIARIS
 * @version 1.0
 * @since 2025-12-01
 */
package com.immobiliaris.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.immobiliaris.demo.entity.TipoUtente;
import com.immobiliaris.demo.entity.User;
import com.immobiliaris.demo.repository.UserRepository;
import com.immobiliaris.demo.repository.TipoUtenteRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {
    
    /** Repository per operazioni su User */
    @Autowired
    private UserRepository userRepository;

    /** Repository per operazioni su TipoUtente */
    @Autowired
    private TipoUtenteRepository tipoUtenteRepository;

    /** Encoder per hashing password con algoritmo BCrypt */
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Registra un nuovo utente nel sistema.
     * 
     * Procedura:
     * 1. Verifica che email non sia già registrata
     * 2. Codifica password con BCrypt
     * 3. Imposta data registrazione corrente
     * 4. Assegna tipo utente (ADMIN, AGENT, CLIENT)
     * 5. Salva nel database
     * 
     * Validazioni:
     * - Email deve essere unica (RuntimeException se duplicata)
     * - Tipo utente deve esistere (RuntimeException se non trovato)
     * - Password viene sempre codificata, mai salvata in chiaro
     * 
     * @param user User da registrare (con email, nome, cognome, password, ecc.)
     * @param tipoUtenteId ID del tipo utente (1=ADMIN, 2=AGENT, 3=CLIENT)
     * @return User registrato con ID generato dal database
     * @throws RuntimeException se email già registrata
     * @throws RuntimeException se tipo utente non trovato
     * 
     * @see User
     * @see TipoUtente
     */
    public User registerUser(User user, Integer tipoUtenteId) {
        // Controllo email duplicata
        if(userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email già registrata");
        }

        // Hash password con BCrypt
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Data registrazione (now)
        user.setDataRegistrazione(LocalDateTime.now());

        // Assegna tipoUtente
        TipoUtente tipo = tipoUtenteRepository.findById(tipoUtenteId)
                .orElseThrow(() -> new RuntimeException("Tipo utente non trovato"));
        user.setTipoUtente(tipo);

        // Salvataggio
        return userRepository.save(user);
    }

    /**
     * Recupera utente per email.
     * 
     * @param email Email dell'utente
     * @return User trovato
     * @throws RuntimeException se utente non trovato
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
    }

    /**
     * Aggiorna profilo personale dell'utente autenticato.
     * 
     * Permette all'utente di modificare i propri dati personali:
     * - Nome, cognome, telefono
     * - Indirizzo (via, città, CAP)
     * - Email (con validazione unicità se diversa da quella attuale)
     * - Password (viene codificata con BCrypt)
     * 
     * Comportamento:
     * - Solo campi non-null e non-vuoti vengono aggiornati
     * - Campi null o stringa vuota vengono ignorati (preservano valore attuale)
     * - Il TIPO UTENTE NON può essere modificato (solo admin può cambiarla)
     * - Email deve essere unica se diversa da quella attuale
     * 
     * Caso email: Se la nuova email è già in uso da altro utente, lancia RuntimeException.
     * 
     * @param email Email dell'utente autenticato (per ricerca)
     * @param updatedUser User con i nuovi dati (solo campi valorizzati)
     * @return User aggiornato e salvato
     * @throws RuntimeException se utente non trovato
     * @throws RuntimeException se nuova email già in uso
     */
    public User updateOwnProfile(String email, User updatedUser) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        // Aggiorna solo i campi forniti (non-null e non-vuoti)
        if(updatedUser.getNome() != null && !updatedUser.getNome().isEmpty()) {
            user.setNome(updatedUser.getNome());
        }
        if(updatedUser.getCognome() != null && !updatedUser.getCognome().isEmpty()) {
            user.setCognome(updatedUser.getCognome());
        }
        if(updatedUser.getTelefono() != null && !updatedUser.getTelefono().isEmpty()) {
            user.setTelefono(updatedUser.getTelefono());
        }
        if(updatedUser.getVia() != null && !updatedUser.getVia().isEmpty()) {
            user.setVia(updatedUser.getVia());
        }
        if(updatedUser.getCitta() != null && !updatedUser.getCitta().isEmpty()) {
            user.setCitta(updatedUser.getCitta());
        }
        if(updatedUser.getCap() != null && !updatedUser.getCap().isEmpty()) {
            user.setCap(updatedUser.getCap());
        }
        
        // Modifica email solo se diversa e non già in uso
        if(updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty() && 
           !updatedUser.getEmail().equals(user.getEmail())) {
            if(userRepository.findByEmail(updatedUser.getEmail()).isPresent()) {
                throw new RuntimeException("Email già in uso");
            }
            user.setEmail(updatedUser.getEmail());
        }
        
        // Modifica password se fornita (sempre codificata)
        if(updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        return userRepository.save(user);
    }

    /**
     * Modifica dati utente per ID (solo ADMIN).
     * 
     * Permette admin di modificare qualsiasi campo di un utente,
     * incluso il TIPO UTENTE (ruolo: ADMIN, AGENT, CLIENT).
     * 
     * Comportamento:
     * - Solo campi non-null e non-vuoti vengono aggiornati
     * - Campi null o stringa vuota vengono ignorati
     * - ADMIN può modificare il tipoUtente dell'utente
     * - Password viene sempre codificata con BCrypt
     * - Email deve rimanere unica (RuntimeException se duplicata)
     * 
     * Differenza da {@link #updateOwnProfile(String, User)}:
     * - Questa richiede ID, non email
     * - Solo ADMIN può modificare tipoUtente
     * - L'admin può modificare qualsiasi utente, non solo se stesso
     * 
     * @param id ID dell'utente da modificare
     * @param updatedUser User con i nuovi dati
     * @return User aggiornato e salvato
     * @throws RuntimeException se utente non trovato
     * @throws RuntimeException se tipo utente non trovato
     * @throws RuntimeException se nuova email già in uso
     */
    public User updateUserAsAdmin(Long id, User updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        // Aggiorna campi personali
        if(updatedUser.getNome() != null && !updatedUser.getNome().isEmpty()) {
            user.setNome(updatedUser.getNome());
        }
        if(updatedUser.getCognome() != null && !updatedUser.getCognome().isEmpty()) {
            user.setCognome(updatedUser.getCognome());
        }
        if(updatedUser.getTelefono() != null && !updatedUser.getTelefono().isEmpty()) {
            user.setTelefono(updatedUser.getTelefono());
        }
        if(updatedUser.getVia() != null && !updatedUser.getVia().isEmpty()) {
            user.setVia(updatedUser.getVia());
        }
        if(updatedUser.getCitta() != null && !updatedUser.getCitta().isEmpty()) {
            user.setCitta(updatedUser.getCitta());
        }
        if(updatedUser.getCap() != null && !updatedUser.getCap().isEmpty()) {
            user.setCap(updatedUser.getCap());
        }
        
        // Modifica email solo se diversa e non già in uso
        if(updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty() && 
           !updatedUser.getEmail().equals(user.getEmail())) {
            if(userRepository.findByEmail(updatedUser.getEmail()).isPresent()) {
                throw new RuntimeException("Email già in uso");
            }
            user.setEmail(updatedUser.getEmail());
        }
        
        // Modifica password se fornita
        if(updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        
        // SOLO ADMIN può modificare il tipoUtente
        if(updatedUser.getTipoUtente() != null && updatedUser.getTipoUtente().getIdTipo() != null) {
            TipoUtente tipo = tipoUtenteRepository.findById(updatedUser.getTipoUtente().getIdTipo())
                    .orElseThrow(() -> new RuntimeException("Tipo utente non trovato"));
            user.setTipoUtente(tipo);
        }

        return userRepository.save(user);
    }

    /**
     * Aggiorna utente generico (wrapper per updateUserAsAdmin).
     * 
     * @param id ID dell'utente
     * @param updatedUser User con dati aggiornati
     * @return User aggiornato
     * 
     * @deprecated Usare {@link #updateUserAsAdmin(Long, User)} direttamente
     */
    @Deprecated(since = "1.0", forRemoval = false)
    public User updateUser(Long id, User updatedUser) {
        return updateUserAsAdmin(id, updatedUser);
    }

    /**
     * Elimina utente per ID (solo ADMIN).
     * 
     * Rimuove completamente l'utente dal database.
     * Attenzione: Questa operazione è irreversibile.
     * 
     * Validazione:
     * - RuntimeException se utente non esiste
     * 
     * @param id ID dell'utente da eliminare
     * @throws RuntimeException se utente non trovato
     */
    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Utente non trovato");
        }
        userRepository.deleteById(id);
    }

    /**
     * Recupera tutti gli utenti dal database.
     * @return List di User
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Recupera utente per ID.
     * @param id ID dell'utente
     * @return User trovato
     * @throws RuntimeException se non trovato
     */
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
    }
}
