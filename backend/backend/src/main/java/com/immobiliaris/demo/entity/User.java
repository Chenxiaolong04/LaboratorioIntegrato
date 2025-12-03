/**
 * Entity User - Rappresenta un utente del sistema IMMOBILIARIS.
 * 
 * Un utente può ricoprire diversi ruoli:
 * - ADMIN: Amministratore del sistema con accesso completo
 * - AGENT: Agente immobiliare che gestisce immobili e contratti
 * - CLIENT: Cliente/proprietario di immobili (creato automaticamente)
 * 
 * @author Sistema IMMOBILIARIS
 * @version 1.0
 * @since 2025-12-01
 */
package com.immobiliaris.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "Utenti")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * Identificativo univoco dell'utente (Primary Key).
     * Auto-generato dal database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_utente")
    private Long idUtente;

    /**
     * Codice Fiscale dell'utente.
     * Massimo 16 caratteri (standard italiano).
     */
    @Column(name = "CF", length = 16)
    private String cf;

    /**
     * Nome dell'utente.
     * Campo obbligatorio, massimo 50 caratteri.
     */
    @Column(name = "Nome", nullable = false, length = 50)
    private String nome;

    /**
     * Cognome dell'utente.
     * Campo obbligatorio, massimo 50 caratteri.
     */
    @Column(name = "Cognome", nullable = false, length = 50)
    private String cognome;

    /**
     * Email dell'utente.
     * Campo obbligatorio e univoco nel sistema.
     * Utilizzata per login e comunicazioni.
     */
    @Column(name = "Email", nullable = false, unique = true, length = 100)
    private String email;

    /**
     * Password dell'utente.
     * Memorizzata con hashing BCrypt per sicurezza.
     * Campo obbligatorio, massimo 255 caratteri.
     */
    @Column(name = "Password", nullable = false, length = 255)
    private String password;

    /**
     * Numero di telefono dell'utente.
     * Opzionale, massimo 20 caratteri.
     */
    @Column(name = "Telefono", length = 20)
    private String telefono;

    /**
     * Indirizzo stradale (via/corso/viale).
     * Opzionale, massimo 100 caratteri.
     */
    @Column(name = "Via", length = 100)
    private String via;

    /**
     * Città di residenza.
     * Opzionale, massimo 100 caratteri.
     */
    @Column(name = "Citta", length = 100)
    private String citta;

    /**
     * Codice Postale (CAP).
     * Opzionale, massimo 5 caratteri (standard italiano).
     */
    @Column(name = "CAP", length = 5)
    private String cap;

    /**
     * Tipo di contratto per agenti (es: "stage", "full-time").
     * Opzionale, massimo 100 caratteri.
     */
    @Column(name = "Contratto", length = 100)
    private String contratto;

    /**
     * Data e ora di registrazione nel sistema.
     * Impostata automaticamente al momento della creazione.
     * Non aggiornabile dopo l'inserimento.
     */
    @CreationTimestamp
    @Column(name = "Data_registrazione", updatable = false)
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dataRegistrazione;

    /**
     * Relazione ManyToOne con TipoUtente.
     * Determina il ruolo dell'utente nel sistema (ADMIN, AGENT, CLIENT).
     * Caricamento EAGER: il tipo utente è sempre caricato insieme all'utente.
     * 
     * @see TipoUtente
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Id_tipo", referencedColumnName = "Id_tipo")
    private TipoUtente tipoUtente;
}
