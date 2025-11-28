package com.immobiliaris.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.immobiliaris.demo.entity.TipoUtente;
import com.immobiliaris.demo.entity.User;
import com.immobiliaris.demo.repository.UserRepository;
import com.immobiliaris.demo.repository.TipoUtenteRepository;

import java.util.List;

@Service
public class UserService {
    // Elimina utente per ID (solo ADMIN)
    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Utente non trovato");
        }
        userRepository.deleteById(id);
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TipoUtenteRepository tipoUtenteRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Registrazione nuovo utente (solo ADMIN)
    public User registerUser(User user, Integer tipoUtenteId) {
        // Controllo email duplicata
        if(userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email già registrata");
        }

        // Hash password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Data registrazione
        user.setDataRegistrazione(java.time.LocalDateTime.now());

        // Assegna tipoUtente
        TipoUtente tipo = tipoUtenteRepository.findById(tipoUtenteId)
                .orElseThrow(() -> new RuntimeException("Tipo utente non trovato"));
        user.setTipoUtente(tipo);

        // Salvataggio
        return userRepository.save(user);
    }

    // Recupera utente per email
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
    }

    // Modifica profilo personale (solo ADMIN)
    public User updateOwnProfile(String email, User updatedUser) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        // Aggiorna solo i campi forniti
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

        return userRepository.save(user);
    }

    // Modifica utente per ID (solo ADMIN)
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

    // Aggiornamento utente generico (deprecato, usare i metodi specifici)
    public User updateUser(Long id, User updatedUser) {
        return updateUserAsAdmin(id, updatedUser);
    }

    // Lista tutti gli utenti (solo ADMIN)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Recupero utente per ID (solo ADMIN)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
    }
}
