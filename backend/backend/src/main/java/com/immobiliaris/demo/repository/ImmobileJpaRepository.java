package com.immobiliaris.demo.repository;

import com.immobiliaris.demo.entity.Immobile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImmobileJpaRepository extends JpaRepository<Immobile, Integer> {
    
    /**
     * Conta gli immobili con uno specifico stato
     * Spring genera automaticamente: SELECT COUNT(*) WHERE statoImmobile.nome = ?
     */
    Long countByStatoImmobileNome(String nome);
    
    /**
     * Ottiene gli ultimi 10 immobili ordinati per data inserimento
     * Spring capisce automaticamente "OrderBy" e "Desc"
     */
    List<Immobile> findTop10ByOrderByDataInserimentoDesc();
    
    /**
     * Ottiene immobili con paginazione ordinati per data inserimento
     * Spring usa automaticamente il Pageable per paginazione e ordinamento
     */
    Page<Immobile> findAllByOrderByDataInserimentoDesc(Pageable pageable);
}
