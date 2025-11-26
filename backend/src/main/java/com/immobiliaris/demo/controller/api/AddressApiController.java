package com.immobiliaris.demo.controller.api;

import com.immobiliaris.demo.service.AddressValidationService;
import com.immobiliaris.demo.dto.AddressValidationRequest;
import com.immobiliaris.demo.dto.AddressValidationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/address")
@CrossOrigin(origins = "*")
public class AddressApiController {

    @Autowired
    private AddressValidationService addressValidationService;

    /**
     * Valida un indirizzo usando Geoapify API
     * 
     * POST /api/address/validate
     * 
     * @param request Dati dell'indirizzo da validare (via, citta, cap, provincia)
     * @return Risposta con validazione e suggerimenti di indirizzi corretti
     */
    @PostMapping("/validate")
    public ResponseEntity<AddressValidationResponse> validateAddress(@RequestBody AddressValidationRequest request) {
        
        // Ora accetta qualsiasi combinazione di campi, anche solo uno
        AddressValidationResponse response = addressValidationService.validateAddress(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint di test per verificare che l'API sia raggiungibile
     * 
     * GET /api/address/test
     */
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Address validation API is working! ✅");
    }
}
