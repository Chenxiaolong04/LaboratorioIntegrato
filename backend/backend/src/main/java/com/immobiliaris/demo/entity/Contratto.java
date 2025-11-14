package com.immobiliaris.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Contratti")
public class Contratto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_contratto")
    private Integer id;
    
    @Column(name = "Data_invio")
    private LocalDate dataInvio;
    
    @Column(name = "Data_ricezione")
    private LocalDate dataRicezione;
    
    @Column(name = "Data_inizio")
    private LocalDate dataInizio;
    
    @Column(name = "Data_fine")
    private LocalDate dataFine;
    
    @ManyToOne
    @JoinColumn(name = "Id_stato_contratto")
    private StatoContratto statoContratto;
    
    @Column(name = "Numero_contratto")
    private String numeroContratto;
    
    @Column(name = "Percentuale_commissione")
    private Double percentualeCommissione;
    
    @ManyToOne
    @JoinColumn(name = "Id_agente")
    private User agente;
    
    @ManyToOne
    @JoinColumn(name = "Id_utente")
    private User utente;
    
    @ManyToOne
    @JoinColumn(name = "Id_immobile")
    private Immobile immobile;

    @ManyToOne
    @JoinColumn(name = "Id_valutazione")
    private Valutazione valutazione;
    
    // Getters e Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public LocalDate getDataInvio() {
        return dataInvio;
    }
    
    public void setDataInvio(LocalDate dataInvio) {
        this.dataInvio = dataInvio;
    }
    
    public LocalDate getDataRicezione() {
        return dataRicezione;
    }
    
    public void setDataRicezione(LocalDate dataRicezione) {
        this.dataRicezione = dataRicezione;
    }
    
    public LocalDate getDataInizio() {
        return dataInizio;
    }
    
    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
    }
    
    public LocalDate getDataFine() {
        return dataFine;
    }
    
    public void setDataFine(LocalDate dataFine) {
        this.dataFine = dataFine;
    }
    
    public StatoContratto getStatoContratto() {
        return statoContratto;
    }
    
    public void setStatoContratto(StatoContratto statoContratto) {
        this.statoContratto = statoContratto;
    }
    
    public String getNumeroContratto() {
        return numeroContratto;
    }
    
    public void setNumeroContratto(String numeroContratto) {
        this.numeroContratto = numeroContratto;
    }
    
    public Double getPercentualeCommissione() {
        return percentualeCommissione;
    }
    
    public void setPercentualeCommissione(Double percentualeCommissione) {
        this.percentualeCommissione = percentualeCommissione;
    }
    
    public User getAgente() {
        return agente;
    }
    
    public void setAgente(User agente) {
        this.agente = agente;
    }
    
    public User getUtente() {
        return utente;
    }
    
    public void setUtente(User utente) {
        this.utente = utente;
    }
    
    public Immobile getImmobile() {
        return immobile;
    }
    
    public void setImmobile(Immobile immobile) {
        this.immobile = immobile;
    }

    public Valutazione getValutazione() {
        return valutazione;
    }

    public void setValutazione(Valutazione valutazione) {
        this.valutazione = valutazione;
    }
}
