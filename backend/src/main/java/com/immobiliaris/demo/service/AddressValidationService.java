package com.immobiliaris.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.immobiliaris.demo.dto.AddressValidationRequest;
import com.immobiliaris.demo.dto.AddressValidationResponse;
import com.immobiliaris.demo.dto.AddressValidationResponse.AddressSuggestion;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressValidationService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${geoapify.api.key:your_api_key_here}")
    private String geoapifyApiKey;

    public AddressValidationService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Valida un indirizzo usando Geoapify API
     * 
     * @param request Dati dell'indirizzo da validare
     * @return Risposta con validazione e suggerimenti
     */
    public AddressValidationResponse validateAddress(AddressValidationRequest request) {
        // Controllo validazione input
        if (request.getVia() == null || request.getVia().trim().isEmpty()) {
            return new AddressValidationResponse(false, new java.util.ArrayList<>());
        }

        String viaLower = request.getVia().trim().toLowerCase();
        
        // Estrai il civico inserito dall'utente (es: "Via Roma 10" -> "10")
        String civicoInserito = extractCivico(request.getVia().trim());

        // Non accettare il nome della città nel campo via
        String[] cittaAccettate = {"torino", "cuneo", "alessandria", "asti"};
        for (String citta : cittaAccettate) {
            if (viaLower.contains(citta)) {
                return new AddressValidationResponse(false, new java.util.ArrayList<>());
            }
        }

        // Valida il tipo di strada
        String[] tipiValidi = {"via", "viale", "corso", "piazza", "strada", "largo", 
                              "vicolo", "piazzale", "ronco", "regione", "frazione", 
                              "borgo", "contrada", "località", "ca"};
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
            // Costruisci indirizzo completo
            StringBuilder addressBuilder = new StringBuilder();
            addressBuilder.append(request.getVia());
            if (request.getCitta() != null && !request.getCitta().isEmpty()) {
                addressBuilder.append(", ").append(request.getCitta());
            }
            if (request.getCap() != null && !request.getCap().isEmpty()) {
                addressBuilder.append(" ").append(request.getCap());
            }
            addressBuilder.append(", Italy");

            String address = addressBuilder.toString();
            System.out.println("[DEBUG] Geoapify Query: " + address);
            System.out.println("[DEBUG] Geoapify API Key: " + (geoapifyApiKey != null && !geoapifyApiKey.isEmpty() && !geoapifyApiKey.equals("your_api_key_here") ? "✓ Configurata" : "✗ Non configurata o di default"));

            // Chiama Geoapify API
            String url = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("api.geoapify.com")
                .path("/v1/geocode/search")
                .queryParam("text", address)
                .queryParam("apiKey", geoapifyApiKey)
                .queryParam("limit", "5")
                .build()
                .toUriString();

            System.out.println("[DEBUG] Geoapify URL: " + url);

            org.springframework.http.ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            System.out.println("[DEBUG] Geoapify Response: " + response.getBody());
            JsonNode results = objectMapper.readTree(response.getBody());

            List<AddressSuggestion> suggestions = parseSuggestions(results, civicoInserito);

            // Filtra per città se specificata
            // Normalizza i nomi delle città perché Geoapify restituisce "Turin" invece di "Torino"
            if (request.getCitta() != null && !request.getCitta().isEmpty()) {
                String cittaRichiesta = normalizeCityName(request.getCitta().trim().toLowerCase());
                suggestions.removeIf(s -> s.getCitta() == null || 
                                          !normalizeCityName(s.getCitta().trim().toLowerCase()).equals(cittaRichiesta));
            }

            // Rimuovi duplicati
            java.util.Set<String> uniqueKeys = new java.util.HashSet<>();
            suggestions.removeIf(s -> {
                String key = (s.getVia() != null ? s.getVia().trim().toLowerCase() : "") + 
                            "|" + (s.getCitta() != null ? s.getCitta().trim().toLowerCase() : "");
                if (uniqueKeys.contains(key)) {
                    return true;
                } else {
                    uniqueKeys.add(key);
                    return false;
                }
            });

            // Filtro: solo vie che iniziano con la stringa inserita
            if (request.getVia() != null && !request.getVia().trim().isEmpty()) {
                String viaInput = request.getVia().trim().toLowerCase();
                List<AddressSuggestion> filtered = new ArrayList<>();
                for (AddressSuggestion s : suggestions) {
                    if (s.getVia() != null && s.getVia().toLowerCase().startsWith(viaInput)) {
                        filtered.add(s);
                    }
                }
                // Se almeno una via corrisponde, restituisci solo quelle
                if (!filtered.isEmpty()) {
                    suggestions = filtered;
                }
            }

            return new AddressValidationResponse(!suggestions.isEmpty(), suggestions);
        } catch (Exception e) {
            System.err.println("[ERROR] Geoapify validation error: " + e.getMessage());
            e.printStackTrace();
            return new AddressValidationResponse(false, new java.util.ArrayList<>());
        }
    }

    /**
     * Costruisce la query di ricerca per Nominatim
     */
    // Metodo rimosso: non più necessario con Geoapify

    /**
     * Normalizza i nomi delle città
     * Geoapify restituisce nomi inglesi, convertiamo in italiani
     */
    private String normalizeCityName(String city) {
        if (city == null) return "";
        switch (city.toLowerCase()) {
            case "turin":
            case "torino":
                return "torino";
            case "cuneo":
                return "cuneo";
            case "alessandria":
                return "alessandria";
            case "asti":
                return "asti";
            default:
                return city;
        }
    }

    /**
     * Parse i risultati JSON di Geoapify in oggetti AddressSuggestion
     */
    private List<AddressSuggestion> parseSuggestions(JsonNode results, String civicoInserito) {
        List<AddressSuggestion> suggestions = new ArrayList<>();

        if (results == null || !results.has("features")) {
            return suggestions;
        }

        JsonNode features = results.path("features");
        if (!features.isArray()) {
            return suggestions;
        }

        for (JsonNode feature : features) {
            try {
                JsonNode properties = feature.path("properties");
                if (properties.isMissingNode()) continue;

                System.out.println("[DEBUG] Parsing feature properties: " + properties.toString());

                AddressSuggestion suggestion = new AddressSuggestion();

                // Estrai coordinate - Geoapify ha lat/lon direttamente in properties
                double lat = properties.path("lat").asDouble(0);
                double lon = properties.path("lon").asDouble(0);
                System.out.println("[DEBUG] Coordinates - lat: " + lat + ", lon: " + lon);
                suggestion.setLat(lat);
                suggestion.setLon(lon);

                // Estrai indirizzo
                String street = properties.path("street").asText(null);
                String city = properties.path("city").asText(null);
                String postcode = properties.path("postcode").asText(null);
                String housenumber = properties.path("housenumber").asText(null);
                
                // Se Geoapify non ha il civico, usa quello inserito dall'utente
                if (housenumber == null && civicoInserito != null) {
                    housenumber = civicoInserito;
                }
                
                System.out.println("[DEBUG] Address - street: " + street + ", city: " + city + ", postcode: " + postcode + ", housenumber: " + housenumber);

                suggestion.setVia(street);
                suggestion.setCitta(city);
                suggestion.setCap(postcode);
                suggestion.setCivico(housenumber);

                // Costruisci display name personalizzato
                StringBuilder display = new StringBuilder();
                if (street != null) display.append(street);
                if (housenumber != null) display.append(" ").append(housenumber);
                if (city != null) display.append(", ").append(city);
                if (postcode != null) display.append(" ").append(postcode);
                suggestion.setDisplayName(display.toString().trim());

                // Genera URL mappa statica Geoapify
                String mapUrl = String.format(java.util.Locale.US,
                    "https://maps.geoapify.com/v1/staticmap?style=osm-carto&center=lonlat:%.6f,%.6f&zoom=15&width=600&height=400&marker=lonlat:%.6f,%.6f;color:red;size:medium&apiKey=%s",
                    lon, lat, lon, lat, geoapifyApiKey
                );
                suggestion.setMapUrl(mapUrl);
                System.out.println("[DEBUG] Generated map URL: " + mapUrl);

                suggestions.add(suggestion);
            } catch (Exception e) {
                System.err.println("[WARNING] Error parsing feature: " + e.getMessage());
            }
        }

        return suggestions;
    }

    /**
     * Estrae il civico da una stringa di indirizzo (es: "Via Roma 10" -> "10")
     */
    private String extractCivico(String via) {
        if (via == null || via.isEmpty()) return null;
        String[] parts = via.trim().split(" ");
        if (parts.length > 1) {
            String lastPart = parts[parts.length - 1];
            if (lastPart.matches("\\d+[a-zA-Z]?$")) {
                return lastPart;
            }
        }
        return null;
    }
}
