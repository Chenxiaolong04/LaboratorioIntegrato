package com.immobiliaris.demo.repository;

import com.immobiliaris.demo.entity.StatoImmobile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StatoImmobileRepository extends JpaRepository<StatoImmobile, Integer> {
    Optional<StatoImmobile> findByNome(String nome);
}