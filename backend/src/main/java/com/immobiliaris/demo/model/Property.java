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
    @Column(name = "Id_immobile")
    private int propertyId;
    
    @Column(name = "Via")
    private String street;
    
    @Column(name = "Citta")
    private String city;
    
    @Column(name = "CAP")
    private String zipCode;
    
    @Column(name = "Provincia")
    private String province;
    
    @Column(name = "Tipologia")
    private String type;
    
    @Column(name = "Metratura")
    private int squareMeters;
    
    @Column(name = "Stanze")
    private int rooms;
    
    @Column(name = "Bagni")
    private int bathrooms;
    
    @Column(name = "Piano")
    private int floor;
    
    @Column(name = "Ascensore")
    private boolean elevator;
    
    @Column(name = "Garage")
    private boolean garage;
    
    @Column(name = "Prezzo")
    private Integer price;
    
    @Column(name = "Descrizione")
    private String description;
    
    @Column(name = "Data_inserimento")
    private LocalDate createdAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Id_stato_immobile", referencedColumnName = "Id_stato_immobile")
    private StatoImmobile statoImmobile;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Id_utente", referencedColumnName = "Id_utente")
    private User utente;
}
