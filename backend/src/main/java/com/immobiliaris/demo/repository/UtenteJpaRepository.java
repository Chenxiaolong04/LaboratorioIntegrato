package com.immobiliaris.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.immobiliaris.demo.entity.Utente;

@Repository
public interface UtenteJpaRepository extends JpaRepository<Utente, Long> {
    Utente findByEmail(String email);
}