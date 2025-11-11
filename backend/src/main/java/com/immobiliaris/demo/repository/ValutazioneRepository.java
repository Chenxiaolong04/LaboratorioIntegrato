package com.immobiliaris.demo.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ValutazioneRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Conta le valutazioni con uno specifico stato
     * @param nomeStato Nome dello stato (es. "in_verifica", "solo_AI", "approvata", ecc.)
     * @return Numero di valutazioni con quello stato
     */
    public Integer countByStato(String nomeStato) {
        String sql = "SELECT COUNT(*) FROM Valutazioni v " +
                     "JOIN Stati_valutazione sv ON v.Id_stato_valutazione = sv.Id_stato_valutazione " +
                     "WHERE sv.Nome = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, nomeStato);
        return count != null ? count : 0;
    }

    /**
     * Conta le valutazioni di un agente specifico con uno specifico stato
     * @param idAgente ID dell'agente
     * @param nomeStato Nome dello stato
     * @return Numero di valutazioni
     */
    public Integer countByAgenteAndStato(Integer idAgente, String nomeStato) {
        String sql = "SELECT COUNT(*) FROM Valutazioni v " +
                     "JOIN Stati_valutazione sv ON v.Id_stato_valutazione = sv.Id_stato_valutazione " +
                     "WHERE sv.Nome = ? AND v.Id_agente = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, nomeStato, idAgente);
        return count != null ? count : 0;
    }

    /**
     * Conta le valutazioni con uno specifico stato nell'ultimo mese usando Data_valutazione
     * @param nomeStato Nome dello stato
     * @return Numero di valutazioni nell'ultimo mese
     */
    public Integer countByStatoLastMonth(String nomeStato) {
        String sql = "SELECT COUNT(*) FROM Valutazioni v " +
                     "JOIN Stati_valutazione sv ON v.Id_stato_valutazione = sv.Id_stato_valutazione " +
                     "WHERE sv.Nome = ? AND v.Data_valutazione >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH)";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, nomeStato);
        return count != null ? count : 0;
    }
}
