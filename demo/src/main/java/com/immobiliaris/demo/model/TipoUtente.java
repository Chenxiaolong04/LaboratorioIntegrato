package com.immobiliaris.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Tipi_utente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoUtente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_tipo")
    private Integer idTipo;

    @Column(name = "Nome", nullable = false, unique = true, length = 50)
    private String nome;

    @Column(name = "Descrizione", length = 255)
    private String descrizione;
}
