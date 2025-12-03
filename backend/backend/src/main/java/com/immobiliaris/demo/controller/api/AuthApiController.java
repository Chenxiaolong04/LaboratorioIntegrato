/**
 * Controller REST API per Autenticazione.
 * 
 * Gestisce le operazioni di autenticazione e autorizzazione:
 * - Login utente con credenziali email/password
 * - Logout e invalidazione sessione
 * - Verifica stato autenticazione
 * - Recupero informazioni utente autenticato
 * 
 * Utilizza sessione HTTP server-side (Cookie JSESSIONID).
 * Supporta sia applicazioni server-side che client-side (React/frontend).
 * 
 * @author Sistema IMMOBILIARIS
 * @version 1.0
 * @since 2025-12-01
 */
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "API Autenticazione - Login, Logout, Verifica sessione")
public class AuthApiController {

    /** Manager autenticazione Spring Security */
    @Autowired
    private AuthenticationManager authenticationManager;

    /** Repository per accesso ai dati utenti */
    @Autowired
    private UserRepository userRepository;

    /**
     * Endpoint Login - Autentica utente con credenziali email/password.
     * 
     * Procedura:
     * 1. Riceve email e password nel JSON body
     * 2. Autentica con Spring Security (verifica password hash BCrypt)
     * 3. Crea sessione HTTP e salva SecurityContext nel cookie JSESSIONID
     * 4. Restituisce dati utente (nome, cognome, ruoli)
     * 
     * Validazione:
     * - Email e password obbligatorie
     * - Password confrontata con hash BCrypt salvato
     * - Se credenziali non valide: HTTP 401 Unauthorized
     * 
     * Risposta successo (200 OK):
     * ```json
     * {
     *   "success": true,
     *   "message": "Login effettuato con successo",
     *   "username": "Mario Rossi",
     *   "roles": ["ROLE_ADMIN", "ROLE_AGENT"]
     * }
     * ```
     * 
     * Risposta errore (401 Unauthorized):
     * ```json
     * {
     *   "success": false,
     *   "message": "Credenziali non valide"
     * }
     * ```
     * 
     * Cookie di sessione:
     * - Nome: JSESSIONID
     * - Scope: Tutti i request successivi
     * - Durata: Configurata in application.properties (default 30 min)
     * - Sicuro: HttpOnly (accessibile solo via HTTP, non JavaScript)
     * 
     * @param credentials Map contenente "email" e "password"
     * @param request HttpServletRequest per accesso alla sessione
     * @return ResponseEntity con status 200 e dati utente, oppure 401 se credenziali non valide
     * 
     * @see UsernamePasswordAuthenticationToken
     * @see HttpSessionSecurityContextRepository
     */
    @PostMapping("/login")
    @Operation(
        summary = "Login utente",
        description = "Autentica utente con email e password, crea sessione HTTP"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login riuscito, sessione creata"),
        @ApiResponse(responseCode = "401", description = "Email o password non valida"),
        @ApiResponse(responseCode = "400", description = "Body JSON malformato")
    })
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody Map<String, String> credentials,
            HttpServletRequest request) {
        
        try {
            String email = credentials.get("email");
            String password = credentials.get("password");

            // Autentica l'utente tramite AuthenticationManager
            // Verifica email e password, confronta password con hash BCrypt
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
            );

            // Salva l'autenticazione nella sessione HTTP
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
            
            // Recupera nome completo dal database
            String principalName = authentication.getName();
            String displayName = userRepository.findByEmail(principalName)
                .map(u -> u.getNome() + (u.getCognome() != null && !u.getCognome().isEmpty() ? " " + u.getCognome() : ""))
                .orElse(principalName);
            response.put("username", displayName);
            
            // Recupera ruoli dell'utente
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
     * Endpoint Logout - Invalida sessione utente e cancella autenticazione.
     * 
     * Procedura:
     * 1. Invalida la sessione HTTP corrente
     * 2. Cancella SecurityContext da thread-local
     * 3. Cookie JSESSIONID viene invalidato dal browser
     * 4. Tutti i successivi request non saranno autenticati
     * 
     * Risposta (200 OK):
     * ```json
     * {
     *   "success": true,
     *   "message": "Logout effettuato con successo"
     * }
     * ```
     * 
     * @param request HttpServletRequest per accesso alla sessione
     * @param response HttpServletResponse per eventuali manipolazioni
     * @return ResponseEntity con status 200 e messaggio successo
     * 
     * @see HttpServletRequest#getSession()
     * @see SecurityContextHolder#clearContext()
     */
    @PostMapping("/logout")
    @Operation(
        summary = "Logout utente",
        description = "Invalida sessione HTTP, termina autenticazione"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Logout riuscito, sessione invalidata"),
        @ApiResponse(responseCode = "500", description = "Errore interno nel logout")
    })
    public ResponseEntity<Map<String, Object>> logout(
            HttpServletRequest request,
            HttpServletResponse response) {
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
     * Endpoint Recupera informazioni utente autenticato.
     * 
     * Restituisce i dati dell'utente attualmente autenticato:
     * - Nome e cognome completo
     * - Lista di ruoli e permessi
     * - Stato autenticazione
     * 
     * Risposta (200 OK):
     * ```json
     * {
     *   "username": "Mario Rossi",
     *   "roles": ["ROLE_ADMIN"],
     *   "authenticated": true
     * }
     * ```
     * 
     * Risposta se non autenticato (401 Unauthorized):
     * ```
     * [vuoto]
     * ```
     * 
     * @param authentication {@link Authentication} iniettato da Spring Security
     * @return ResponseEntity con dati utente o status 401 se non autenticato
     * 
     * @see Authentication
     */
    @GetMapping("/user")
    @Operation(
        summary = "Info utente autenticato",
        description = "Restituisce dati dell'utente attualmente loggato"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dati utente ritornati"),
        @ApiResponse(responseCode = "401", description = "Utente non autenticato")
    })
    public ResponseEntity<Map<String, Object>> getCurrentUser(
            Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        Map<String, Object> user = new HashMap<>();
        
        // Recupera nome completo dal database
        String principal = authentication.getName();
        String display = userRepository.findByEmail(principal)
            .map(u -> u.getNome() + (u.getCognome() != null && !u.getCognome().isEmpty() ? " " + u.getCognome() : ""))
            .orElse(principal);
        user.put("username", display);
        
        // Recupera ruoli
        user.put("roles", authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        
        user.put("authenticated", true);

        return ResponseEntity.ok(user);
    }

    /**
     * Endpoint Verifica rapida stato autenticazione.
     * 
     * Utility leggero per check veloce se utente è autenticato.
     * Utile per frontend per decidere se mostrare login/logout.
     * 
     * Risposta (200 OK - Autenticato):
     * ```json
     * {
     *   "authenticated": true
     * }
     * ```
     * 
     * Risposta (200 OK - Non autenticato):
     * ```json
     * {
     *   "authenticated": false
     * }
     * ```
     * 
     * @param authentication {@link Authentication} iniettato da Spring Security
     * @return ResponseEntity con status 200 e flag "authenticated"
     * 
     * @see Authentication
     */
    @GetMapping("/check")
    @Operation(
        summary = "Verifica autenticazione",
        description = "Check veloce dello stato di autenticazione utente"
    )
    @ApiResponse(responseCode = "200", description = "Status autenticazione ritornato")
    public ResponseEntity<Map<String, Boolean>> checkAuth(
            Authentication authentication) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("authenticated", authentication != null && authentication.isAuthenticated());
        return ResponseEntity.ok(response);
    }
}
