package com.immobiliaris.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PropertyRequest {
    
    // Property details
    @NotBlank(message = "La via è obbligatoria")
    private String street;

    @NotBlank(message = "La città è obbligatoria")
    private String city;

    private String zipCode; // opzionale (CAP)

    @NotBlank(message = "La provincia è obbligatoria")
    private String province;

    @NotBlank(message = "La tipologia è obbligatoria")
    private String type;

    @NotNull(message = "La metratura è obbligatoria")
    @Min(value = 0, message = "La metratura non può essere negativa")
    private Integer squareMeters;

    @NotNull(message = "Il numero di locali è obbligatorio")
    @Min(value = 0, message = "I locali non possono essere negativi")
    private Integer rooms;

    @NotNull(message = "Il numero di bagni è obbligatorio")
    @Min(value = 0, message = "I bagni non possono essere negativi")
    private Integer bathrooms;

    @NotNull(message = "Il piano è obbligatorio")
    @Min(value = 0, message = "Il piano non può essere negativo")
    private Integer floor;

    private Boolean elevator;
    private Boolean garage;
    private String description;
    
    // Contact details (for CRM)
    private String contactName;

    @Email(message = "Email non valida")
    private String contactEmail;

    private String contactPhone;
    
}
