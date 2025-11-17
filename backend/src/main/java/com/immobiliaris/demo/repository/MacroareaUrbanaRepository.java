package com.immobiliaris.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.immobiliaris.demo.entity.MacroareaUrbana;

@Repository
public interface MacroareaUrbanaRepository extends JpaRepository<MacroareaUrbana, Integer> {
}