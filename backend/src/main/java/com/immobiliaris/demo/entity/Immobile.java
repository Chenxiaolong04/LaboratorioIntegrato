package com.immobiliaris.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "Immobili")
public class Immobile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_immobile")
    private Integer id;
    
    @Column(name = "Via")
    private String via;
    
    @Column(name = "Citta")
    private String citta;
    
    @Column(name = "CAP")
    private String cap;
    
    @Column(name = "Provincia")
    private String provincia;
    
    @Column(name = "Tipologia")
    private String tipologia;
    
    @Column(name = "Metratura")
    private Integer metratura;
    
    @Column(name = "Condizioni")
    private String condizioni;
    
    @Column(name = "Stanze")
    private Integer stanze;

    @Column(name = "Bagni")
    private Integer bagni;

    @Column(name = "Riscaldamento")
    private String riscaldamento;
    
    @ManyToOne
    @JoinColumn(name = "Id_stato_immobile")
    private StatoImmobile statoImmobile;
    
    @Column(name = "Piano")
    private Integer piano;
    
    @Column(name = "Ascensore")
    private Boolean ascensore;
    
    @Column(name = "Garage")
    private Boolean garage;

    @Column(name = "Giardino")
    private Boolean giardino;

    @Column(name = "Balcone")
    private Boolean balcone;

    @Column(name = "Terrazzo")
    private Boolean terrazzo;

    @Column(name = "Cantina")
    private Boolean cantina;
    
    @Column(name = "Prezzo")
    private Integer prezzo;
    
    @Column(name = "Descrizione")
    private String descrizione;
    
    @CreationTimestamp
    @Column(name = "Data_registrazione", updatable = false)
    private LocalDateTime dataRegistrazione;
    
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
