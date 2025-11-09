package com.immobiliaris.demo.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class ValutazioneResponse {
    private double valoreStimato;
    private double rangeMin;
    private double rangeMax;
}
