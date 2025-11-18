package com.immobiliaris.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Stati_immobile")
public class StatoImmobile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_stato_immobile")
    private Integer idStatoImmobile;
    
    @Column(name = "Nome", length = 50)
    private String nome;
    
    @Column(name = "Descrizione", length = 255)
    private String descrizione;
}
