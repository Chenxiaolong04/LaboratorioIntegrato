package com.immobiliaris.demo.dto;

import lombok.Data;

@Data
public class ValutazioneRequest {
    private String citta;
    private double metratura;
    private int piano;
    private boolean ascensore;
    private String stato;
}
