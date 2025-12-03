/**
 * Entity Immobile - Rappresenta una proprietà immobiliare nel sistema.
 * 
 * Un immobile contiene:
 * - Dati identificativi (indirizzo, città, CAP, provincia)
 * - Caratteristiche tecniche (metratura, stanze, bagni, piano, tipologia)
 * - Dotazioni (garage, giardino, balcone, terrazzo, cantina, ascensore)
 * - Dati amministrativi (prezzo, descrizione, stato, data registrazione)
 * - Relazioni con proprietario e valutazioni
 * 
 * @author Sistema IMMOBILIARIS
 * @version 1.0
 * @since 2025-12-01
 */
package com.immobiliaris.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "Immobili")
public class Immobile {
    
    /**
     * Identificativo univoco dell'immobile (Primary Key).
     * Auto-generato dal database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_immobile")
    private Integer id;
    
    /**
     * Via/corso/viale dell'immobile.
     * Esempio: "Via Roma 123", "Corso Vittorio 45"
     */
    @Column(name = "Via")
    private String via;
    
    /**
     * Città dell'immobile.
     * Valori accettati: Torino, Cuneo, Alessandria, Asti
     */
    @Column(name = "Citta")
    private String citta;
    
    /**
     * Codice Postale (CAP).
     * Utilizzato per il calcolo del prezzo AI tramite tabella Zone.
     */
    @Column(name = "CAP")
    private String cap;
    
    /**
     * Sigla provincia (es: TO, CN, AL, AT).
     * Impostata automaticamente in base alla città.
     */
    @Column(name = "Provincia")
    private String provincia;
    
    /**
     * Tipologia immobile.
     * Valori: Appartamento, Villa, Attico, Loft, Ufficio, Negozio, ecc.
     * Influenza il calcolo del prezzo AI (+30% per villa, +12% per attico/loft).
     */
    @Column(name = "Tipologia")
    private String tipologia;
    
    /**
     * Superficie calpestabile in metri quadri.
     * Utilizzata per il calcolo del prezzo AI.
     */
    @Column(name = "Metratura")
    private Integer metratura;
    
    /**
     * Stato di conservazione dell'immobile.
     * Valori: Nuovo, Ottimo, Buono, Da ristrutturare
     * Influenza il calcolo del prezzo AI (+15% nuovo, +10% ottimo, -25% da ristrutturare).
     */
    @Column(name = "Condizioni")
    private String condizioni;
    
    /**
     * Numero di stanze (camere da letto).
     * Utilizzato per verifica funzionale nel calcolo AI.
     */
    @Column(name = "Stanze")
    private Integer stanze;

    /**
     * Numero di bagni.
     * Utilizzato per verifica funzionale nel calcolo AI.
     */
    @Column(name = "Bagni")
    private Integer bagni;

    /**
     * Sistema di riscaldamento.
     * Valori: Centralizzato, Autonomo, Teleriscaldamento, Nessuno
     * Influenza il calcolo del prezzo AI:
     * - Teleriscaldamento: +8%
     * - Autonomo: +5%
     * - Centralizzato: -3%
     */
    @Column(name = "Riscaldamento")
    private String riscaldamento;
    
    /**
     * Relazione ManyToOne con StatoImmobile.
     * Indica lo stato corrente (disponibile, venduto, in_verifica, ecc.).
     * 
     * @see StatoImmobile
     */
    @ManyToOne
    @JoinColumn(name = "Id_stato_immobile")
    private StatoImmobile statoImmobile;
    
    /**
     * Piano dell'immobile.
     * Per appartamenti: numero piano (0=piano terra, 1=primo piano, ecc.)
     * Per ville: numero di livelli/piani
     * Influenza il prezzo AI: +2% per piano negli appartamenti, +10% per ville multipiano.
     */
    @Column(name = "Piano")
    private Integer piano;
    
    /**
     * Presenza di ascensore.
     * Influenza il calcolo del prezzo AI: +8%
     */
    @Column(name = "Ascensore")
    private Boolean ascensore;
    
    /**
     * Presenza di garage.
     * Influenza il calcolo del prezzo AI: +25% (bonus maggiorato)
     */
    @Column(name = "Garage")
    private Boolean garage;

    /**
     * Presenza di giardino.
     * Influenza il calcolo del prezzo AI: +10%
     */
    @Column(name = "Giardino")
    private Boolean giardino;

