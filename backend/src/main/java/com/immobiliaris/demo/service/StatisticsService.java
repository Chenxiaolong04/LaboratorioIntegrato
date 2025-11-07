package com.immobiliaris.demo.service;

import com.immobiliaris.demo.repository.ContrattoRepository;
import com.immobiliaris.demo.repository.ImmobileRepository;
import com.immobiliaris.demo.repository.UtenteRepository;
import com.immobiliaris.demo.repository.ValutazioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class StatisticsService {

    @Autowired
    private ContrattoRepository contrattoRepository;

    @Autowired
    private ValutazioneRepository valutazioneRepository;

    @Autowired
    private ImmobileRepository immobileRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    /**
     * Ottiene le statistiche globali per l'amministratore
     * @return Map con le statistiche: contrattiConclusi, valutazioniInCorso, valutazioniConAI, immobiliInValutazione
     */
    public Map<String, Integer> getAdminStatistics() {
        Map<String, Integer> stats = new LinkedHashMap<>();
        
        // Contratti conclusi (stato 'chiuso')
        stats.put("contrattiConclusi", contrattoRepository.countByStato("chiuso"));
        
        // Valutazioni in corso (stato 'in_verifica')
        stats.put("valutazioniInCorso", valutazioneRepository.countByStato("in_verifica"));
        
        // Valutazioni con AI (stato 'solo_AI')
        stats.put("valutazioniConAI", valutazioneRepository.countByStato("solo_AI"));
        
        // Immobili in valutazione
        stats.put("immobiliInValutazione", immobileRepository.countByStato("in valutazione"));
        
        return stats;
    }

    /**
     * Ottiene le statistiche personali per un agente
     * @param emailAgente Email dell'agente autenticato
     * @return Map con le statistiche filtrate per l'agente
     */
    public Map<String, Integer> getAgentStatistics(String emailAgente) {
        Map<String, Integer> stats = new LinkedHashMap<>();
        
        // Ottieni ID dell'agente
        Integer idAgente = utenteRepository.findIdByEmail(emailAgente);
        
        if (idAgente == null) {
            // Ritorna statistiche vuote se l'agente non viene trovato
            stats.put("contrattiConclusi", 0);
            stats.put("valutazioniInCorso", 0);
            stats.put("valutazioniConAI", 0);
            stats.put("immobiliInValutazione", 0);
            return stats;
        }
        
        // Contratti conclusi dall'agente (stato 'chiuso')
        stats.put("contrattiConclusi", contrattoRepository.countByAgenteAndStato(idAgente, "chiuso"));
        
        // Valutazioni in corso dell'agente (stato 'in_verifica')
        stats.put("valutazioniInCorso", valutazioneRepository.countByAgenteAndStato(idAgente, "in_verifica"));
        
        // Valutazioni con AI assegnate all'agente (stato 'solo_AI')
        stats.put("valutazioniConAI", valutazioneRepository.countByAgenteAndStato(idAgente, "solo_AI"));
        
        // Immobili in valutazione (generale, non filtrato per agente)
        stats.put("immobiliInValutazione", immobileRepository.countByStato("in valutazione"));
        
        return stats;
    }
}
