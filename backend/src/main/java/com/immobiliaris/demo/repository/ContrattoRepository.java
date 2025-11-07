package com.immobiliaris.demo.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ContrattoRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Conta i contratti con uno specifico stato
     * @param nomeStato Nome dello stato (es. "chiuso", "in corso", ecc.)
     * @return Numero di contratti con quello stato
     */
    public Integer countByStato(String nomeStato) {
        String sql = "SELECT COUNT(*) FROM Contratti c " +
                     "JOIN Stati_contratto sc ON c.Id_stato_contratto = sc.Id_stato_contratto " +
                     "WHERE sc.Nome = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, nomeStato);
        return count != null ? count : 0;
    }

    /**
     * Conta i contratti di un agente specifico con uno specifico stato
     * @param idAgente ID dell'agente
     * @param nomeStato Nome dello stato
     * @return Numero di contratti
     */
    public Integer countByAgenteAndStato(Integer idAgente, String nomeStato) {
        String sql = "SELECT COUNT(*) FROM Contratti c " +
                     "JOIN Stati_contratto sc ON c.Id_stato_contratto = sc.Id_stato_contratto " +
                     "WHERE sc.Nome = ? AND c.Id_agente = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, nomeStato, idAgente);
        return count != null ? count : 0;
    }
}
