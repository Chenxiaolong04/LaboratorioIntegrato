package com.immobiliaris.demo.controller.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import com.immobiliaris.demo.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    /**
     * API REST per login (React/frontend)
     * POST /api/auth/login
     * Body: {"email": "user@test.com", "password": "password123"}
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody Map<String, String> credentials,
            HttpServletRequest request) {
        
        try {
            String email = credentials.get("email");
            String password = credentials.get("password");

            // Autentica l'utente
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
            );

            // Salva l'autenticazione nella sessione
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);
            request.getSession().setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                securityContext
            );

                // Prepara la risposta con info utente
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Login effettuato con successo");
                // Se possibile, restituisci il nome (campo `Nome`) dell'utente salvato nel DB
                String principalName = authentication.getName();
                    String displayName = userRepository.findByEmail(principalName)
                        .map(u -> u.getNome() + (u.getCognome() != null && !u.getCognome().isEmpty() ? " " + u.getCognome() : ""))
                        .orElse(principalName);
                response.put("username", displayName);
            response.put("roles", authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Credenziali non valide");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    /**
     * API REST per logout (React/frontend)
     * POST /api/auth/logout
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Invalida la sessione
            request.getSession().invalidate();
            SecurityContextHolder.clearContext();

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", true);
            responseBody.put("message", "Logout effettuato con successo");

            return ResponseEntity.ok(responseBody);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Errore durante il logout");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Endpoint per ottenere informazioni sull'utente autenticato
     * React chiama questo dopo il login per sapere ruolo e username
     */
    @GetMapping("/user")
    public ResponseEntity<Map<String, Object>> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        Map<String, Object> user = new HashMap<>();
        // Mostra il nome leggendo il profilo utente dal DB quando possibile
        String principal = authentication.getName();
        String display = userRepository.findByEmail(principal)
            .map(u -> u.getNome() + (u.getCognome() != null && !u.getCognome().isEmpty() ? " " + u.getCognome() : ""))
            .orElse(principal);
        user.put("username", display);
        user.put("roles", authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        user.put("authenticated", true);

        return ResponseEntity.ok(user);
    }

    /**
     * Endpoint per check rapido dello stato di autenticazione
     */
    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkAuth(Authentication authentication) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("authenticated", authentication != null && authentication.isAuthenticated());
        return ResponseEntity.ok(response);
    }
}
