package com.immobiliaris.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Stati_contratto")
public class StatoContratto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_stato_contratto")
    private Integer id;
    
    @Column(name = "Nome")
    private String nome;
    
    @Column(name = "Descrizione")
    private String descrizione;
    
    // Getters e Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getDescrizione() {
        return descrizione;
    }
    
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
}
