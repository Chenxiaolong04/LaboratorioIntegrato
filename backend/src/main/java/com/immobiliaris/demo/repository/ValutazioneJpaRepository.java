package com.immobiliaris.demo.repository;

import com.immobiliaris.demo.entity.Valutazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ValutazioneJpaRepository extends JpaRepository<Valutazione, Integer> {
    
    /**
     * Conta le valutazioni con uno specifico stato
     */
    @Query("SELECT COUNT(v) FROM Valutazione v WHERE v.statoValutazione.nome = :stato")
    Long countByStatoValutazioneNome(@Param("stato") String stato);
    
    /**
     * Conta le valutazioni di un agente specifico con uno specifico stato
     */
    @Query("SELECT COUNT(v) FROM Valutazione v WHERE v.statoValutazione.nome = :stato AND v.agente.id = :idAgente")
    Long countByStatoValutazioneNomeAndAgenteId(@Param("stato") String stato, @Param("idAgente") Integer idAgente);
    
    /**
     * Conta le valutazioni con uno specifico stato nell'ultimo mese
     */
    @Query("SELECT COUNT(v) FROM Valutazione v WHERE v.statoValutazione.nome = :stato AND v.dataValutazione >= :dataLimite")
    Long countByStatoValutazioneNomeAndDataValutazioneAfter(@Param("stato") String stato, @Param("dataLimite") LocalDate dataLimite);
}
