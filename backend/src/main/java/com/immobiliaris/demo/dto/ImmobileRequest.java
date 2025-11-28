package com.immobiliaris.demo.dto;

import com.immobiliaris.demo.entity.Immobile;
import com.immobiliaris.demo.entity.User;

public class ImmobileRequest {
    private String via;
    private String citta;
    private String cap;
    private String provincia;
    private String tipologia;
    private Integer metratura;
    private String condizioni;
    private Integer stanze;
    private Integer bagni;
    private String riscaldamento;
    private Integer piano;
    private Boolean ascensore;
    private Boolean garage;
    private Boolean giardino;
    private Boolean balcone;
    private Boolean terrazzo;
    private Boolean cantina;
    private Integer prezzo;
    private String descrizione;
    private String descrizioneImmobile;
    
    // Campi piatti per proprietario
    private String nome;
    private String cognome;
    private String email;
    private String telefono;
    
    // Opzionalmente accetta anche proprietario annidato
    private ProprietarioRequest proprietario;

    public ImmobileRequest() {
    }

    /**
     * Converte il DTO in entità Immobile, supportando sia formato piatto che annidato per il proprietario
     */
    public Immobile toImmobile() {
        Immobile immobile = new Immobile();
        immobile.setVia(this.via);
        immobile.setCitta(this.citta);
        immobile.setCap(this.cap);
        immobile.setProvincia(this.provincia);
        immobile.setTipologia(this.tipologia);
        immobile.setMetratura(this.metratura);
        immobile.setCondizioni(this.condizioni);
        immobile.setStanze(this.stanze);
        immobile.setBagni(this.bagni);
        immobile.setRiscaldamento(this.riscaldamento);
        immobile.setPiano(this.piano);
        immobile.setAscensore(this.ascensore);
        immobile.setGarage(this.garage);
        immobile.setGiardino(this.giardino);
        immobile.setBalcone(this.balcone);
        immobile.setTerrazzo(this.terrazzo);
        immobile.setCantina(this.cantina);
        immobile.setPrezzo(this.prezzo);
        
        String desc = this.descrizione != null ? this.descrizione : this.descrizioneImmobile;
        immobile.setDescrizione(desc);

        String emailProprietario = null;
        String nomeProprietario = null;
        String cognomeProprietario = null;
        String telefonoProprietario = null;
        
        if (this.email != null) {
            emailProprietario = this.email;
            nomeProprietario = this.nome;
            cognomeProprietario = this.cognome;
            telefonoProprietario = this.telefono;
        }
        else if (this.proprietario != null && this.proprietario.getEmail() != null) {
            emailProprietario = this.proprietario.getEmail();
            nomeProprietario = this.proprietario.getNome();
            cognomeProprietario = this.proprietario.getCognome();
            telefonoProprietario = this.proprietario.getTelefono();
        }
        
        if (emailProprietario != null) {
            User user = new User();
            user.setEmail(emailProprietario);
            user.setNome(nomeProprietario);
            user.setCognome(cognomeProprietario);
            user.setTelefono(telefonoProprietario);
            immobile.setProprietario(user);
        }

        return immobile;
    }

    // Getters e Setters
    public String getVia() { return via; }
    public void setVia(String via) { this.via = via; }
    public String getCitta() { return citta; }
    public void setCitta(String citta) { this.citta = citta; }
    public String getCap() { return cap; }
    public void setCap(String cap) { this.cap = cap; }
    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }
    public String getTipologia() { return tipologia; }
    public void setTipologia(String tipologia) { this.tipologia = tipologia; }
    public Integer getMetratura() { return metratura; }
    public void setMetratura(Integer metratura) { this.metratura = metratura; }
    public String getCondizioni() { return condizioni; }
    public void setCondizioni(String condizioni) { this.condizioni = condizioni; }
    public Integer getStanze() { return stanze; }
    public void setStanze(Integer stanze) { this.stanze = stanze; }
    public Integer getBagni() { return bagni; }
    public void setBagni(Integer bagni) { this.bagni = bagni; }
    public String getRiscaldamento() { return riscaldamento; }
    public void setRiscaldamento(String riscaldamento) { this.riscaldamento = riscaldamento; }
    public Integer getPiano() { return piano; }
    public void setPiano(Integer piano) { this.piano = piano; }
    public Boolean getAscensore() { return ascensore; }
    public void setAscensore(Boolean ascensore) { this.ascensore = ascensore; }
    public Boolean getGarage() { return garage; }
    public void setGarage(Boolean garage) { this.garage = garage; }
    public Boolean getGiardino() { return giardino; }
    public void setGiardino(Boolean giardino) { this.giardino = giardino; }
    public Boolean getBalcone() { return balcone; }
    public void setBalcone(Boolean balcone) { this.balcone = balcone; }
    public Boolean getTerrazzo() { return terrazzo; }
    public void setTerrazzo(Boolean terrazzo) { this.terrazzo = terrazzo; }
    public Boolean getCantina() { return cantina; }
    public void setCantina(Boolean cantina) { this.cantina = cantina; }
    public Integer getPrezzo() { return prezzo; }
    public void setPrezzo(Integer prezzo) { this.prezzo = prezzo; }
    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }
    public String getDescrizioneImmobile() { return descrizioneImmobile; }
    public void setDescrizioneImmobile(String descrizioneImmobile) { this.descrizioneImmobile = descrizioneImmobile; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public ProprietarioRequest getProprietario() { return proprietario; }
    public void setProprietario(ProprietarioRequest proprietario) { this.proprietario = proprietario; }
}
