/**
 * Entity Valutazione - Rappresenta la valutazione di prezzo di un immobile.
 * 
 * Una valutazione può passare per tre stati:
 * 1. SOLO_AI: Valutazione generata automaticamente dall'algoritmo AI
 * 2. IN_VERIFICA: Valutazione in corso di verifica da un agente
 * 3. APPROVATA: Valutazione verificata e approvata dall'agente
 * 
 * Contiene:
 * - Prezzo stimato dall'AI (algoritmo multi-fattoriale)
 * - Prezzo stimato dall'agente (dopo verifica)
 * - Stato della valutazione
 * - Agente assegnato alla verifica
 * - Immobile valutato
 * 
 * @author Sistema IMMOBILIARIS
 * @version 1.0
 * @since 2025-12-01
 */
package com.immobiliaris.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "Valutazioni")
public class Valutazione {
    
    /**
     * Identificativo univoco della valutazione (Primary Key).
     * Auto-generato dal database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_valutazione")
    private Integer id;
    
    /**
     * Prezzo stimato dall'intelligenza artificiale.
     * Calcolato tramite algoritmo multi-fattoriale che considera:
     * - Quotazione base del CAP (tabella Zone)
     * - Coefficiente di efficienza funzionale (bagni/stanze vs metratura)
     * - Coefficiente qualitativo (condizioni + tipologia)
     * - Moltiplicatore accessori (garage, terrazzo, giardino, ecc.)
     * - Fattore riscaldamento e piano
     * 
     * Formula finale: Prezzo_AI = (metratura × quotazione_CAP) × C_Funzionale × C_Qualitativo × M_Finale
     */
    @Column(name = "Prezzo_AI")
    private Integer prezzoAI;
    
    /**
     * Prezzo stimato manualmente dall'agente.
     * Compilato solo dopo che l'agente ha verificato la valutazione AI.
     * Null fino a quando lo stato non diventa IN_VERIFICA o APPROVATA.
     */
    @Column(name = "Prezzo_Umano")
    private Integer prezzoUmano;
    
    /**
     * Data e ora di creazione della valutazione.
     * Immutabile dopo l'inserimento.
     */
    @Column(name = "Data_valutazione")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dataValutazione;
    
    /**
     * Relazione ManyToOne con StatoValutazione.
     * Indica lo stato corrente della valutazione:
     * - SOLO_AI: Generata solo dall'AI, nessuna verifica umana
     * - IN_VERIFICA: Assegnata a un agente per verifica
     * - APPROVATA: Verificata e approvata dall'agente
     * 
     * @see StatoValutazione
     */
    @ManyToOne
    @JoinColumn(name = "Id_stato_valutazione")
    private StatoValutazione statoValutazione;
    
    /**
     * Note e commenti sulla valutazione.
     * Può contenere osservazioni dell'AI o dell'agente.
     */
    @Column(name = "Descrizione")
    private String descrizione;
    
    /**
     * Relazione ManyToOne con User (agente).
     * Riferimento all'agente assegnato per verificare la valutazione.
     * Null se lo stato è SOLO_AI (nessun agente ancora assegnato).
     * Non null se lo stato è IN_VERIFICA o APPROVATA.
     * 
     * @see User
     */
    @ManyToOne
    @JoinColumn(name = "Id_agente")
    private User agente;
    
    /**
     * Relazione ManyToOne con Immobile.
     * Riferimento all'immobile che viene valutato.
     * Una valutazione esiste sempre per uno e un solo immobile.
     * Un immobile può avere più valutazioni (storico), ma solo una è "attiva".
     * 
     * @see Immobile
     */
    @ManyToOne
    @JoinColumn(name = "Id_immobile")
    private Immobile immobile;
    
    // Getters e Setters
    /**
     * Ottiene l'ID della valutazione.
     * @return ID univoco della valutazione
     */
    public Integer getId() {
        return id;
    }
    
    /**
     * Imposta l'ID della valutazione.
     * @param id ID univoco della valutazione
     */
    public void setId(Integer id) {
        this.id = id;
    }
    
    /**
     * Ottiene il prezzo stimato dall'AI.
     * @return prezzo in euro
     */
    public Integer getPrezzoAI() {
        return prezzoAI;
    }
    
    /**
     * Imposta il prezzo stimato dall'AI.
     * @param prezzoAI prezzo in euro
     */
    public void setPrezzoAI(Integer prezzoAI) {
        this.prezzoAI = prezzoAI;
    }
    
    /**
     * Ottiene il prezzo stimato dall'agente.
     * @return prezzo in euro (null se non ancora valutato)
     */
    public Integer getPrezzoUmano() {
        return prezzoUmano;
    }
    
    /**
     * Imposta il prezzo stimato dall'agente.
     * @param prezzoUmano prezzo in euro
     */
    public void setPrezzoUmano(Integer prezzoUmano) {
        this.prezzoUmano = prezzoUmano;
    }
    
    /**
     * Ottiene la data della valutazione.
     * @return data e ora in LocalDateTime
     */
    public LocalDateTime getDataValutazione() {
        return dataValutazione;
    }
    
    /**
     * Imposta la data della valutazione.
     * @param dataValutazione data e ora in LocalDateTime
     */
    public void setDataValutazione(LocalDateTime dataValutazione) {
        this.dataValutazione = dataValutazione;
    }
    
    /**
     * Ottiene lo stato della valutazione.
     * @return StatoValutazione (SOLO_AI, IN_VERIFICA, APPROVATA)
     */
    public StatoValutazione getStatoValutazione() {
        return statoValutazione;
    }
    
    /**
     * Imposta lo stato della valutazione.
     * @param statoValutazione StatoValutazione (SOLO_AI, IN_VERIFICA, APPROVATA)
     */
    public void setStatoValutazione(StatoValutazione statoValutazione) {
        this.statoValutazione = statoValutazione;
    }
    
    /**
     * Ottiene la descrizione della valutazione.
     * @return descrizione/note sulla valutazione
     */
    public String getDescrizione() {
        return descrizione;
    }
    
    /**
     * Imposta la descrizione della valutazione.
     * @param descrizione descrizione/note sulla valutazione
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
    
    /**
     * Ottiene l'agente assegnato alla valutazione.
     * @return User (agente) o null se non assegnato
     */
    public User getAgente() {
        return agente;
    }
    
    /**
     * Imposta l'agente assegnato alla valutazione.
     * @param agente User (agente)
     */
    public void setAgente(User agente) {
        this.agente = agente;
    }
    
    /**
     * Ottiene l'immobile valutato.
     * @return Immobile valutato
     */
    public Immobile getImmobile() {
        return immobile;
    }
    
    /**
     * Imposta l'immobile da valutare.
     * @param immobile Immobile da valutare
     */
    public void setImmobile(Immobile immobile) {
        this.immobile = immobile;
    }
}
