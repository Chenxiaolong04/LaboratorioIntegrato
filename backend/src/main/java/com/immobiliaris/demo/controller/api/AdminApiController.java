package com.immobiliaris.demo.controller.api;

import com.immobiliaris.demo.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminApiController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard(Authentication authentication,
                                                            @RequestParam(defaultValue = "0") int offset,
                                                            @RequestParam(defaultValue = "10") int limit) {
        Map<String, Object> response = new LinkedHashMap<>();

        try {
            // Ottieni statistiche complete dal Service
            Map<String, Object> dashboardData = statisticsService.getAdminDashboardData();
            response.put("statistics", dashboardData.get("statistics"));

            // Ottieni batch di immobili usando offset/limit (per comportamento "Carica altri")
            Map<String, Object> immobiliLoad = statisticsService.getImmobiliLoadMore(offset, limit);
            response.put("immobili", immobiliLoad.get("immobili"));
            response.put("nextOffset", immobiliLoad.get("nextOffset"));
            response.put("hasMore", immobiliLoad.get("hasMore"));
            response.put("pageSize", immobiliLoad.get("pageSize"));

        } catch (Exception e) {
            // Se c'è errore, ritorna almeno le info base
            System.err.println("Errore caricamento dashboard: " + e.getMessage());
            e.printStackTrace();
            response.put("error", e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    /**
     * API per ottenere immobili con paginazione
     * GET /api/admin/immobili?page=0&size=10
     * @param page Numero pagina (0 = prima pagina)
     * @param size Numero elementi per pagina (default 10)
     */
    @GetMapping("/immobili")
    public ResponseEntity<Map<String, Object>> getImmobiliPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Map<String, Object> result = statisticsService.getImmobiliPaginated(page, size);
        return ResponseEntity.ok(result);
    }

    /**
     * Endpoint per il pulsante "Carica altri" nel frontend.
     * Usa offset (numero di elementi già caricati) e limit (quantità da caricare)
     * Esempio: GET /api/admin/immobili/load?offset=0&limit=10
     */
    

    /**
     * Restituisce contratti chiusi con i dettagli degli immobili associati
     * Usa offset/limit per load-more progressivo (come dashboard)
     * Esempio: GET /api/admin/contratti/chiusi?offset=0&limit=10
     */
    @GetMapping("/contratti/chiusi")
    public ResponseEntity<Object> getContrattiChiusi(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit) {
        try {
            return ResponseEntity.ok(statisticsService.getContrattiChiusiLoadMore(offset, limit));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Errore recupero contratti");
        }
    }

    /**
     * Restituisce valutazioni generate solo da AI con i dettagli degli immobili
     * Usa offset/limit per load-more progressivo
     * Esempio: GET /api/admin/valutazioni/solo-ai?offset=0&limit=10
     */
    @GetMapping("/valutazioni/solo-ai")
    public ResponseEntity<Object> getValutazioniSoloAI(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit) {
        try {
            return ResponseEntity.ok(statisticsService.getValutazioniSoloAILoadMore(offset, limit));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Errore recupero valutazioni");
        }
    }
}
