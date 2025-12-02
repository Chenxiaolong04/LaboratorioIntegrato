package com.immobiliaris.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Contratti")
public class Contratto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_contratto")
    private Long id;

    @Column(name = "Data_registrazione")
    private LocalDateTime dataRegistrazione;

    private LocalDateTime dataInizio;
    private LocalDateTime dataFine;

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
    @JoinColumn(name = "Id_stato_contratto")
    private StatoContratto statoContratto;

    private String numeroContratto;
    private Double percentualeCommissione;

    private LocalDateTime dataInvio;
    private LocalDateTime dataRicezione;

    @ManyToOne
    @JoinColumn(name = "Id_valutazione")
    private Valutazione valutazione;

    // Getter e Setter
        public LocalDateTime getDataInvio() {
            return dataInvio;
        }
        public void setDataInvio(LocalDateTime dataInvio) {
            this.dataInvio = dataInvio;
        }
        public LocalDateTime getDataRicezione() {
            return dataRicezione;
        }
        public void setDataRicezione(LocalDateTime dataRicezione) {
            this.dataRicezione = dataRicezione;
        }
        public Valutazione getValutazione() {
            return valutazione;
        }
        public void setValutazione(Valutazione valutazione) {
            this.valutazione = valutazione;
        }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public LocalDateTime getDataRegistrazione() {
        return dataRegistrazione;
    }
    public void setDataRegistrazione(LocalDateTime dataRegistrazione) {
        this.dataRegistrazione = dataRegistrazione;
    }
    public LocalDateTime getDataInizio() {
        return dataInizio;
    }
    public void setDataInizio(LocalDateTime dataInizio) {
        this.dataInizio = dataInizio;
    }
    public LocalDateTime getDataFine() {
        return dataFine;
    }
    public void setDataFine(LocalDateTime dataFine) {
        this.dataFine = dataFine;
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
}
