package com.immobiliaris.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import com.immobiliaris.demo.entity.MacroareaUrbana;

@Repository
public interface MacroareaUrbanaRepository extends JpaRepository<MacroareaUrbana, Integer> {
    @Query("SELECT m FROM MacroareaUrbana m WHERE m.cap_da <= :cap AND m.cap_a >= :cap")
    Optional<MacroareaUrbana> findByCapRange(@Param("cap") String cap);
}