    /**
     * Presenza di balcone.
     * Influenza il calcolo del prezzo AI: +5%
     */
    @Column(name = "Balcone")
    private Boolean balcone;

    /**
     * Presenza di terrazzo.
     * Influenza il calcolo del prezzo AI: +12%
     */
    @Column(name = "Terrazzo")
    private Boolean terrazzo;

    /**
     * Presenza di cantina.
     * Influenza il calcolo del prezzo AI: +3%
     */
    @Column(name = "Cantina")
    private Boolean cantina;
    
    /**
     * Prezzo di mercato dell'immobile.
     * Può provenire da:
     * 1. Prezzo_Umano dalla valutazione dell'agente (prioritario)
     * 2. Prezzo_AI calcolato automaticamente
     * Opzionale al momento della registrazione.
     */
    @Column(name = "Prezzo")
    private Integer prezzo;
    
    /**
     * Descrizione dettagliata dell'immobile.
     * Contiene informazioni aggiuntive non coperte dai campi strutturati.
     */
    @Column(name = "Descrizione")
    private String descrizione;
    
    /**
     * Data e ora di registrazione dell'immobile nel sistema.
     * Impostata automaticamente al momento della creazione.
     * Non aggiornabile.
     */
    @CreationTimestamp
    @Column(name = "Data_registrazione", updatable = false)
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dataRegistrazione;
    
    /**
     * Relazione ManyToOne con User (proprietario).
     * Fa riferimento all'utente che possiede/gestisce l'immobile.
     * Se non esiste un proprietario con quella email, viene creato automaticamente
     * con ruolo CLIENT al momento della registrazione dell'immobile.
     * 
     * @see User
     */
    @ManyToOne
    @JoinColumn(name = "Id_utente")
    private User proprietario;
    
    // Getters e Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
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
    
    public String getTipologia() {
        return tipologia;
    }
    
    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }
    
    public Integer getMetratura() {
        return metratura;
    }
    
    public void setMetratura(Integer metratura) {
        this.metratura = metratura;
    }
    
    public String getCondizioni() {
        return condizioni;
    }
    
    public void setCondizioni(String condizioni) {
        this.condizioni = condizioni;
    }
    
    public Integer getStanze() {
        return stanze;
    }
    
    public void setStanze(Integer stanze) {
        this.stanze = stanze;
    }

    public Integer getBagni() {
        return bagni;
    }

    public void setBagni(Integer bagni) {
        this.bagni = bagni;
    }

    public String getRiscaldamento() {
        return riscaldamento;
    }

    public void setRiscaldamento(String riscaldamento) {
        this.riscaldamento = riscaldamento;
    }
    
    public StatoImmobile getStatoImmobile() {
        return statoImmobile;
    }
    
    public void setStatoImmobile(StatoImmobile statoImmobile) {
        this.statoImmobile = statoImmobile;
    }
    
    public Integer getPiano() {
        return piano;
    }
    
    public void setPiano(Integer piano) {
        this.piano = piano;
    }
    
    public Boolean getAscensore() {
        return ascensore;
    }
    
    public void setAscensore(Boolean ascensore) {
        this.ascensore = ascensore;
    }
    
    public Boolean getGarage() {
        return garage;
    }
    
    public void setGarage(Boolean garage) {
        this.garage = garage;
    }

    public Boolean getGiardino() {
        return giardino;
    }

    public void setGiardino(Boolean giardino) {
        this.giardino = giardino;
    }

    public Boolean getBalcone() {
        return balcone;
    }

    public void setBalcone(Boolean balcone) {
        this.balcone = balcone;
    }

    public Boolean getTerrazzo() {
        return terrazzo;
    }

    public void setTerrazzo(Boolean terrazzo) {
        this.terrazzo = terrazzo;
    }

    public Boolean getCantina() {
        return cantina;
    }

    public void setCantina(Boolean cantina) {
        this.cantina = cantina;
    }
    
    public Integer getPrezzo() {
        return prezzo;
    }
    
    public void setPrezzo(Integer prezzo) {
        this.prezzo = prezzo;
    }
    
    public String getDescrizione() {
        return descrizione;
    }
    
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
    
    public LocalDateTime getDataRegistrazione() {
        return dataRegistrazione;
    }

    
    public User getProprietario() {
        return proprietario;
    }
    
    public void setProprietario(User proprietario) {
        this.proprietario = proprietario;
    }
}
