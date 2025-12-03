/**
 * Entity StatoValutazione - Rappresenta gli stati possibili di una valutazione.
 * 
 * Stati disponibili:
 * 1. SOLO_AI - Valutazione generata solo dall'algoritmo AI, no intervento umano
 * 2. IN_VERIFICA - Valutazione assegnata a un agente per verifica e approvazione
 * 3. APPROVATA - Valutazione verificata e approvata dall'agente
 * 
 * Utilizzo:
 * - Ogni Valutazione ha un riferimento a StatoValutazione tramite @ManyToOne
 * - Determina il flusso di lavoro della valutazione
 * - Non può essere modificato direttamente dall'utente (solo da logica di business)
 * 
 * @author Sistema IMMOBILIARIS
 * @version 1.0
 * @since 2025-12-01
 * 
 * @see Valutazione
 */
package com.immobiliaris.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Stati_valutazione")
public class StatoValutazione {
    
    /**
     * Identificativo univoco dello stato (Primary Key).
     * Auto-generato dal database.
     * Valori tipici: 1=solo_AI, 2=in_verifica, 3=approvata
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_stato_valutazione")
    private Integer id;
    
    /**
     * Nome dello stato.
     * Valori: "solo_AI", "in_verifica", "approvata"
     * Utilizzato nelle query e comparazioni di business logic.
     */
    @Column(name = "Nome")
    private String nome;
    
    /**
     * Descrizione testuale dello stato.
     * Fornisce contesto su cosa significa lo stato.
     * Esempio: "Valutazione generata automaticamente, in attesa di assegnazione agente"
     */
    @Column(name = "Descrizione")
    private String descrizione;
    
    // Getters e Setters
    
    /**
     * Ottiene l'ID dello stato.
     * @return ID univoco (1, 2, 3, ecc.)
     */
    public Integer getId() {
        return id;
    }
    
    /**
     * Imposta l'ID dello stato.
     * @param id ID univoco
     */
    public void setId(Integer id) {
        this.id = id;
    }
    
    /**
     * Ottiene il nome dello stato.
     * @return Nome dello stato ("solo_AI", "in_verifica", "approvata")
     */
    public String getNome() {
        return nome;
    }
    
    /**
     * Imposta il nome dello stato.
     * @param nome Nome dello stato
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    /**
     * Ottiene la descrizione dello stato.
     * @return Descrizione testuale
     */
    public String getDescrizione() {
        return descrizione;
    }
    
    /**
     * Imposta la descrizione dello stato.
     * @param descrizione Descrizione testuale
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
}
