package com.immobiliaris.demo.repository;

import com.immobiliaris.demo.entity.Valutazione;
import com.immobiliaris.demo.entity.Immobile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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
    Long countByStatoValutazioneNomeAndDataValutazioneAfter(String nome, LocalDate data);
    
    /**
     * Trova tutte le valutazioni con uno specifico stato
     */
    List<Valutazione> findByStatoValutazioneNome(String nome);
}
