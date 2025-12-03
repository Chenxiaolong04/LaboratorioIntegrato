package com.immobiliaris.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/map")
@CrossOrigin
public class MapController {

    @Value("${geoapify.api.key:your_api_key_here}")
    private String geoapifyApiKey;

    /**
     * Genera un URL di mappa statica Geoapify
     * 
     * POST /api/map/generate
     * 
     * @param body JSON con lat, lon, zoom (opzionale), width (opzionale), height (opzionale)
     * @return URL della mappa statica
     */
    @PostMapping("/generate")
    public ResponseEntity<Map<String, String>> generateMap(@RequestBody Map<String, Object> body) {
        try {
            // Estrai coordinate
            double lat = ((Number) body.get("lat")).doubleValue();
            double lon = ((Number) body.get("lon")).doubleValue();
            
            // Parametri opzionali
            int zoom = body.containsKey("zoom") ? ((Number) body.get("zoom")).intValue() : 15;
            int width = body.containsKey("width") ? ((Number) body.get("width")).intValue() : 600;
            int height = body.containsKey("height") ? ((Number) body.get("height")).intValue() : 400;
            String style = body.containsKey("style") ? (String) body.get("style") : "osm-carto";

            // Costruisci URL mappa statica Geoapify
            String mapUrl = String.format(java.util.Locale.US,
                "https://maps.geoapify.com/v1/staticmap?style=%s&center=lonlat:%.6f,%.6f&zoom=%d&width=%d&height=%d&apiKey=%s",
                style, lon, lat, zoom, width, height, geoapifyApiKey
            );

            System.out.println("[DEBUG] Generated map URL: " + mapUrl);

            return ResponseEntity.ok(Map.of("mapUrl", mapUrl));
        } catch (Exception e) {
            System.err.println("[ERROR] Map generation error: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", "Map generation failed"));
        }
    }

    /**
     * Genera un URL di mappa con marker per un indirizzo
     * 
     * POST /api/map/address-marker
     * 
     * @param body JSON con lat, lon, address (opzionale), zoom (opzionale), width (opzionale), height (opzionale)
     * @return URL della mappa statica con marker
     */
    @PostMapping("/address-marker")
    public ResponseEntity<Map<String, String>> generateMapWithMarker(@RequestBody Map<String, Object> body) {
        try {
            // Estrai coordinate
            double lat = ((Number) body.get("lat")).doubleValue();
            double lon = ((Number) body.get("lon")).doubleValue();
            
            // Parametri opzionali
            int zoom = body.containsKey("zoom") ? ((Number) body.get("zoom")).intValue() : 15;
            int width = body.containsKey("width") ? ((Number) body.get("width")).intValue() : 600;
            int height = body.containsKey("height") ? ((Number) body.get("height")).intValue() : 400;
            String style = body.containsKey("style") ? (String) body.get("style") : "osm-carto";
            String address = body.containsKey("address") ? (String) body.get("address") : "";

            // Costruisci marker
            String marker = String.format(java.util.Locale.US, "lonlat:%.6f,%.6f;color:red;size:medium;text:%s", 
                lon, lat, address.isEmpty() ? "📍" : address.substring(0, Math.min(1, address.length())));

            // Costruisci URL mappa statica con marker
            String mapUrl = String.format(java.util.Locale.US,
                "https://maps.geoapify.com/v1/staticmap?style=%s&center=lonlat:%.6f,%.6f&zoom=%d&width=%d&height=%d&marker=%s&apiKey=%s",
                style, lon, lat, zoom, width, height, marker, geoapifyApiKey
            );

            System.out.println("[DEBUG] Generated map with marker URL: " + mapUrl);

            return ResponseEntity.ok(Map.of("mapUrl", mapUrl));
        } catch (Exception e) {
            System.err.println("[ERROR] Map with marker generation error: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", "Map generation failed"));
        }
    }
}
