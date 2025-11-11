package com.immobiliaris.demo.repository;

import com.immobiliaris.demo.entity.Immobile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImmobileJpaRepository extends JpaRepository<Immobile, Integer> {
    
    /**
     * Conta gli immobili con uno specifico stato
     */
    @Query("SELECT COUNT(i) FROM Immobile i WHERE i.statoImmobile.nome = :stato")
    Long countByStatoImmobileNome(@Param("stato") String stato);
    
    /**
     * Ottiene gli ultimi 10 immobili ordinati per data inserimento
     */
    @Query("SELECT i FROM Immobile i ORDER BY i.dataInserimento DESC")
    List<Immobile> findTop10ByOrderByDataInserimentoDesc(Pageable pageable);
    
    /**
     * Ottiene immobili con paginazione ordinati per data inserimento
     */
    @Query("SELECT i FROM Immobile i ORDER BY i.dataInserimento DESC")
    Page<Immobile> findAllOrderByDataInserimentoDesc(Pageable pageable);
}
