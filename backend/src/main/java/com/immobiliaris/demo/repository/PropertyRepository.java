package com.immobiliaris.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.immobiliaris.demo.model.Property;

public interface PropertyRepository extends JpaRepository<Property,Integer> {
    
}
