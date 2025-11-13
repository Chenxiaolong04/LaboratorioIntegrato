package com.immobiliaris.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.immobiliaris.demo.entity.TipoUtente;
import com.immobiliaris.demo.entity.User;
import com.immobiliaris.demo.repository.UserRepository;
import com.immobiliaris.demo.repository.TipoUtenteRepository;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TipoUtenteRepository tipoUtenteRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Registrazione nuovo utente (admin o agent)
    public User registerUser(User user, Integer tipoUtenteId) {
        // Controllo email duplicata
        if(userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email già registrata");
        }

        // Hash password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Data registrazione
        user.setDataRegistrazione(new java.sql.Date(System.currentTimeMillis()));

        // Assegna tipoUtente
        TipoUtente tipo = tipoUtenteRepository.findById(tipoUtenteId)
                .orElseThrow(() -> new RuntimeException("Tipo utente non trovato"));
        user.setTipoUtente(tipo);

        // Salvataggio
        return userRepository.save(user);
    }

    // Registrazione nuovo utente senza tipo utente specifico
    public User registerUserBasic(User user) {
        // Controllo email duplicata
        if(userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email già registrata");
        }

        // Hash password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Data registrazione
        user.setDataRegistrazione(new java.sql.Date(System.currentTimeMillis()));

        // Salvataggio
        return userRepository.save(user);
    }

    // Aggiornamento utente
    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setNome(updatedUser.getNome());
                    user.setCognome(updatedUser.getCognome());
                    user.setEmail(updatedUser.getEmail());
                    user.setTelefono(updatedUser.getTelefono());
                    user.setVia(updatedUser.getVia());
                    user.setCitta(updatedUser.getCitta());
                    user.setCap(updatedUser.getCap());

                    // Aggiornamento password solo se fornita
                    if(updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                        user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                    }

                    // Aggiornamento tipoUtente
                    if(updatedUser.getTipoUtente() != null) {
                        TipoUtente tipo = tipoUtenteRepository.findById(updatedUser.getTipoUtente().getIdTipo())
                                .orElseThrow(() -> new RuntimeException("Tipo utente non trovato"));
                        user.setTipoUtente(tipo);
                    }

                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
    }

    // Lista tutti gli utenti
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Recupero utente per ID
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
    }

    private Integer tipoUtenteId;

    public Integer getTipoUtenteId() {
        return tipoUtenteId;
    }

    public void setTipoUtenteId(Integer tipoUtenteId) {
        this.tipoUtenteId = tipoUtenteId;
    }
}
