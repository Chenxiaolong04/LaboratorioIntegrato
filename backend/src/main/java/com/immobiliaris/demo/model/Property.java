package com.immobiliaris.demo.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Immobili")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int propertyId;    
    private String street;
    private String city;
    private String zipCode;
    private String province;
    private String type;
    private int squareMeters;
    private int rooms;
    private int bathrooms;
    private int floor;
    private boolean elevator;
    private boolean garage;
    private Integer price;
    private String description;
    private LocalDate createdAt;
}
