package com.immobiliaris.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "Valutazioni")
public class Valutazione {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_valutazione")
    private Integer id;
    
    @Column(name = "Prezzo_AI")
    private Integer prezzoAI;
    
    @Column(name = "Prezzo_Umano")
    private Integer prezzoUmano;
    
    @CreationTimestamp
    @Column(name = "Data_valutazione", updatable = false)
    private LocalDateTime dataValutazione;
    
    @ManyToOne
    @JoinColumn(name = "Id_stato_valutazione")
    private StatoValutazione statoValutazione;
    
    @Column(name = "Descrizione")
    private String descrizione;
    
    @ManyToOne
    @JoinColumn(name = "Id_agente")
    private User agente;
    
    @ManyToOne
    @JoinColumn(name = "Id_immobile")
    private Immobile immobile;
    
    // Getters e Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getPrezzoAI() {
        return prezzoAI;
    }
    
    public void setPrezzoAI(Integer prezzoAI) {
        this.prezzoAI = prezzoAI;
    }
    
    public Integer getPrezzoUmano() {
        return prezzoUmano;
    }
    
    public void setPrezzoUmano(Integer prezzoUmano) {
        this.prezzoUmano = prezzoUmano;
    }
    
    public LocalDateTime getDataValutazione() {
        return dataValutazione;
    }
    
    public void setDataValutazione(LocalDateTime dataValutazione) {
        this.dataValutazione = dataValutazione;
    }
    
    public StatoValutazione getStatoValutazione() {
        return statoValutazione;
    }
    
    public void setStatoValutazione(StatoValutazione statoValutazione) {
        this.statoValutazione = statoValutazione;
    }
    
    public String getDescrizione() {
        return descrizione;
    }
    
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
    
    public User getAgente() {
        return agente;
    }
    
    public void setAgente(User agente) {
        this.agente = agente;
    }
    
    public Immobile getImmobile() {
        return immobile;
    }
    
    public void setImmobile(Immobile immobile) {
        this.immobile = immobile;
    }
}
