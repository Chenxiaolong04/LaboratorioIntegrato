package com.immobiliaris.demo.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/agent")
public class AgentApiController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Benvenuto agente");
        response.put("user", authentication.getName());
        response.put("role", "AGENT");
        
        // Ottieni ID dell'agente loggato
        Integer idAgente = jdbcTemplate.queryForObject(
            "SELECT Id_utente FROM Utenti WHERE Email = ?",
            Integer.class,
            authentication.getName()
        );
        
        // Statistiche personali dell'agente
        Map<String, Object> stats = new HashMap<>();
        
        // Contratti conclusi dall'agente (stato 'chiuso')
        Integer contrattiConclusi = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM Contratti c JOIN Stati_contratto sc ON c.Id_stato_contratto = sc.Id_stato_contratto WHERE sc.Nome = 'chiuso' AND c.Id_agente = ?",
            Integer.class,
            idAgente
        );
        stats.put("contrattiConclusi", contrattiConclusi != null ? contrattiConclusi : 0);
        
        // Valutazioni in corso dell'agente (stato 'in_verifica')
        Integer valutazioniInCorso = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM Valutazioni v JOIN Stati_valutazione sv ON v.Id_stato_valutazione = sv.Id_stato_valutazione WHERE sv.Nome = 'in_verifica' AND v.Id_agente = ?",
            Integer.class,
            idAgente
        );
        stats.put("valutazioniInCorso", valutazioniInCorso != null ? valutazioniInCorso : 0);
        
        // Valutazioni con AI assegnate all'agente (stato 'solo_AI')
        Integer valutazioniConAI = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM Valutazioni v JOIN Stati_valutazione sv ON v.Id_stato_valutazione = sv.Id_stato_valutazione WHERE sv.Nome = 'solo_AI' AND v.Id_agente = ?",
            Integer.class,
            idAgente
        );
        stats.put("valutazioniConAI", valutazioniConAI != null ? valutazioniConAI : 0);
        
        // Immobili in valutazione (generale)
        Integer immobiliInValutazione = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM Immobili i JOIN Stati_immobile si ON i.Id_stato_immobile = si.Id_stato_immobile WHERE si.Nome = 'in valutazione'",
            Integer.class
        );
        stats.put("immobiliInValutazione", immobiliInValutazione != null ? immobiliInValutazione : 0);
        
        response.put("statistics", stats);
        
        return ResponseEntity.ok(response);
    }
}
