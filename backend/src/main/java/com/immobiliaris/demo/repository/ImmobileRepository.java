package com.immobiliaris.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.immobiliaris.demo.model.Immobile;

public interface ImmobileRepository extends JpaRepository<Immobile,Integer> {
    
}
