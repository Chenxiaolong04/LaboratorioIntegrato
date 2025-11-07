package com.immobiliaris.demo.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
}
