package com.immobiliaris.demo.dto;

import lombok.Data;

@Data
public class PropertyRequest {
    
    private String street;
    private String city;
    private String province;
    private String type;
    private Integer squareMeters;
    private Integer rooms;
    private Integer floor;
    private Boolean elevator;
    private Boolean garage;
    private String description;
    
}
