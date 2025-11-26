package com.immobiliaris.demo.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UtenteRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Trova l'ID utente dato l'email
     * @param email Email dell'utente
     * @return ID utente o null se non trovato
     */
    public Integer findIdByEmail(String email) {
        String sql = "SELECT Id_utente FROM Utenti WHERE Email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
