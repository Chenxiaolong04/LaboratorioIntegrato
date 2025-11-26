package com.immobiliaris.demo.dto;

public class AddressValidationRequest {
    private String via;
    private String citta;
    private String cap;
    private String provincia;

    public AddressValidationRequest() {
    }

    public AddressValidationRequest(String via, String citta, String cap, String provincia) {
        this.via = via;
        this.citta = citta;
        this.cap = cap;
        this.provincia = provincia;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }
}
