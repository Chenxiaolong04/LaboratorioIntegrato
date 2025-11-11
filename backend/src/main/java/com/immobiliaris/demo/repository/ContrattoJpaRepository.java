package com.immobiliaris.demo.repository;

import com.immobiliaris.demo.entity.Contratto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ContrattoJpaRepository extends JpaRepository<Contratto, Integer> {
    
    /**
     * Conta i contratti con uno specifico stato
     * Spring genera automaticamente: SELECT COUNT(*) WHERE statoContratto.nome = ?
     */
    Long countByStatoContrattoNome(String nome);
    
    /**
     * Conta i contratti di un agente specifico con uno specifico stato
     * Spring genera automaticamente la query con AND
     */
    Long countByStatoContrattoNomeAndAgenteIdUtente(String nome, Integer agenteIdUtente);
    
    /**
     * Conta i contratti conclusi nell'ultimo mese usando Data_inizio
     * Spring genera automaticamente: WHERE nome = ? AND dataInizio >= ?
     */
    Long countByStatoContrattoNomeAndDataInizioAfter(String nome, LocalDate data);
}
