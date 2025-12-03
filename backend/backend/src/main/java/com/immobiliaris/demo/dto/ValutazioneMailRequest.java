package com.immobiliaris.demo.dto;

public class ValutazioneMailRequest {
    private Integer idValutazione;

    public ValutazioneMailRequest() {}

    public ValutazioneMailRequest(Integer idValutazione) {
        this.idValutazione = idValutazione;
    }

    public Integer getIdValutazione() {
        return idValutazione;
    }

    public void setIdValutazione(Integer idValutazione) {
        this.idValutazione = idValutazione;
    }
}
