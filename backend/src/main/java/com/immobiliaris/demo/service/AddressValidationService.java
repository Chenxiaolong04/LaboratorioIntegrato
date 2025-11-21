package com.immobiliaris.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.immobiliaris.demo.dto.AddressValidationRequest;
import com.immobiliaris.demo.dto.AddressValidationResponse;
import com.immobiliaris.demo.dto.AddressValidationResponse.AddressSuggestion;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class AddressValidationService {

    private static final String NOMINATIM_API_URL = "https://nominatim.openstreetmap.org/search";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public AddressValidationService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Valida un indirizzo usando l'API Nominatim di OpenStreetMap
     * 
     * @param request Dati dell'indirizzo da validare
     * @return Risposta con validazione e suggerimenti
     */
    public AddressValidationResponse validateAddress(AddressValidationRequest request) {
        // Controllo: il campo via deve iniziare con un tipo valido
        if (request.getVia() == null || request.getVia().trim().isEmpty()) {
            return new AddressValidationResponse(false, new java.util.ArrayList<>());
        }
        String viaLower = request.getVia().trim().toLowerCase();
        // Non accettare il nome della città nel campo via
        String[] cittaAccettate = {"torino", "cuneo", "alessandria", "asti"};
        for (String citta : cittaAccettate) {
            if (viaLower.contains(citta)) {
                return new AddressValidationResponse(false, new java.util.ArrayList<>());
            }
        }
        String[] tipiValidi = {"via", "viale", "corso", "piazza", "strada", "largo", "vicolo", "piazzale", "ronco", "regione", "frazione", "borgo", "contrada", "località", "ca"};
        boolean tipoValido = false;
        for (String tipo : tipiValidi) {
            if (viaLower.startsWith(tipo + " ") || viaLower.equals(tipo)) {
                tipoValido = true;
                break;
            }
        }
        if (!tipoValido) {
            return new AddressValidationResponse(false, new java.util.ArrayList<>());
        }
        try {
            // Costruisci la query per Nominatim
            String query = buildAddressQuery(request);
            // DEBUG: logga la query
            System.out.println("[DEBUG] Query Nominatim: " + query);

            // Costruisci l'URL con parametri
            String url = UriComponentsBuilder.fromHttpUrl(NOMINATIM_API_URL)
                    .queryParam("q", query)
                    .queryParam("format", "json")
                    .queryParam("addressdetails", "1")
                    .queryParam("limit", "5")
                    .queryParam("countrycodes", "it")
                    .toUriString();
            // DEBUG: logga l'URL
            System.out.println("[DEBUG] URL Nominatim: " + url);

            // Aggiungi User-Agent header (richiesto da Nominatim)
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.set("User-Agent", "Immobiliaris/1.0");
            org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(headers);

            // Chiama l'API Nominatim
            org.springframework.http.ResponseEntity<String> response = restTemplate.exchange(
                url, 
                org.springframework.http.HttpMethod.GET, 
                entity, 
                String.class
            );

            // Parse la risposta JSON
            JsonNode results = objectMapper.readTree(response.getBody());

            if (results.isArray() && results.size() > 0) {
                // Indirizzo trovato - estrai i suggerimenti
                List<AddressSuggestion> suggestions = parseSuggestions(results);
                // Filtra per città se specificata
                if (request.getCitta() != null && !request.getCitta().isEmpty()) {
                    String cittaRichiesta = request.getCitta().trim().toLowerCase();
                    suggestions.removeIf(s -> s.getCitta() == null || !s.getCitta().trim().toLowerCase().equals(cittaRichiesta));
                }
                // Rimuovi duplicati: solo uno per via+citta
                java.util.Set<String> uniqueKeys = new java.util.HashSet<>();
                suggestions.removeIf(s -> {
                    String key = (s.getVia() != null ? s.getVia().trim().toLowerCase() : "") + "|" + (s.getCitta() != null ? s.getCitta().trim().toLowerCase() : "");
                    if (uniqueKeys.contains(key)) {
                        return true;
                    } else {
                        uniqueKeys.add(key);
                        return false;
                    }
                });
                // Accetta solo città specifiche
                java.util.Set<String> accettate = java.util.Set.of("torino", "cuneo", "alessandria", "asti");
                suggestions.removeIf(s -> s.getCitta() == null || !accettate.contains(s.getCitta().trim().toLowerCase()));
                if (suggestions.isEmpty()) {
                    return new AddressValidationResponse(false, new java.util.ArrayList<>());
                }
                return new AddressValidationResponse(true, suggestions);
            } else {
                // Nessun risultato trovato
                return new AddressValidationResponse(
                    false,
                    new ArrayList<>()
                );
            }
        } catch (Exception e) {
            // Errore nella chiamata API
            return new AddressValidationResponse(
                false,
                new ArrayList<>()
            );
        }
    }

    /**
     * Costruisce la query di ricerca per Nominatim
     */
    private String buildAddressQuery(AddressValidationRequest request) {
        StringBuilder query = new StringBuilder();
        if (request.getVia() != null && !request.getVia().isEmpty()) {
            query.append(request.getVia());
        }
        // Aggiungi la città se presente
        if (request.getCitta() != null && !request.getCitta().isEmpty()) {
            query.append(" ").append(request.getCitta());
        }
        // Alla fine aggiungi Italia
        query.append(" Italia");
        // Sostituisci spazi multipli con singolo spazio, poi con +
        String q = query.toString().replaceAll("\\s+", " ").trim().replace(" ", "+");
        return q;
    }

    /**
     * Parse i risultati JSON di Nominatim in oggetti AddressSuggestion
     */
    private List<AddressSuggestion> parseSuggestions(JsonNode results) {
        List<AddressSuggestion> suggestions = new ArrayList<>();
        
        for (JsonNode result : results) {
            AddressSuggestion suggestion = new AddressSuggestion();
            
            // Display name completo
            suggestion.setDisplayName(result.path("display_name").asText());
            
            // Coordinate geografiche
            suggestion.setLat(result.path("lat").asDouble());
            suggestion.setLon(result.path("lon").asDouble());
            
            // Address details
            JsonNode address = result.path("address");
            if (address != null && !address.isMissingNode()) {
                // Via/Road
                String via = address.path("road").asText(null);
                if (via == null) via = address.path("street").asText(null);
                if (via == null) via = address.path("pedestrian").asText(null);
                suggestion.setVia(via);
                
                // Città
                String citta = address.path("city").asText(null);
                if (citta == null) citta = address.path("town").asText(null);
                if (citta == null) citta = address.path("village").asText(null);
                if (citta == null) citta = address.path("municipality").asText(null);
                suggestion.setCitta(citta);
                
                // CAP
                suggestion.setCap(address.path("postcode").asText(null));
                
                // Campo provincia rimosso
            }
            
            suggestions.add(suggestion);
        }
        
        return suggestions;
    }
}
