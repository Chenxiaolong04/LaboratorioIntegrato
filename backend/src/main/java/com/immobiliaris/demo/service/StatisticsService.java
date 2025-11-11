package com.immobiliaris.demo.service;

import com.immobiliaris.demo.entity.Immobile;
import com.immobiliaris.demo.entity.Valutazione;
import com.immobiliaris.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    @Autowired
    private ContrattoJpaRepository contrattoRepository;

    @Autowired
    private ValutazioneJpaRepository valutazioneRepository;

    @Autowired
    private ImmobileJpaRepository immobileRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    /**
     * Ottiene le statistiche complete per l'amministratore
     * Include: statistiche totali, statistiche mensili e ultimi 10 immobili
     * @return Map con tutte le statistiche
     */
    public Map<String, Object> getAdminDashboardData() {
        Map<String, Object> data = new LinkedHashMap<>();
        
        // Statistiche totali e mensili
        Map<String, Long> stats = new LinkedHashMap<>();
        
        // Data limite per statistiche mensili (ultimi 30 giorni)
        LocalDate dataLimite = LocalDate.now().minusMonths(1);
        
        // TOTALI
        stats.put("contrattiConclusi", contrattoRepository.countByStatoContrattoNome("chiuso"));
        stats.put("valutazioniInCorso", valutazioneRepository.countByStatoValutazioneNome("in_verifica"));
        stats.put("valutazioniConAI", valutazioneRepository.countByStatoValutazioneNome("solo_AI"));
        
        // MENSILI (ultimi 30 giorni)
        stats.put("contrattiConclusiMensili", contrattoRepository.countByStatoContrattoNomeAndDataInizioAfter("chiuso", dataLimite));
        stats.put("valutazioniInCorsoMensili", valutazioneRepository.countByStatoValutazioneNomeAndDataValutazioneAfter("in_verifica", dataLimite));
        stats.put("valutazioniConAIMensili", valutazioneRepository.countByStatoValutazioneNomeAndDataValutazioneAfter("solo_AI", dataLimite));
        
        data.put("statistics", stats);
        
        // Ultimi 10 immobili aggiunti
        Pageable top10 = PageRequest.of(0, 10);
        List<Immobile> immobili = immobileRepository.findTop10ByOrderByDataInserimentoDesc(top10);
        
        // Trasforma in Map per JSON
        List<Map<String, Object>> ultimi10Immobili = immobili.stream().map(i -> {
            Map<String, Object> immobileMap = new LinkedHashMap<>();
            immobileMap.put("tipo", i.getTipologia());
            immobileMap.put("nomeProprietario", i.getProprietario().getNome() + " " + i.getProprietario().getCognome());
            immobileMap.put("dataInserimento", i.getDataInserimento());
            
            // Trova agente dalla valutazione
            String agenteNome = findAgenteForImmobile(i.getId());
            immobileMap.put("agenteAssegnato", agenteNome);
            
            return immobileMap;
        }).collect(Collectors.toList());
        
        data.put("ultimi10Immobili", ultimi10Immobili);
        
        return data;
    }
    
    /**
     * Trova l'agente assegnato per un immobile dalla tabella Valutazioni
     */
    private String findAgenteForImmobile(Integer idImmobile) {
        List<Valutazione> valutazioni = valutazioneRepository.findAll();
        return valutazioni.stream()
            .filter(v -> v.getImmobile() != null && v.getImmobile().getId().equals(idImmobile))
            .filter(v -> v.getAgente() != null)
            .findFirst()
            .map(v -> v.getAgente().getNome() + " " + v.getAgente().getCognome())
            .orElse(null);
    }

    /**
     * Ottiene le statistiche personali per un agente
     * @param emailAgente Email dell'agente autenticato
     * @return Map con le statistiche filtrate per l'agente
     */
    public Map<String, Long> getAgentStatistics(String emailAgente) {
        Map<String, Long> stats = new LinkedHashMap<>();
        
        // Ottieni ID dell'agente
        Integer idAgente = utenteRepository.findIdByEmail(emailAgente);
        
        if (idAgente == null) {
            // Ritorna statistiche vuote se l'agente non viene trovato
            stats.put("contrattiConclusi", 0L);
            stats.put("valutazioniInCorso", 0L);
            stats.put("valutazioniConAI", 0L);
            return stats;
        }
        
        // Contratti conclusi dall'agente (stato 'chiuso')
        stats.put("contrattiConclusi", contrattoRepository.countByStatoContrattoNomeAndAgenteId("chiuso", idAgente));
        
        // Valutazioni in corso dell'agente (stato 'in_verifica')
        stats.put("valutazioniInCorso", valutazioneRepository.countByStatoValutazioneNomeAndAgenteId("in_verifica", idAgente));
        
        // Valutazioni con AI assegnate all'agente (stato 'solo_AI')
        stats.put("valutazioniConAI", valutazioneRepository.countByStatoValutazioneNomeAndAgenteId("solo_AI", idAgente));
        
        return stats;
    }

    /**
     * Ottiene una pagina di immobili con paginazione
     * @param page Numero pagina (0-based)
     * @param size Numero elementi per pagina
     * @return Map con lista immobili e informazioni paginazione
     */
    public Map<String, Object> getImmobiliPaginated(int page, int size) {
        Map<String, Object> result = new LinkedHashMap<>();
        
        // Ottieni immobili della pagina con Spring Data JPA
        Pageable pageable = PageRequest.of(page, size);
        Page<Immobile> immobiliPage = immobileRepository.findAllOrderByDataInserimentoDesc(pageable);
        
        // Trasforma in Map per JSON
        List<Map<String, Object>> immobili = immobiliPage.getContent().stream().map(i -> {
            Map<String, Object> immobileMap = new LinkedHashMap<>();
            immobileMap.put("tipo", i.getTipologia());
            immobileMap.put("nomeProprietario", i.getProprietario().getNome() + " " + i.getProprietario().getCognome());
            immobileMap.put("dataInserimento", i.getDataInserimento());
            
            // Trova agente dalla valutazione
            String agenteNome = findAgenteForImmobile(i.getId());
            immobileMap.put("agenteAssegnato", agenteNome);
            
            return immobileMap;
        }).collect(Collectors.toList());
        
        result.put("immobili", immobili);
        result.put("currentPage", page);
        result.put("pageSize", size);
        result.put("totalImmobili", immobiliPage.getTotalElements());
        result.put("totalPages", immobiliPage.getTotalPages());
        result.put("hasNext", immobiliPage.hasNext());
        result.put("hasPrevious", immobiliPage.hasPrevious());
        
        return result;
    }
}
