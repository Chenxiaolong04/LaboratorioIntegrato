/**
 * Entity Contratto - Rappresenta un contratto di mediazione immobiliare.
 * 
 * Il contratto formalizza l'accordo tra:
 * - MANDANTE (proprietario): Chi incarica l'agente di vendere/affittare
 * - AGENTE: Chi si incarica di promuovere la proprietà
 * 
 * Il contratto include:
 * - Dati identificativi (numero, date)
 * - Riferimenti alle parti (agente, proprietario/utente)
 * - Condizioni economiche (commissione, prezzo)
 * - Stato del contratto
 * - Collegamento alla valutazione associata
 * 
 * @author Sistema IMMOBILIARIS
 * @version 1.0
 * @since 2025-12-01
 */
package com.immobiliaris.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Contratti")
public class Contratto {
    
    /**
     * Identificativo univoco del contratto (Primary Key).
     * Auto-generato dal database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_contratto")
    private Long id;

    /**
     * Data e ora di registrazione del contratto nel sistema.
     * Impostata al momento della creazione.
     */
    @Column(name = "Data_registrazione")
    private LocalDateTime dataRegistrazione;

    /**
     * Data di inizio della mediazione/mandato.
     * Data da cui inizia l'incarico dell'agente.
     */
    private LocalDateTime dataInizio;
    
    /**
     * Data di fine della mediazione/mandato.
     * Data di scadenza dell'incarico dell'agente.
     * Normalmente impostata a 6 mesi dalla data di inizio.
     */
    private LocalDateTime dataFine;

    /**
     * Relazione ManyToOne con User (agente).
     * Riferimento all'agente immobiliare che gestisce il contratto.
     * L'agente è responsabile della promozione della proprietà.
     * 
     * @see User
     */
    @ManyToOne
    @JoinColumn(name = "Id_agente")
    private User agente;

    /**
     * Relazione ManyToOne con User (proprietario/mandante).
     * Riferimento al proprietario che incarica l'agente.
     * È il soggetto che intende vendere/affittare l'immobile.
     * 
     * @see User
     */
    @ManyToOne
    @JoinColumn(name = "Id_utente")
    private User utente;

    /**
     * Relazione ManyToOne con Immobile.
     * Riferimento all'immobile oggetto del contratto.
     * Un contratto riguarda uno e un solo immobile.
     * 
     * @see Immobile
     */
    @ManyToOne
    @JoinColumn(name = "Id_immobile")
    private Immobile immobile;

    /**
     * Relazione ManyToOne con StatoContratto.
     * Indica lo stato corrente del contratto:
     * - BOZZA: In preparazione, non ancora attivo
     * - ATTIVO: Contratto firmato e in corso
     * - SOSPESO: Temporaneamente non attivo
     * - CHIUSO: Terminato con successo (immobile venduto/affittato)
     * - ANNULLATO: Annullato prima della conclusione
     * 
     * @see StatoContratto
     */
    @ManyToOne
    @JoinColumn(name = "Id_stato_contratto")
    private StatoContratto statoContratto;

    /**
     * Numero identificativo univoco del contratto.
     * Formato tipico: "C-2025-001", "C-2025-002", ecc.
     * Utilizzato per riferimenti amministrativi.
     */
    private String numeroContratto;
    
    /**
     * Percentuale di commissione per l'agente.
     * Solitamente 3% del prezzo dell'immobile.
     * Viene calcolata sul campo prezzoUmano della valutazione.
     */
    private Double percentualeCommissione;

    /**
     * Data di invio del contratto al proprietario.
     * Data in cui il PDF viene inviato via email.
     */
    private LocalDateTime dataInvio;
    
    /**
     * Data di ricezione/firma del contratto da parte del proprietario.
     * Data in cui il proprietario firma e rimanda il contratto.
     */
    private LocalDateTime dataRicezione;

    /**
     * Relazione ManyToOne con Valutazione.
     * Riferimento alla valutazione che ha originato questo contratto.
     * Permette di tracciare il collegamento tra valutazione e contratto.
     * 
     * @see Valutazione
     */
    @ManyToOne
    @JoinColumn(name = "Id_valutazione")
    private Valutazione valutazione;

    // Getters e Setters
    
    /**
     * Ottiene la data di invio del contratto.
     * @return data e ora di invio
     */
    public LocalDateTime getDataInvio() {
        return dataInvio;
    }
    
    /**
     * Imposta la data di invio del contratto.
     * @param dataInvio data e ora di invio
     */
    public void setDataInvio(LocalDateTime dataInvio) {
        this.dataInvio = dataInvio;
    }
    
    /**
     * Ottiene la data di ricezione del contratto.
     * @return data e ora di ricezione/firma
     */
    public LocalDateTime getDataRicezione() {
        return dataRicezione;
    }
    
