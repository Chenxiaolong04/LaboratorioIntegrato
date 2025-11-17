package com.immobiliaris.demo.repository;

import com.immobiliaris.demo.entity.StatoValutazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatoValutazioneRepository extends JpaRepository<StatoValutazione, Integer> {
    Optional<StatoValutazione> findByNome(String nome);
}
