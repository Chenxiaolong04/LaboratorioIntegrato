package com.immobiliaris.demo.repository;

import com.immobiliaris.demo.entity.Contratto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ContrattoJpaRepository extends JpaRepository<Contratto, Integer> {
    
    /**
     * Conta i contratti con uno specifico stato
     */
    @Query("SELECT COUNT(c) FROM Contratto c WHERE c.statoContratto.nome = :stato")
    Long countByStatoContrattoNome(@Param("stato") String stato);
    
    /**
     * Conta i contratti di un agente specifico con uno specifico stato
     */
    @Query("SELECT COUNT(c) FROM Contratto c WHERE c.statoContratto.nome = :stato AND c.agente.id = :idAgente")
    Long countByStatoContrattoNomeAndAgenteId(@Param("stato") String stato, @Param("idAgente") Integer idAgente);
    
    /**
     * Conta i contratti conclusi nell'ultimo mese usando Data_inizio
     */
    @Query("SELECT COUNT(c) FROM Contratto c WHERE c.statoContratto.nome = :stato AND c.dataInizio >= :dataLimite")
    Long countByStatoContrattoNomeAndDataInizioAfter(@Param("stato") String stato, @Param("dataLimite") LocalDate dataLimite);
}
