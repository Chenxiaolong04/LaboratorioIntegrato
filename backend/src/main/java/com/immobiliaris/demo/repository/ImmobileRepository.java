package com.immobiliaris.demo.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ImmobileRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Conta gli immobili con uno specifico stato
     * @param nomeStato Nome dello stato (es. "in valutazione", "disponibile", ecc.)
     * @return Numero di immobili con quello stato
     */
    public Integer countByStato(String nomeStato) {
        String sql = "SELECT COUNT(*) FROM Immobili i " +
                     "JOIN Stati_immobile si ON i.Id_stato_immobile = si.Id_stato_immobile " +
                     "WHERE si.Nome = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, nomeStato);
        return count != null ? count : 0;
    }

    /**
     * Ottiene gli ultimi 10 immobili aggiunti con dettagli
     * Include l'agente che si occupa dell'immobile dalla tabella Valutazioni
     * @return Lista degli ultimi 10 immobili con tipo, proprietario, data e agente assegnato
     */
    public List<Map<String, Object>> getLatest10Immobili() {
        String sql = "SELECT " +
                     "i.Tipologia as tipo, " +
                     "CONCAT(u.Nome, ' ', u.Cognome) as nomeProprietario, " +
                     "i.Data_registrazione as dataRegistrazione, " +
                     "CONCAT(u_ag.Nome, ' ', u_ag.Cognome) as agenteAssegnato " +
                     "FROM Immobili i " +
                     "JOIN Utenti u ON i.Id_utente = u.Id_utente " +
                     "LEFT JOIN Valutazioni v ON i.Id_immobile = v.Id_immobile " +
                     "LEFT JOIN Utenti u_ag ON v.Id_agente = u_ag.Id_utente " +
                     "ORDER BY i.Data_registrazione DESC " +
                     "LIMIT 10";
        
        return jdbcTemplate.queryForList(sql);
    }

    /**
     * Ottiene una pagina di immobili recenti con paginazione
     * Include l'agente che si occupa dell'immobile dalla tabella Valutazioni
     * @param page Numero pagina (0 = prima pagina)
     * @param size Numero elementi per pagina (es. 10)
     * @return Lista immobili della pagina richiesta
     */
    public List<Map<String, Object>> getImmobiliPaginated(int page, int size) {
        int offset = page * size;
        
        String sql = "SELECT " +
                     "i.Tipologia as tipo, " +
                     "CONCAT(u.Nome, ' ', u.Cognome) as nomeProprietario, " +
                     "i.Data_registrazione as dataRegistrazione, " +
                     "CONCAT(u_ag.Nome, ' ', u_ag.Cognome) as agenteAssegnato " +
                     "FROM Immobili i " +
                     "JOIN Utenti u ON i.Id_utente = u.Id_utente " +
                     "LEFT JOIN Valutazioni v ON i.Id_immobile = v.Id_immobile " +
                     "LEFT JOIN Utenti u_ag ON v.Id_agente = u_ag.Id_utente " +
                     "ORDER BY i.Data_registrazione DESC " +
                     "LIMIT ? OFFSET ?";
        
        return jdbcTemplate.queryForList(sql, size, offset);
    }

    /**
     * Conta il numero totale di immobili nel database
     * @return Numero totale immobili
     */
    public Integer countAllImmobili() {
        String sql = "SELECT COUNT(*) FROM Immobili";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}
