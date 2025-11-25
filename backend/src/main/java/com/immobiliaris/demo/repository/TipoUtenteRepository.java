package com.immobiliaris.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.immobiliaris.demo.entity.TipoUtente;

@Repository
public interface TipoUtenteRepository extends JpaRepository<TipoUtente, Integer> {
}
