package com.immobiliaris.demo.dto;

import lombok.Data;

@Data
public class PropertyRequest {
    
    // Property details
    private String street;
    private String city;
    private String province;
    private String type;
    private Integer squareMeters;
    private String condizioni;
    private Integer rooms;
    private Integer bathrooms;
    private String riscaldamento;
    private Integer floor;
    private Boolean elevator;
    private Boolean garage;
    private Boolean giardino;
    private Boolean balcone;
    private Boolean terrazzo;
    private Boolean cantina;
    private String description;
    
    // Contact details (for CRM)
    private String contactName;
    private String contactEmail;
    private String contactPhone;
    
}
