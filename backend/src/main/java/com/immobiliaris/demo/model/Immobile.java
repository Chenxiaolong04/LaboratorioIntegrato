package com.immobiliaris.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Immobili")
public class Immobile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idImmobile;

    private String via;
    private String citta;
    private String cap;
    private String provincia;
    private int metratura;
    private int locali;
    private int bagni;
    private int piano;
    private boolean ascensore;
    private boolean garage;
    private double prezzo;
    private String descrizione;
    private String data_inserimento;
}
