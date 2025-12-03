package com.immobiliaris.demo.service;

import com.immobiliaris.demo.entity.User;
import com.immobiliaris.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato con email: " + email));

        // Verifica che tipoUtente non sia null
        if (user.getTipoUtente() == null) {
            throw new UsernameNotFoundException("Tipo utente non definito per: " + email);
        }

        // Mappa il nome del tipo utente a un ruolo Spring Security
        // "Amministratore" -> ROLE_ADMIN, "Agente" -> ROLE_AGENT, "Cliente" -> ROLE_USER
        String roleName = mapTipoToRole(user.getTipoUtente().getNome());

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority(roleName)))
                .build();
    }

    private String mapTipoToRole(String tipoNome) {
        if (tipoNome == null) {
            return "ROLE_USER";
        }
        return switch (tipoNome.toLowerCase()) {
            case "amministratore" -> "ROLE_ADMIN";
            case "agente" -> "ROLE_AGENT";
            case "cliente" -> "ROLE_USER";
            default -> "ROLE_USER";
        };
    }
}
