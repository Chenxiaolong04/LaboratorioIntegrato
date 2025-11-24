package com.immobiliaris.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Utenti")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_utente")
    private Long idUtente;

    @Column(name = "CF", length = 16)
    private String cf;

    @Column(name = "Nome", nullable = false, length = 50)
    private String nome;

    @Column(name = "Cognome", nullable = false, length = 50)
    private String cognome;

    @Column(name = "Email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "Password", nullable = false, length = 255)
    private String password;

    @Column(name = "Telefono", length = 20)
    private String telefono;

    @Column(name = "Via", length = 100)
    private String via;

    @Column(name = "Citta", length = 100)
    private String citta;

    @Column(name = "CAP", length = 5)
    private String cap;

    @Column(name = "Data_registrazione")
    private java.sql.Date dataRegistrazione;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Id_tipo", referencedColumnName = "Id_tipo")
    private TipoUtente tipoUtente;
}
