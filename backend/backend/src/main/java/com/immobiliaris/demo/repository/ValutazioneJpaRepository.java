package com.immobiliaris.demo.repository;

import com.immobiliaris.demo.entity.Valutazione;
import com.immobiliaris.demo.entity.Immobile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ValutazioneJpaRepository extends JpaRepository<Valutazione, Integer> {
    List<Valutazione> findByImmobile(Immobile immobile);
    
    /**
     * Conta le valutazioni con uno specifico stato
     * Spring genera automaticamente la query
     */
    Long countByStatoValutazioneNome(String nome);
    
    /**
     * Conta le valutazioni di un agente specifico con uno specifico stato
     * Spring genera automaticamente: WHERE statoValutazione.nome = ? AND agente.idUtente = ?
     */
    Long countByStatoValutazioneNomeAndAgenteIdUtente(String nome, Integer agenteIdUtente);
    
    /**
     * Conta le valutazioni con uno specifico stato nell'ultimo mese
     * Spring genera automaticamente: WHERE nome = ? AND dataValutazione >= ?
     */
    Long countByStatoValutazioneNomeAndDataValutazioneAfter(String nome, LocalDateTime data);
    
    /**
     * Trova tutte le valutazioni con uno specifico stato
     */
    List<Valutazione> findByStatoValutazioneNome(String nome);
    
    /**
     * Trova tutte le valutazioni per un immobile specifico, ordinate per data (dalla più recente)
     * Utilizzato per recuperare lo stato della valutazione e l'agente assegnato nella dashboard
     * 
     * Spring genera automaticamente la query:
     * SELECT * FROM Valutazioni WHERE Id_immobile = ? ORDER BY Data_valutazione DESC
     * 
     * @param immobileId ID dell'immobile
     * @return Lista di valutazioni ordinate per data discendente (la prima è la più recente)
     *         Include le relazioni JPA: StatoValutazione e User (agente)
     */
    List<Valutazione> findByImmobileIdOrderByDataValutazioneDesc(Integer immobileId);
}
