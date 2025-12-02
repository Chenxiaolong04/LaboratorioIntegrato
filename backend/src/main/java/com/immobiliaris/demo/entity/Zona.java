package com.immobiliaris.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "zone")
public class Zona {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_zona")
    private Integer idZona;
    
    @Column(name = "nome_quartiere", nullable = false)
    private String nomeQuartiere;
    
    @Column(name = "cap", nullable = false, length = 5)
    private String cap;
    
    @Column(name = "prezzo_medio_mq", nullable = false)
    private Integer prezzoMedioMq;

    public Zona() {}

    public Zona(String nomeQuartiere, String cap, Integer prezzoMedioMq) {
        this.nomeQuartiere = nomeQuartiere;
        this.cap = cap;
        this.prezzoMedioMq = prezzoMedioMq;
    }

    public Integer getIdZona() {
        return idZona;
    }

    public void setIdZona(Integer idZona) {
        this.idZona = idZona;
    }

    public String getNomeQuartiere() {
        return nomeQuartiere;
    }

    public void setNomeQuartiere(String nomeQuartiere) {
        this.nomeQuartiere = nomeQuartiere;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public Integer getPrezzoMedioMq() {
        return prezzoMedioMq;
    }

    public void setPrezzoMedioMq(Integer prezzoMedioMq) {
        this.prezzoMedioMq = prezzoMedioMq;
    }
}
