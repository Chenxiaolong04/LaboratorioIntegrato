package com.immobiliaris.demo.service;

import com.immobiliaris.demo.entity.Immobile;
import com.immobiliaris.demo.entity.StatoValutazione;
import com.immobiliaris.demo.entity.Valutazione;
import com.immobiliaris.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import com.immobiliaris.demo.entity.Contratto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class StatisticsService {
    private static final Logger logger = LoggerFactory.getLogger(StatisticsService.class);

    @Autowired
    private ContrattoJpaRepository contrattoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValutazioneJpaRepository valutazioneRepository;

    @Autowired
    private ImmobileJpaRepository immobileRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private ValutazioneJpaRepository valutazioneJpaRepository;

    @Autowired
    private ImmobileJpaRepository immobileJpaRepository;

    @Autowired
    private StatoValutazioneRepository statoValutazioneRepository;

    /**
     * Ottiene le statistiche complete per l'amministratore
     * Include: statistiche totali, statistiche mensili e ultimi 10 immobili
     * @return Map con tutte le statistiche
     */
    public Map<String, Object> getAdminDashboardData() {
        Map<String, Object> data = new LinkedHashMap<>();



        // Statistiche totali e mensili
        Map<String, Long> stats = new LinkedHashMap<>();

        // Data limite per statistiche mensili (ultimi 30 giorni) e settimanali (ultimi 7 giorni)
        LocalDateTime dataLimiteMensile = LocalDateTime.now().minusMonths(1);
        LocalDateTime dataLimiteSettimanale = LocalDateTime.now().minusWeeks(1);

        // TOTALE IMMOBILI
        Long totaleImmobili = immobileRepository.count();
        stats.put("totaleImmobili", totaleImmobili);

        // TOTALE IMMOBILI CON VALUTAZIONE IN VERIFICA
        Long immobiliInVerifica = valutazioneRepository.countByStatoValutazioneNome("in_verifica");
        stats.put("immobiliInVerifica", immobiliInVerifica);

        // CONTRATTI CONCLUSI
        Long contrattiConclusi = contrattoRepository.countByStatoContrattoNome("chiuso");
        stats.put("contrattiConclusi", contrattiConclusi);

        // FATTURATO TOTALE DEI CONTRATTI CONCLUSI (somma prezzoUmano dalle valutazioni)
        List<Contratto> contrattiChiusi = contrattoRepository.findByStatoContrattoNome("chiuso");
        long fatturatoTotale = contrattiChiusi.stream()
            .map(c -> {
                if (c.getValutazione() != null && c.getValutazione().getPrezzoUmano() != null) {
                    return (long) c.getValutazione().getPrezzoUmano();
                }
                return 0L;
            })
            .reduce(0L, Long::sum);
        stats.put("fatturatoTotale", fatturatoTotale);

        // REGISTRAZIONI IMMOBILI NELL'ULTIMO MESE (ultimi 30 giorni)
        Long immobiliRegistratiMensili = immobileRepository.countByDataRegistrazioneAfter(dataLimiteMensile);
        stats.put("immobiliRegistratiMensili", immobiliRegistratiMensili);

        // REGISTRAZIONI IMMOBILI NELL'ULTIMA SETTIMANA (ultimi 7 giorni)
        Long immobiliRegistratiSettimanali = immobileRepository.countByDataRegistrazioneAfter(dataLimiteSettimanale);
        stats.put("immobiliRegistratiSettimanali", immobiliRegistratiSettimanali);

        // TOTALE AGENTI
        List<com.immobiliaris.demo.entity.User> tuttiAgenti = userRepository.findAll().stream()
            .filter(u -> u.getTipoUtente() != null && u.getTipoUtente().getIdTipo() == 2)
            .collect(Collectors.toList());
        
        // Separa agenti normali da agenti stage
        long agentiNormali = tuttiAgenti.stream()
            .filter(u -> u.getContratto() == null || !u.getContratto().equalsIgnoreCase("stage"))
            .count();
        
        long agentiStage = tuttiAgenti.stream()
            .filter(u -> u.getContratto() != null && u.getContratto().equalsIgnoreCase("stage"))
            .count();
        
        stats.put("totaleAgenti", agentiNormali);
        stats.put("agentiStage", agentiStage);

        data.put("statistics", stats);

        // Ultimi 10 immobili aggiunti (Spring trova automaticamente i primi 10)
        List<Immobile> immobili = immobileRepository.findTop10ByOrderByDataRegistrazioneDesc();

        // Trasforma in Map per JSON
        List<Map<String, Object>> ultimi10Immobili = immobili.stream().map(i -> {
            Map<String, Object> immobileMap = new LinkedHashMap<>();
            immobileMap.put("tipo", i.getTipologia());
            immobileMap.put("nomeProprietario", i.getProprietario().getNome() + " " + i.getProprietario().getCognome());

            String agenteNome = findAgenteForImmobile(i.getId());
            immobileMap.put("agenteAssegnato", agenteNome);

            return immobileMap;
        }).collect(Collectors.toList());

        data.put("ultimi10Immobili", ultimi10Immobili);

            // Statistiche immobili per tipo (Appartamento, Attico, Villa, Loft)
            List<Immobile> tuttiImmobili = immobileRepository.findAll();
            Map<String, Long> immobiliPerTipo = tuttiImmobili.stream()
                .filter(i -> i.getTipologia() != null)
                .filter(i -> {
                    String tipo = i.getTipologia().toLowerCase();
                    return tipo.equals("appartamento") || tipo.equals("attico") || tipo.equals("villa") || tipo.equals("loft");
                })
                .collect(Collectors.groupingBy(i -> i.getTipologia(), Collectors.counting()));
            data.put("immobiliPerTipo", immobiliPerTipo);

        // Aggiungi contratti per mese (ultimi 6 mesi)
        data.putAll(getContrattiPerMese());

        // Aggiungi top 3 agenti
        data.putAll(getTop3Agenti());

        // Aggiungi tutti gli agenti con statistiche
        data.putAll(getTuttiAgentiConStatistiche());

        // Aggiungi tempi medi di processo e performance
        data.putAll(getTempiProcessoEPerformance());

        return data;
    }

    /**
     * Contratti stipulati per mese negli ultimi 6 mesi
     * @return Map con mese e relativi dati (numero contratti, totale prezzo immobili)
     */
    public Map<String, Object> getContrattiPerMese() {
        Map<String, Object> result = new LinkedHashMap<>();
        List<Map<String, Object>> contrattiMensili = new java.util.ArrayList<>();

        for (int i = 5; i >= 0; i--) {
            LocalDateTime inizio = LocalDateTime.now().minusMonths(i).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime fine = inizio.plusMonths(1).minusDays(1).withHour(23).withMinute(59).withSecond(59).withNano(999999999);

                List<Contratto> contrattiMese = contrattoRepository.findByStatoContrattoNome("chiuso")
                    .stream()
                    .filter(c -> c.getDataInizio() != null && 
                        !c.getDataInizio().isBefore(inizio) && 
                        !c.getDataInizio().isAfter(fine))
                    .collect(Collectors.toList());

            // Sostituisco unboxing possibly null value
            Integer totalePrezzoMese = contrattiMese.stream()
                .map(c -> {
                    if (c.getImmobile() != null && c.getImmobile().getPrezzo() != null) {
                        return c.getImmobile().getPrezzo();
                    } else {
                        return 0;
                    }
                })
                .reduce(0, Integer::sum);

            Map<String, Object> mese = new LinkedHashMap<>();
            mese.put("mese", String.format("%02d/%d", inizio.getMonthValue(), inizio.getYear()));
            mese.put("numeroContratti", (long) contrattiMese.size());
            mese.put("totalePrezzoImmobili", totalePrezzoMese);

            contrattiMensili.add(mese);
        }

        result.put("contrattiPerMese", contrattiMensili);
        return result;
    }

    /**
     * Top 3 agenti con più contratti conclusi nel mese corrente
     * @return Map con agenti, numero contratti e prezzo totale immobili
     */
    public Map<String, Object> getTop3Agenti() {
        Map<String, Object> result = new LinkedHashMap<>();

        LocalDateTime oggi = LocalDateTime.now();
        LocalDateTime inizio = oggi.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime fine = inizio.plusMonths(1).minusDays(1).withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        System.out.println("DEBUG: Data inizio mese: " + inizio);
        System.out.println("DEBUG: Data fine mese: " + fine);


        List<Contratto> tuttiContratti = contrattoRepository.findByStatoContrattoNome("chiuso");
        logger.info("DEBUG: Contratti chiusi trovati: {}", tuttiContratti.size());
        for (Contratto c : tuttiContratti) {
            logger.info("DEBUG: Contratto ID: {}, data_inizio: {}, stato: {}, agente: {}", c.getId(), c.getDataInizio(), (c.getStatoContratto() != null ? c.getStatoContratto().getNome() : "NULL"), (c.getAgente() != null ? c.getAgente().getIdUtente() : "NULL"));
        }

        List<Contratto> contrattiConclusiMese = tuttiContratti.stream()
                .filter(c -> {
                    boolean hasData = c.getDataInizio() != null;
                    boolean isInRange = hasData && !c.getDataInizio().isBefore(inizio) && !c.getDataInizio().isAfter(fine);
                    if (hasData && isInRange) {
                        System.out.println("DEBUG MATCH: Contratto " + c.getId() + " del " + c.getDataInizio());
                    }
                    return isInRange;
                })
                .collect(Collectors.toList());

        System.out.println("DEBUG: Contratti conclusi nel mese corrente (filtrati): " + contrattiConclusiMese.size());

        Map<?, List<Contratto>> agenteContrattiMapRaw = contrattiConclusiMese.stream()
                .filter(c -> c.getAgente() != null && c.getAgente().getIdUtente() != null)
                .collect(Collectors.groupingBy(c -> c.getAgente().getIdUtente()));

        System.out.println("Agenti trovati nel mese: " + agenteContrattiMapRaw.size());

        List<Map<String, Object>> top3 = agenteContrattiMapRaw.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue().size(), a.getValue().size()))
                .limit(3)
                .map(entry -> {
                    Map<String, Object> agente = new LinkedHashMap<>();
                    try {
                        Object keyObj = entry.getKey();
                        System.out.println("DEBUG: Key object type: " + keyObj.getClass().getName() + ", value: " + keyObj);
                        
                        // Sostituisco instanceof con pattern matching
                        Integer agenteId;
                        if (keyObj instanceof Long l) {
                            agenteId = l.intValue();
                        } else if (keyObj instanceof Integer i) {
                            agenteId = i;
                        } else {
                            throw new ClassCastException("Tipo di key non supportato: " + keyObj.getClass().getName());
                        }
                        System.out.println("DEBUG: Casting successful, agenteId = " + agenteId);
                        System.out.println("DEBUG: Cercando agente con ID: " + agenteId);
                        
                        com.immobiliaris.demo.entity.User user = userRepository.findById((long) agenteId)
                                .orElse(null);
                        
                        System.out.println("DEBUG: User trovato: " + (user != null ? "SÌ" : "NO"));
                        
                        if (user != null) {
                            System.out.println("DEBUG: Agente trovato: " + user.getNome() + " " + user.getCognome() + 
                                    " con " + entry.getValue().size() + " contratti nel mese");
                            
                            Integer fatturato = entry.getValue().stream()
                                .map(c -> {
                                    if (c.getValutazione() != null && c.getValutazione().getPrezzoUmano() != null) {
                                        return c.getValutazione().getPrezzoUmano();
                                    } else {
                                        return 0;
                                    }
                                })
                                .reduce(0, Integer::sum);
                            
                            agente.put("nomeAgente", user.getNome() + " " + user.getCognome());
                            agente.put("numeroContratti", (long) entry.getValue().size());
                            agente.put("fatturato", fatturato);
                            
                            System.out.println("DEBUG: Agente aggiunto a top3. Map size: " + agente.size());
                        } else {
                            System.out.println("DEBUG: Agente non trovato con ID: " + agenteId);
                        }
                    } catch (ClassCastException e) {
                        System.err.println("DEBUG: ClassCastException nel casting della key: " + e.getMessage());
                        logger.error("DEBUG: ClassCastException nel casting della key: {}", e.getMessage(), e);
                    } catch (Exception e) {
                        System.err.println("DEBUG: Errore nel recupero agente: " + e.getMessage());
                        logger.error("DEBUG: Errore nel recupero agente: {}", e.getMessage(), e);
                    }
                    return agente;
                })
                .filter(m -> !m.isEmpty())
                .collect(Collectors.toList());

        System.out.println("Top 3 agenti del mese trovati: " + top3.size());
        result.put("top3Agenti", top3);
        return result;
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
        stats.put("contrattiConclusi", contrattoRepository.countByStatoContrattoNomeAndAgenteIdUtente("chiuso", idAgente));

        // Valutazioni in corso dell'agente (stato 'in_verifica')
        stats.put("valutazioniInCorso", valutazioneRepository.countByStatoValutazioneNomeAndAgenteIdUtente("in_verifica", idAgente));

        // Totale valutazioni con AI nel sistema (tutte, anche senza agente assegnato)
        stats.put("valutazioniConAI", valutazioneRepository.countByStatoValutazioneNome("solo_AI"));

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

        // Ottieni immobili della pagina con Spring Data JPA ordinati per ID decrescente
        Pageable pageable = PageRequest.of(page, size);
        Page<Immobile> immobiliPage = immobileRepository.findAllByOrderByIdDesc(pageable);

        // Trasforma in Map per JSON
        List<Map<String, Object>> immobili = immobiliPage.getContent().stream().map(i -> {
            Map<String, Object> immobileMap = new LinkedHashMap<>();
            immobileMap.put("tipo", i.getTipologia());
            immobileMap.put("nomeProprietario", i.getProprietario().getNome() + " " + i.getProprietario().getCognome());

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

    /**
     * Carica immobili usando offset e limit (uso per "Load more" nel frontend)
     * Restituisce una mappa con la lista di immobili, nextOffset e hasMore
     */
    public Map<String, Object> getImmobiliLoadMore(int offset, int limit) {
        List<Map<String, Object>> items = new java.util.ArrayList<>();

        if (limit <= 0) limit = 10;
        if (offset < 0) offset = 0;

        int page = offset / limit;
        int indexInPage = offset % limit;
        long total = immobileRepository.count();

        while (items.size() < limit) {
            Page<Immobile> p = immobileRepository.findAllByOrderByIdDesc(PageRequest.of(page, limit));
            List<Immobile> content = p.getContent();
            if (content.isEmpty()) break;

            for (int i = indexInPage; i < content.size() && items.size() < limit; i++) {
                Immobile iObj = content.get(i);
                Map<String, Object> immobileMap = new LinkedHashMap<>();
                immobileMap.put("tipo", iObj.getTipologia());
                immobileMap.put("nomeProprietario", iObj.getProprietario() != null ? iObj.getProprietario().getNome() + " " + iObj.getProprietario().getCognome() : null);

                List<Valutazione> valutazioni = valutazioneJpaRepository.findByImmobileIdOrderByDataValutazioneDesc(iObj.getId());
                String statoValutazione = null;
                String agenteNome = null;
                
                if (!valutazioni.isEmpty()) {
                    Valutazione valutazione = valutazioni.get(0); // Prende la più recente
                    if (valutazione.getStatoValutazione() != null) {
                        statoValutazione = valutazione.getStatoValutazione().getNome();
                    }
                    
                    // Mostra agente solo se stato valutazione è "in_verifica" o "approvata" (non per "solo_AI")
                    if (statoValutazione != null && ("in_verifica".equalsIgnoreCase(statoValutazione) || "approvata".equalsIgnoreCase(statoValutazione))) {
                        if (valutazione.getAgente() != null) {
                            agenteNome = valutazione.getAgente().getNome() + " " + valutazione.getAgente().getCognome();
                        }
                    }
                }
                
                immobileMap.put("statoValutazione", statoValutazione);
                immobileMap.put("agenteAssegnato", agenteNome);

                items.add(immobileMap);
            }

            if ((page + 1) * (long) limit >= total) break; // non ci sono altre pagine
            page++;
            indexInPage = 0; // dopo la prima iterazione prendi sempre dall'inizio della pagina
        }

        int nextOffset = offset + items.size();
        boolean hasMore = nextOffset < total;

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("immobili", items);
        result.put("nextOffset", nextOffset);
        result.put("hasMore", hasMore);
        result.put("pageSize", limit);

        return result;
    }

    /**
     * Restituisce contratti chiusi con offset/limit per load-more (come dashboard)
     * Mostra i dettagli dell'immobile associato (tipo, proprietario, data, agente)
     */
    public Map<String, Object> getContrattiChiusiLoadMore(int offset, int limit) {
        Map<String, Object> result = new LinkedHashMap<>();

        // Ottieni tutti i contratti chiusi (nota: non usiamo Page perché findByStatoContrattoNome non è pageable)
        List<Contratto> tuttiContratti = contrattoRepository.findByStatoContrattoNome("chiuso");

        // Applica offset e limit manualmente
        int totalContratti = tuttiContratti.size();
        List<Contratto> contractiBatch = tuttiContratti.stream()
            .skip(offset)
            .limit(limit)
            .collect(Collectors.toList());

        // Trasforma in Map mostrando i dettagli dell'immobile
        List<Map<String, Object>> contrattiData = contractiBatch.stream().map(c -> {
            Map<String, Object> m = new LinkedHashMap<>();

            // Dati contratto
            m.put("numeroContratto", c.getNumeroContratto());
            m.put("dataInvio", c.getDataInvio());
            m.put("dataRicezione", c.getDataRicezione());
            m.put("dataInizio", c.getDataInizio());
            m.put("dataFine", c.getDataFine());

            // Valutazione umana (se disponibile)
            if (c.getValutazione() != null) {
                m.put("valutazioneUmana", c.getValutazione().getPrezzoUmano());
            } else {
                m.put("valutazioneUmana", null);
            }

            // Dati immobile (come nel dashboard)
            if (c.getImmobile() != null) {
                Immobile immobile = c.getImmobile();
                m.put("tipo", immobile.getTipologia());
                m.put("nomeProprietario", immobile.getProprietario().getNome() + " " + immobile.getProprietario().getCognome());

                String agenteNome = null;
                if (c.getAgente() != null) {
                    agenteNome = c.getAgente().getNome() + " " + c.getAgente().getCognome();
                }
                m.put("agenteAssegnato", agenteNome);
            } else {
                m.put("tipo", null);
                m.put("nomeProprietario", null);
                m.put("agenteAssegnato", null);
            }

            return m;
        }).collect(Collectors.toList());

        result.put("contratti", contrattiData);
        result.put("nextOffset", offset + limit);
        result.put("hasMore", (offset + limit) < totalContratti);
        result.put("pageSize", contractiBatch.size());

        return result;
    }

    /**
     * Restituisce valutazioni generate solo da AI con offset/limit
     * Mostra i dettagli dell'immobile e il prezzo stimato dall'AI
     */
    public Map<String, Object> getValutazioniSoloAILoadMore(int offset, int limit) {
        Map<String, Object> result = new LinkedHashMap<>();

        // Ottieni tutte le valutazioni con stato "solo_AI"
        List<Valutazione> tutteValutazioni = valutazioneRepository.findByStatoValutazioneNome("solo_AI");

        // Applica offset e limit manualmente
        int totalValutazioni = tutteValutazioni.size();
        List<Valutazione> valutazioniBatch = tutteValutazioni.stream()
            .skip(offset)
            .limit(limit)
            .collect(Collectors.toList());

        // Trasforma in Map mostrando i dettagli della valutazione
        List<Map<String, Object>> valuazioniData = valutazioniBatch.stream().map(v -> {
            Map<String, Object> m = new LinkedHashMap<>();

            // ID della valutazione (per operazioni di eliminazione)
            m.put("id", v.getId());

            // Dati valutazione
            m.put("prezzoAI", v.getPrezzoAI());
            m.put("dataValutazione", v.getDataValutazione());
            m.put("descrizione", v.getDescrizione());

            if (v.getImmobile() != null) {
                Immobile immobile = v.getImmobile();
                m.put("tipo", immobile.getTipologia());
                m.put("via", immobile.getVia());
                m.put("citta", immobile.getCitta());
                m.put("cap", immobile.getCap());
                m.put("provincia", immobile.getProvincia());
                m.put("metratura", immobile.getMetratura());
                m.put("condizioni", immobile.getCondizioni());
                m.put("stanze", immobile.getStanze());
                m.put("bagni", immobile.getBagni());
                m.put("piano", immobile.getPiano());
                m.put("ascensore", immobile.getAscensore());
                m.put("garage", immobile.getGarage());
                m.put("giardino", immobile.getGiardino());
                m.put("balcone", immobile.getBalcone());
                m.put("terrazzo", immobile.getTerrazzo());
                m.put("cantina", immobile.getCantina());
                m.put("riscaldamento", immobile.getRiscaldamento());

                if (immobile.getProprietario() != null) {
                    m.put("nomeProprietario", immobile.getProprietario().getNome() + " " + immobile.getProprietario().getCognome());
                    m.put("emailProprietario", immobile.getProprietario().getEmail());
                    m.put("telefonoProprietario", immobile.getProprietario().getTelefono());
                } else {
                    m.put("nomeProprietario", null);
                    m.put("emailProprietario", null);
                    m.put("telefonoProprietario", null);
                }

                m.put("descrizione", immobile.getDescrizione());
            } else {
                m.put("tipo", null);
                m.put("via", null);
                m.put("citta", null);
                m.put("cap", null);
                m.put("provincia", null);
                m.put("metratura", null);
                m.put("condizioni", null);
                m.put("stanze", null);
                m.put("bagni", null);
                m.put("piano", null);
                m.put("ascensore", null);
                m.put("garage", null);
                m.put("giardino", null);
                m.put("balcone", null);
                m.put("terrazzo", null);
                m.put("cantina", null);
                m.put("riscaldamento", null);
                m.put("nomeProprietario", null);
                m.put("emailProprietario", null);
                m.put("telefonoProprietario", null);
                m.put("descrizione", null);
            }

            return m;
        }).collect(Collectors.toList());

        result.put("valutazioni", valuazioniData);
        result.put("nextOffset", offset + limit);
        result.put("hasMore", (offset + limit) < totalValutazioni);
        result.put("pageSize", valutazioniBatch.size());
        result.put("agents", getAllAgents());

        return result;
    }

    public Map<String, Object> getValutazioniInVerficaLoadMore(int offset, int limit) {
        Map<String, Object> result = new LinkedHashMap<>();

        // Ottieni tutte le valutazioni con stato "in_verifica"
        List<Valutazione> tutteValutazioni = valutazioneRepository.findByStatoValutazioneNome("in_verifica");

        // Applica offset e limit manualmente
        int totalValutazioni = tutteValutazioni.size();
        List<Valutazione> valutazioniBatch = tutteValutazioni.stream()
            .skip(offset)
            .limit(limit)
            .collect(Collectors.toList());

        // Trasforma in Map mostrando TUTTI i campi della tabella valutazione
        List<Map<String, Object>> valuazioniData = valutazioniBatch.stream().map(v -> {
            Map<String, Object> m = new LinkedHashMap<>();

            // ID della valutazione (per operazioni di eliminazione)
            m.put("id", v.getId());

            // Tutti i campi della tabella Valutazioni
            m.put("prezzoAI", v.getPrezzoAI());
            m.put("prezzoUmano", v.getPrezzoUmano());
            m.put("dataValutazione", v.getDataValutazione());
            m.put("statoValutazione", v.getStatoValutazione() != null ? v.getStatoValutazione().getNome() : null);
            m.put("descrizione", v.getDescrizione());

            // Dati agente
            if (v.getAgente() != null) {
                m.put("nomeAgente", v.getAgente().getNome() + " " + v.getAgente().getCognome());
                m.put("emailAgente", v.getAgente().getEmail());
            } else {
                m.put("nomeAgente", null);
                m.put("emailAgente", null);
            }

            // Dati immobile COMPLETI
            if (v.getImmobile() != null) {
                Immobile immobile = v.getImmobile();
                m.put("tipo", immobile.getTipologia());
                m.put("via", immobile.getVia());
                m.put("citta", immobile.getCitta());
                m.put("cap", immobile.getCap());
                m.put("provincia", immobile.getProvincia());
                m.put("metratura", immobile.getMetratura());
                m.put("condizioni", immobile.getCondizioni());
                m.put("stanze", immobile.getStanze());
                m.put("bagni", immobile.getBagni());
                m.put("piano", immobile.getPiano());
                m.put("ascensore", immobile.getAscensore());
                m.put("garage", immobile.getGarage());
                m.put("giardino", immobile.getGiardino());
                m.put("balcone", immobile.getBalcone());
                m.put("terrazzo", immobile.getTerrazzo());
                m.put("cantina", immobile.getCantina());
                m.put("riscaldamento", immobile.getRiscaldamento());

                if (immobile.getProprietario() != null) {
                    m.put("nomeProprietario", immobile.getProprietario().getNome() + " " + immobile.getProprietario().getCognome());
                    m.put("emailProprietario", immobile.getProprietario().getEmail());
                    m.put("telefonoProprietario", immobile.getProprietario().getTelefono());
                } else {
                    m.put("nomeProprietario", null);
                    m.put("emailProprietario", null);
                    m.put("telefonoProprietario", null);
                }

                m.put("descrizione", immobile.getDescrizione());
            } else {
                m.put("tipo", null);
                m.put("via", null);
                m.put("citta", null);
                m.put("cap", null);
                m.put("provincia", null);
                m.put("metratura", null);
                m.put("condizioni", null);
                m.put("stanze", null);
                m.put("bagni", null);
                m.put("piano", null);
                m.put("ascensore", null);
                m.put("garage", null);
                m.put("giardino", null);
                m.put("balcone", null);
                m.put("terrazzo", null);
                m.put("cantina", null);
                m.put("riscaldamento", null);
                m.put("nomeProprietario", null);
                m.put("emailProprietario", null);
                m.put("telefonoProprietario", null);
                m.put("descrizione", null);
            }

            return m;
        }).collect(Collectors.toList());

        result.put("valutazioni", valuazioniData);
        result.put("nextOffset", offset + limit);
        result.put("hasMore", (offset + limit) < totalValutazioni);
        result.put("pageSize", valutazioniBatch.size());

        return result;
    }

    /**
     * Elimina una valutazione per ID
     */
    public void deleteValutazione(Integer id) {
        valutazioneRepository.deleteById(id);
    }

    /**
     * Aggiorna i campi di una valutazione e dell'immobile collegato
     */
    public void updateValutazione(Integer id, Map<String, Object> updates) {
        Valutazione valutazione = valutazioneRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Valutazione non trovata"));

        Immobile immobile = valutazione.getImmobile();
        boolean immobileModificato = false;

        // Aggiorna campi della valutazione
        if (updates.containsKey("prezzoAI")) {
            valutazione.setPrezzoAI((Integer) updates.get("prezzoAI"));
        }
        if (updates.containsKey("prezzoUmano")) {
            valutazione.setPrezzoUmano((Integer) updates.get("prezzoUmano"));
        }
        if (updates.containsKey("dataValutazione")) {
            String dataStr = (String) updates.get("dataValutazione");
            valutazione.setDataValutazione(LocalDateTime.parse(dataStr));
        }
        if (updates.containsKey("descrizione")) {
            valutazione.setDescrizione((String) updates.get("descrizione"));
        }

        // Aggiorna campi dell'immobile
        if (updates.containsKey("tipo")) {
            immobile.setTipologia((String) updates.get("tipo"));
            immobileModificato = true;
        }
        if (updates.containsKey("via")) {
            immobile.setVia((String) updates.get("via"));
            immobileModificato = true;
        }
        if (updates.containsKey("citta")) {
            immobile.setCitta((String) updates.get("citta"));
            immobileModificato = true;
        }
        if (updates.containsKey("cap")) {
            immobile.setCap((String) updates.get("cap"));
            immobileModificato = true;
        }
        if (updates.containsKey("provincia")) {
            immobile.setProvincia((String) updates.get("provincia"));
            immobileModificato = true;
        }
        if (updates.containsKey("metratura")) {
            immobile.setMetratura((Integer) updates.get("metratura"));
            immobileModificato = true;
        }
        if (updates.containsKey("condizioni")) {
            immobile.setCondizioni((String) updates.get("condizioni"));
            immobileModificato = true;
        }
        if (updates.containsKey("stanze")) {
            immobile.setStanze((Integer) updates.get("stanze"));
            immobileModificato = true;
        }
        if (updates.containsKey("bagni")) {
            immobile.setBagni((Integer) updates.get("bagni"));
            immobileModificato = true;
        }
        if (updates.containsKey("piano")) {
            immobile.setPiano((Integer) updates.get("piano"));
            immobileModificato = true;
        }
        if (updates.containsKey("ascensore")) {
            immobile.setAscensore((Boolean) updates.get("ascensore"));
            immobileModificato = true;
        }
        if (updates.containsKey("garage")) {
            immobile.setGarage((Boolean) updates.get("garage"));
            immobileModificato = true;
        }
        if (updates.containsKey("giardino")) {
            immobile.setGiardino((Boolean) updates.get("giardino"));
            immobileModificato = true;
        }
        if (updates.containsKey("balcone")) {
            immobile.setBalcone((Boolean) updates.get("balcone"));
            immobileModificato = true;
        }
        if (updates.containsKey("terrazzo")) {
            immobile.setTerrazzo((Boolean) updates.get("terrazzo"));
            immobileModificato = true;
        }
        if (updates.containsKey("cantina")) {
            immobile.setCantina((Boolean) updates.get("cantina"));
            immobileModificato = true;
        }
        if (updates.containsKey("riscaldamento")) {
            immobile.setRiscaldamento((String) updates.get("riscaldamento"));
            immobileModificato = true;
        }
        if (updates.containsKey("descrizioneImmobile")) {
            immobile.setDescrizione((String) updates.get("descrizioneImmobile"));
            immobileModificato = true;
        }

        // Salva le modifiche
        if (immobileModificato) {
            immobileJpaRepository.save(immobile);
        }
        valutazioneRepository.save(valutazione);
    }

    /**
     * Assegna un agente a una valutazione solo_AI e cambia lo stato in "in_verifica"
     */
    public void assegnaAgenteValutazioneAI(Integer idValutazione, Long idAgente) {
        Valutazione valutazione = valutazioneRepository.findById(idValutazione)
            .orElseThrow(() -> new RuntimeException("Valutazione non trovata"));
        if (!"solo_AI".equalsIgnoreCase(valutazione.getStatoValutazione().getNome())) {
            throw new RuntimeException("La valutazione non è di tipo solo_AI");
        }
        com.immobiliaris.demo.entity.User agente = userRepository.findById(idAgente)
            .orElseThrow(() -> new RuntimeException("Agente non trovato"));
        valutazione.setAgente(agente);
        // Cambia lo stato in "in_verifica"
        StatoValutazione nuovoStato = statoValutazioneRepository.findByNome("in_verifica")
            .orElseThrow(() -> new RuntimeException("Stato 'in_verifica' non trovato"));
        valutazione.setStatoValutazione(nuovoStato);
        
        // Imposta la data valutazione quando l'agente prende in carico
        valutazione.setDataValutazione(LocalDateTime.now());
        
        valutazioneRepository.save(valutazione);
    }

    /**
     * Recupera tutti gli agenti dal database
     * Filtra gli utenti con idTipo = 2 (Agenti)
     * @return Lista di mappe con nome e cognome concatenati
     */
    public List<Map<String, String>> getAllAgents() {
        return userRepository.findAll().stream()
                .filter(user -> user.getTipoUtente() != null && user.getTipoUtente().getIdTipo() == 2)
                .map(user -> {
                    Map<String, String> agentMap = new LinkedHashMap<>();
                    agentMap.put("nomeCognome", user.getNome() + " " + user.getCognome());
                    return agentMap;
                })
                .collect(Collectors.toList());
    }

    /**
     * Ottiene la lista di tutti gli agenti con le loro statistiche
     * Ordinati in modo decrescente per numero di contratti conclusi
     * @return Map con lista agenti e totale
     */
    public Map<String, Object> getTuttiAgentiConStatistiche() {
        Map<String, Object> result = new LinkedHashMap<>();

        // Ottieni tutti gli agenti (idTipo = 2)
        List<com.immobiliaris.demo.entity.User> tuttiAgenti = userRepository.findAll().stream()
            .filter(u -> u.getTipoUtente() != null && u.getTipoUtente().getIdTipo() == 2)
            .collect(Collectors.toList());

        // Per ogni agente, calcola le statistiche richieste
        List<Map<String, Object>> agentiDati = tuttiAgenti.stream()
            .map(agente -> {
                Map<String, Object> agentMap = new LinkedHashMap<>();
                
                // Nome e Cognome
                agentMap.put("nome", agente.getNome());
                agentMap.put("cognome", agente.getCognome());
                
                // Contratti conclusi dell'agente (stato "chiuso")
                List<Contratto> contrattiConclusi = contrattoRepository.findByStatoContrattoNome("chiuso")
                    .stream()
                    .filter(c -> c.getAgente() != null && c.getAgente().getIdUtente().equals(agente.getIdUtente()))
                    .collect(Collectors.toList());
                
                agentMap.put("contrattiConclusi", (long) contrattiConclusi.size());
                
                // Immobili in gestione = valutazioni con stato "in_verifica" dell'agente
                long immobiliInGestione = valutazioneRepository.countByStatoValutazioneNomeAndAgenteIdUtente("in_verifica", agente.getIdUtente().intValue());
                agentMap.put("immobiliInGestione", immobiliInGestione);
                
                // Fatturato = somma dei prezzoUmano delle valutazioni collegate ai contratti chiusi
                long fatturato = contrattiConclusi.stream()
                    .map(c -> {
                        if (c.getValutazione() != null && c.getValutazione().getPrezzoUmano() != null) {
                            return (long) c.getValutazione().getPrezzoUmano();
                        }
                        return 0L;
                    })
                    .reduce(0L, Long::sum);
                agentMap.put("fatturato", fatturato);
                
                return agentMap;
            })
            // Ordina per numero di contratti conclusi in modo decrescente
            .sorted((a, b) -> Long.compare((Long) b.get("contrattiConclusi"), (Long) a.get("contrattiConclusi")))
            .collect(Collectors.toList());

        result.put("agenti", agentiDati);
        return result;
    }

    /**
     * Calcola i tempi medi del processo e la valutazione di performance
     * @return Map con tempi e valutazione performance
     */
    public Map<String, Object> getTempiProcessoEPerformance() {
        Map<String, Object> result = new LinkedHashMap<>();

        // Ottieni tutti gli immobili con valutazione e contratto
        List<Immobile> immobili = immobileRepository.findAll();
        
        // Lista per calcolare tempo AI -> Presa in carico
        List<Long> tempiAIaPresaInCarico = new java.util.ArrayList<>();
        
        // Lista per calcolare tempo Presa in carico -> Firma contratto
        List<Long> tempiPresaInCaricoaContratto = new java.util.ArrayList<>();

        for (Immobile immobile : immobili) {
            // Trova la valutazione più recente dell'immobile
            List<Valutazione> valutazioni = valutazioneRepository.findByImmobileIdOrderByDataValutazioneDesc(immobile.getId());
            
            if (!valutazioni.isEmpty()) {
                Valutazione valutazione = valutazioni.get(0);
                
                // Tempo AI -> Presa in carico (se esiste dataValutazione)
                if (valutazione.getDataValutazione() != null && immobile.getDataRegistrazione() != null) {
                    long secondi = java.time.Duration.between(
                        immobile.getDataRegistrazione(), 
                        valutazione.getDataValutazione()
                    ).getSeconds();
                    tempiAIaPresaInCarico.add(secondi);
                }
                
                // Tempo Presa in carico -> Firma contratto
                if (valutazione.getDataValutazione() != null) {
                    // Trova contratto collegato a questa valutazione
                    List<Contratto> contratti = contrattoRepository.findByStatoContrattoNome("chiuso").stream()
                        .filter(c -> c.getValutazione() != null && 
                                     c.getValutazione().getId().equals(valutazione.getId()) &&
                                     c.getDataInizio() != null)
                        .collect(Collectors.toList());
                    
                    if (!contratti.isEmpty()) {
                        Contratto contratto = contratti.get(0);
                        long secondi = java.time.Duration.between(
                            valutazione.getDataValutazione(), 
                            contratto.getDataInizio()
                        ).getSeconds();
                        tempiPresaInCaricoaContratto.add(secondi);
                    }
                }
            }
        }

        // Calcola medie
        Map<String, Object> tempoAIaPresaInCaricoMap = calcolaTempoMedio(tempiAIaPresaInCarico);
        Map<String, Object> tempoPresaInCaricoaContrattoMap = calcolaTempoMedio(tempiPresaInCaricoaContratto);

        result.put("tempoAIaPresaInCarico", tempoAIaPresaInCaricoMap);
        result.put("tempoPresaInCaricoaContratto", tempoPresaInCaricoaContrattoMap);

        // Calcola performance separate per ogni fase
        String performancePresaInCarico = calcolaPerformancePresaInCarico(tempoAIaPresaInCaricoMap);
        String performanceContratto = calcolaPerformanceContratto(tempoPresaInCaricoaContrattoMap);
        
        result.put("valutazionePerformancePresaInCarico", performancePresaInCarico);
        result.put("valutazionePerformanceContratto", performanceContratto);

        return result;
    }

    /**
     * Calcola il tempo medio in giorni, ore, minuti e secondi
     * @param tempiInSecondi Lista di tempi in secondi
     * @return Map con giorni, ore, minuti, secondi
     */
    private Map<String, Object> calcolaTempoMedio(List<Long> tempiInSecondi) {
        Map<String, Object> result = new LinkedHashMap<>();
        
        if (tempiInSecondi.isEmpty()) {
            result.put("giorni", 0);
            result.put("ore", 0);
            result.put("minuti", 0);
            result.put("secondi", 0);
            result.put("totaleSecondi", 0L);
            return result;
        }

        // Calcola media
        long mediaSecondi = (long) tempiInSecondi.stream()
            .mapToLong(Long::longValue)
            .average()
            .orElse(0.0);

        // Converti in giorni, ore, minuti, secondi
        long giorni = mediaSecondi / 86400;
        long resto = mediaSecondi % 86400;
        long ore = resto / 3600;
        resto = resto % 3600;
        long minuti = resto / 60;
        long secondi = resto % 60;

        result.put("giorni", giorni);
        result.put("ore", ore);
        result.put("minuti", minuti);
        result.put("secondi", secondi);
        result.put("totaleSecondi", mediaSecondi);

        return result;
    }

    /**
     * Calcola la valutazione di performance basata sui tempi
     * @param tempoAIaPresaInCarico Tempo medio AI -> Presa in carico
     * @param tempoPresaInCaricoaContratto Tempo medio Presa in carico -> Contratto
     * @return Valutazione: "eccellente", "ottimo", "buono", "standard"
     */
    private String calcolaPerformance(Map<String, Object> tempoAIaPresaInCarico, 
                                       Map<String, Object> tempoPresaInCaricoaContratto) {
        long totaleSecondi1 = (Long) tempoAIaPresaInCarico.getOrDefault("totaleSecondi", 0L);
        long totaleSecondi2 = (Long) tempoPresaInCaricoaContratto.getOrDefault("totaleSecondi", 0L);
        
        long totaleSecondi = totaleSecondi1 + totaleSecondi2;
        long totaleGiorni = totaleSecondi / 86400;

        // Valutazione basata sul tempo totale
        if (totaleGiorni <= 7) {
            return "eccellente";
        } else if (totaleGiorni <= 14) {
            return "ottimo";
        } else if (totaleGiorni <= 30) {
            return "buono";
        } else {
            return "standard";
        }
    }

    /**
     * Calcola la valutazione di performance per la fase di presa in carico
     * @param tempoFase Tempo medio di presa in carico
     * @return Valutazione: "eccellente" (≤3d), "ottimo" (≤5d), "buono" (≤7d), "standard" (>7d)
     */
    private String calcolaPerformancePresaInCarico(Map<String, Object> tempoFase) {
        long totaleSecondi = (Long) tempoFase.getOrDefault("totaleSecondi", 0L);
        long totaleGiorni = totaleSecondi / 86400;

        // Valutazione basata sul tempo della fase di presa in carico
        if (totaleGiorni <= 3) {
            return "eccellente";
        } else if (totaleGiorni <= 5) {
            return "ottimo";
        } else if (totaleGiorni <= 7) {
            return "buono";
        } else {
            return "standard";
        }
    }

    /**
     * Calcola la valutazione di performance per la fase di contratto
     * @param tempoFase Tempo medio fino al contratto
     * @return Valutazione: "eccellente" (≤7d), "ottimo" (≤14d), "buono" (≤30d), "standard" (>30d)
     */
    private String calcolaPerformanceContratto(Map<String, Object> tempoFase) {
        long totaleSecondi = (Long) tempoFase.getOrDefault("totaleSecondi", 0L);
        long totaleGiorni = totaleSecondi / 86400;

        // Valutazione basata sul tempo della fase di contratto
        if (totaleGiorni <= 7) {
            return "eccellente";
        } else if (totaleGiorni <= 14) {
            return "ottimo";
        } else if (totaleGiorni <= 30) {
            return "buono";
        } else {
            return "standard";
        }
    }

    /**
     * Ottiene tutti gli immobili con tutti i dettagli inclusi prezzoAI e prezzoUmano dalla valutazione
     * @param offset Offset per la paginazione
     * @param limit Numero di elementi da restituire
     * @return Map con immobili e metadati di paginazione
     */
    public Map<String, Object> getTuttiImmobiliConDettagli(int offset, int limit) {
        Map<String, Object> result = new LinkedHashMap<>();

        // Ottieni tutti gli immobili ordinati per data registrazione discendente
        List<Immobile> tuttiImmobili = immobileRepository.findAll().stream()
            .sorted((a, b) -> b.getDataRegistrazione().compareTo(a.getDataRegistrazione()))
            .collect(Collectors.toList());

        // Applica offset e limit
        int totalImmobili = tuttiImmobili.size();
        List<Immobile> immobiliBatch = tuttiImmobili.stream()
            .skip(offset)
            .limit(limit)
            .collect(Collectors.toList());

        // Trasforma in Map con tutti i dettagli
        List<Map<String, Object>> immobiliData = immobiliBatch.stream().map(immobile -> {
            Map<String, Object> m = new LinkedHashMap<>();

            // Dati base immobile
            m.put("id", immobile.getId());
            m.put("via", immobile.getVia());
            m.put("citta", immobile.getCitta());
            m.put("cap", immobile.getCap());
            m.put("provincia", immobile.getProvincia());
            m.put("tipologia", immobile.getTipologia());
            m.put("metratura", immobile.getMetratura());
            m.put("condizioni", immobile.getCondizioni());
            m.put("stanze", immobile.getStanze());
            m.put("bagni", immobile.getBagni());
            m.put("riscaldamento", immobile.getRiscaldamento());
            m.put("piano", immobile.getPiano());
            m.put("ascensore", immobile.getAscensore());
            m.put("garage", immobile.getGarage());
            m.put("giardino", immobile.getGiardino());
            m.put("balcone", immobile.getBalcone());
            m.put("terrazzo", immobile.getTerrazzo());
            m.put("cantina", immobile.getCantina());
            m.put("prezzo", immobile.getPrezzo());
            m.put("descrizione", immobile.getDescrizione());
            m.put("dataRegistrazione", immobile.getDataRegistrazione());

            // Stato immobile
            if (immobile.getStatoImmobile() != null) {
                m.put("statoImmobile", immobile.getStatoImmobile().getNome());
            } else {
                m.put("statoImmobile", null);
            }

            // Dati proprietario
            if (immobile.getProprietario() != null) {
                m.put("nomeProprietario", immobile.getProprietario().getNome() + " " + immobile.getProprietario().getCognome());
                m.put("emailProprietario", immobile.getProprietario().getEmail());
                m.put("telefonoProprietario", immobile.getProprietario().getTelefono());
            } else {
                m.put("nomeProprietario", null);
                m.put("emailProprietario", null);
                m.put("telefonoProprietario", null);
            }

            // Trova la valutazione più recente per questo immobile
            List<Valutazione> valutazioni = valutazioneRepository.findByImmobileIdOrderByDataValutazioneDesc(immobile.getId());
            if (!valutazioni.isEmpty()) {
                Valutazione valutazioneRecente = valutazioni.get(0);
                m.put("prezzoAI", valutazioneRecente.getPrezzoAI());
                m.put("prezzoUmano", valutazioneRecente.getPrezzoUmano());
                m.put("dataValutazione", valutazioneRecente.getDataValutazione());
                m.put("descrizioneValutazione", valutazioneRecente.getDescrizione());
                
                if (valutazioneRecente.getStatoValutazione() != null) {
                    m.put("statoValutazione", valutazioneRecente.getStatoValutazione().getNome());
                } else {
                    m.put("statoValutazione", null);
                }
                
                if (valutazioneRecente.getAgente() != null) {
                    m.put("agenteAssegnato", valutazioneRecente.getAgente().getNome() + " " + valutazioneRecente.getAgente().getCognome());
                } else {
                    m.put("agenteAssegnato", null);
                }
            } else {
                m.put("prezzoAI", null);
                m.put("prezzoUmano", null);
                m.put("dataValutazione", null);
                m.put("descrizioneValutazione", null);
                m.put("statoValutazione", null);
                m.put("agenteAssegnato", null);
            }

            return m;
        }).collect(Collectors.toList());

        result.put("immobili", immobiliData);
        result.put("nextOffset", offset + limit);
        result.put("hasMore", (offset + limit) < totalImmobili);
        result.put("pageSize", immobiliBatch.size());
        result.put("total", totalImmobili);

        return result;
    }
}

