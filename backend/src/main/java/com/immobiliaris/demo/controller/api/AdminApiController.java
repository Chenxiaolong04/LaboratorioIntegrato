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
    public ResponseEntity<Map<String, Object>> getDashboard(Authentication authentication) {
        Map<String, Object> response = new LinkedHashMap<>();
        
        try {
            // Ottieni statistiche complete dal Service
            Map<String, Object> dashboardData = statisticsService.getAdminDashboardData();
            response.put("statistics", dashboardData.get("statistics"));
            response.put("ultimi10Immobili", dashboardData.get("ultimi10Immobili"));
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
}