    /**
     * Imposta la data di ricezione del contratto.
     * @param dataRicezione data e ora di ricezione/firma
     */
    public void setDataRicezione(LocalDateTime dataRicezione) {
        this.dataRicezione = dataRicezione;
    }
    
    /**
     * Ottiene la valutazione associata al contratto.
     * @return Valutazione che ha originato il contratto
     */
    public Valutazione getValutazione() {
        return valutazione;
    }
    
    /**
     * Imposta la valutazione associata al contratto.
     * @param valutazione Valutazione che ha originato il contratto
     */
    public void setValutazione(Valutazione valutazione) {
        this.valutazione = valutazione;
    }
    
    /**
     * Ottiene l'ID del contratto.
     * @return ID univoco del contratto
     */
    public Long getId() {
        return id;
    }
    
    /**
     * Imposta l'ID del contratto.
     * @param id ID univoco del contratto
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * Ottiene la data di registrazione del contratto.
     * @return data e ora di registrazione
     */
    public LocalDateTime getDataRegistrazione() {
        return dataRegistrazione;
    }
    
    /**
     * Imposta la data di registrazione del contratto.
     * @param dataRegistrazione data e ora di registrazione
     */
    public void setDataRegistrazione(LocalDateTime dataRegistrazione) {
        this.dataRegistrazione = dataRegistrazione;
    }
    
    /**
     * Ottiene la data di inizio del contratto.
     * @return data e ora di inizio
     */
    public LocalDateTime getDataInizio() {
        return dataInizio;
    }
    
    /**
     * Imposta la data di inizio del contratto.
     * @param dataInizio data e ora di inizio
     */
    public void setDataInizio(LocalDateTime dataInizio) {
        this.dataInizio = dataInizio;
    }
    
    /**
     * Ottiene la data di fine del contratto.
     * @return data e ora di fine/scadenza
     */
    public LocalDateTime getDataFine() {
        return dataFine;
    }
    
    /**
     * Imposta la data di fine del contratto.
     * @param dataFine data e ora di fine/scadenza
     */
    public void setDataFine(LocalDateTime dataFine) {
        this.dataFine = dataFine;
    }
    
    /**
     * Ottiene l'agente associato al contratto.
     * @return User (agente)
     */
    public User getAgente() {
        return agente;
    }
    
    /**
     * Imposta l'agente associato al contratto.
     * @param agente User (agente)
     */
    public void setAgente(User agente) {
        this.agente = agente;
    }
    
    /**
     * Ottiene il proprietario/mandante del contratto.
     * @return User (proprietario)
     */
    public User getUtente() {
        return utente;
    }
    
    /**
     * Imposta il proprietario/mandante del contratto.
     * @param utente User (proprietario)
     */
    public void setUtente(User utente) {
        this.utente = utente;
    }
    
    /**
     * Ottiene l'immobile associato al contratto.
     * @return Immobile
     */
    public Immobile getImmobile() {
        return immobile;
    }
    
    /**
     * Imposta l'immobile associato al contratto.
     * @param immobile Immobile
     */
    public void setImmobile(Immobile immobile) {
        this.immobile = immobile;
    }
    
    /**
     * Ottiene lo stato del contratto.
     * @return StatoContratto (BOZZA, ATTIVO, SOSPESO, CHIUSO, ANNULLATO)
     */
    public StatoContratto getStatoContratto() {
        return statoContratto;
    }
    
    /**
     * Imposta lo stato del contratto.
     * @param statoContratto StatoContratto (BOZZA, ATTIVO, SOSPESO, CHIUSO, ANNULLATO)
     */
    public void setStatoContratto(StatoContratto statoContratto) {
        this.statoContratto = statoContratto;
    }
    
    /**
     * Ottiene il numero univoco del contratto.
     * @return numero del contratto (es: "C-2025-001")
     */
    public String getNumeroContratto() {
        return numeroContratto;
    }
    
    /**
     * Imposta il numero univoco del contratto.
     * @param numeroContratto numero del contratto (es: "C-2025-001")
     */
    public void setNumeroContratto(String numeroContratto) {
        this.numeroContratto = numeroContratto;
    }
    
    /**
     * Ottiene la percentuale di commissione dell'agente.
     * @return percentuale (es: 3.0 per 3%)
     */
    public Double getPercentualeCommissione() {
        return percentualeCommissione;
    }
    
    /**
     * Imposta la percentuale di commissione dell'agente.
     * @param percentualeCommissione percentuale (es: 3.0 per 3%)
     */
    public void setPercentualeCommissione(Double percentualeCommissione) {
        this.percentualeCommissione = percentualeCommissione;
    }
}
