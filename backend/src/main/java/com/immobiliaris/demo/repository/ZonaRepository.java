package com.immobiliaris.demo.repository;

import com.immobiliaris.demo.entity.Zona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ZonaRepository extends JpaRepository<Zona, Integer> {
    Optional<Zona> findByCap(String cap);
}
