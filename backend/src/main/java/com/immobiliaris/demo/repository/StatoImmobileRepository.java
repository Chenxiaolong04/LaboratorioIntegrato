package com.immobiliaris.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.immobiliaris.demo.model.StatoImmobile;
import java.util.Optional;

public interface StatoImmobileRepository extends JpaRepository<StatoImmobile, Integer> {
    
    Optional<StatoImmobile> findByNome(String nome);
}
