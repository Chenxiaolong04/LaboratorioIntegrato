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
        System.out.println("LOG DTO: ImmobileRequest creato (costruttore vuoto)");
    }

    public Immobile toImmobile() {
        System.out.println("\nLOG DTO: Inizio toImmobile()");
        System.out.println("LOG DTO: via=" + this.via);
        System.out.println("LOG DTO: citta=" + this.citta);
        System.out.println("LOG DTO: tipologia=" + this.tipologia);
        System.out.println("LOG DTO: nome=" + this.nome);
        System.out.println("LOG DTO: cognome=" + this.cognome);
        System.out.println("LOG DTO: email=" + this.email);
        System.out.println("LOG DTO: telefono=" + this.telefono);
        
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
        
        // Gestisce sia "descrizione" che "descrizioneImmobile"
        String desc = this.descrizione != null ? this.descrizione : this.descrizioneImmobile;
        immobile.setDescrizione(desc);

        // Gestisce entrambi i formati: piatto e annidato
        String emailProprietario = null;
        String nomeProprietario = null;
        String cognomeProprietario = null;
        String telefonoProprietario = null;
        
        // Formato 1: campi piatti
        if (this.email != null) {
            System.out.println("LOG DTO: Usando formato PIATTO per proprietario");
            emailProprietario = this.email;
            nomeProprietario = this.nome;
            cognomeProprietario = this.cognome;
            telefonoProprietario = this.telefono;
        }
        // Formato 2: proprietario annidato
        else if (this.proprietario != null && this.proprietario.getEmail() != null) {
            System.out.println("LOG DTO: Usando formato ANNIDATO per proprietario");
            emailProprietario = this.proprietario.getEmail();
            nomeProprietario = this.proprietario.getNome();
            cognomeProprietario = this.proprietario.getCognome();
            telefonoProprietario = this.proprietario.getTelefono();
        }
        
        if (emailProprietario != null) {
            System.out.println("LOG DTO: Creando User temporaneo con email=" + emailProprietario);
            User user = new User();
            user.setEmail(emailProprietario);
            user.setNome(nomeProprietario);
            user.setCognome(cognomeProprietario);
            user.setTelefono(telefonoProprietario);
            immobile.setProprietario(user);
        } else {
            System.out.println("LOG DTO: WARN - Nessun proprietario trovato");
        }
        
        System.out.println("LOG DTO: Fine toImmobile()\n");

        return immobile;
    }

    // Getters e Setters
    public String getVia() { return via; }
    public void setVia(String via) { 
        System.out.println("LOG DTO SETTER: setVia(" + via + ")");
        this.via = via; 
    }
    public String getCitta() { return citta; }
    public void setCitta(String citta) { 
        System.out.println("LOG DTO SETTER: setCitta(" + citta + ")");
        this.citta = citta; 
    }
    public String getCap() { return cap; }
    public void setCap(String cap) { 
        System.out.println("LOG DTO SETTER: setCap(" + cap + ")");
        this.cap = cap; 
    }
    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }
    public String getTipologia() { return tipologia; }
    public void setTipologia(String tipologia) { 
        System.out.println("LOG DTO SETTER: setTipologia(" + tipologia + ")");
        this.tipologia = tipologia; 
    }
    public Integer getMetratura() { return metratura; }
    public void setMetratura(Integer metratura) { 
        System.out.println("LOG DTO SETTER: setMetratura(" + metratura + ")");
        this.metratura = metratura; 
    }
    public String getCondizioni() { return condizioni; }
    public void setCondizioni(String condizioni) { this.condizioni = condizioni; }
    public Integer getStanze() { return stanze; }
    public void setStanze(Integer stanze) { 
        System.out.println("LOG DTO SETTER: setStanze(" + stanze + ")");
        this.stanze = stanze; 
    }
    public Integer getBagni() { return bagni; }
    public void setBagni(Integer bagni) { 
        System.out.println("LOG DTO SETTER: setBagni(" + bagni + ")");
        this.bagni = bagni; 
    }
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
    public void setDescrizione(String descrizione) { 
        System.out.println("LOG DTO SETTER: setDescrizione(" + descrizione + ")");
        this.descrizione = descrizione; 
    }
    public String getDescrizioneImmobile() { return descrizioneImmobile; }
    public void setDescrizioneImmobile(String descrizioneImmobile) { this.descrizioneImmobile = descrizioneImmobile; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { 
        System.out.println("LOG DTO SETTER: setNome(" + nome + ")");
        this.nome = nome; 
    }
    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { 
        System.out.println("LOG DTO SETTER: setCognome(" + cognome + ")");
        this.cognome = cognome; 
    }
    public String getEmail() { return email; }
    public void setEmail(String email) { 
        System.out.println("LOG DTO SETTER: setEmail(" + email + ")");
        this.email = email; 
    }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { 
        System.out.println("LOG DTO SETTER: setTelefono(" + telefono + ")");
        this.telefono = telefono; 
    }
    
    public ProprietarioRequest getProprietario() { return proprietario; }
    public void setProprietario(ProprietarioRequest proprietario) { this.proprietario = proprietario; }
}
