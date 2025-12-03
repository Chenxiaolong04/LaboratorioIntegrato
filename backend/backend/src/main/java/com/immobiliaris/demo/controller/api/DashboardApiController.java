/**
 * Controller REST API per Dashboard Agente.
 * 
 * Fornisce endpoint per recuperare dati dashboard personalizzati per agenti immobiliari,
 * includendo:
 * - Statistiche di performance (valutazioni, acquisizioni, trattative, vendite)
 * - Grafico performance ultimi 6 mesi
 * - Prossime attività/immobili da gestire
 * - Pipeline di lavoro (richieste, trattative, vendite chiuse)
 * - Conteggio immobili per stato
 * 
 * Autenticazione richiesta: SI (via Principal o path parameter)
 * Ruoli richiesti: AGENT, ADMIN
 * 
 * @author Sistema IMMOBILIARIS
 * @version 1.0
 * @since 2025-12-01
 */
package com.immobiliaris.demo.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.immobiliaris.demo.repository.*;
import com.immobiliaris.demo.entity.*;
import com.immobiliaris.demo.dto.DashboardDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
@Tag(name = "Dashboard", description = "API Dashboard Agente - Recupera statistiche e performance agente")
public class DashboardApiController {
    
    /** Repository per operazioni su Valutazioni */
    @Autowired
    private ValutazioneJpaRepository valutazioneRepository;
    
    /** Repository per operazioni su Immobili */
    @Autowired
    private ImmobileJpaRepository immobileRepository;
    
    /** Repository per operazioni su Contratti */
    @Autowired
    private ContrattoJpaRepository contrattoRepository;
    
    /** Repository per operazioni su Utenti */
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Endpoint legacy per dashboard con path parameter agenteId.
     * DEPRECATO: usare {@link #getDashboard(Principal)} senza parametri.
     * 
     * @param agenteId ID dell'agente (da URL)
     * @return Dashboard con statistiche e performance agente
     * @throws RuntimeException se agente non trovato
     * 
     * @deprecated Usare GET /api/dashboard/agente senza parametri
     */
    @GetMapping("/agente/{agenteId}")
    @Deprecated(since = "1.0", forRemoval = false)
    public ResponseEntity<?> getDashboard(@PathVariable Long agenteId) {
        try {
            if(agenteId == null || agenteId <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("error", "ID agente non valido", null));
            }
            
            // Verifica agente esiste
            var agenteOpt = userRepository.findById(agenteId);
            if(agenteOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Response("error", "Agente non trovato", null));
            }
            
            DashboardDTO.DashboardResponse dashboard = new DashboardDTO.DashboardResponse();
            
            // 1. Statistiche
            dashboard.valutazioniRichieste = getValutazioniRichieste(agenteId);
            dashboard.immobiliAcquisiti = getImmobiliAcquisiti(agenteId);
            dashboard.trattativeAttive = getTrattativeAttive(agenteId);
            dashboard.venditeChiuse = getVenditeChiuse(agenteId);
            
            // 2. Performance ultimi 6 mesi
            dashboard.performance = getPerformance(agenteId);
            
            // 3. Prossime attività (prima pagina)
            dashboard.prossimiAttivita = getProssimiAttivita(agenteId, 0);
            
            // 4. Pipeline
            dashboard.pipeline = getPipeline(agenteId);
            dashboard.immobiliPerStato = getImmobiliPerStato(agenteId);
            
            return ResponseEntity.ok(new Response("success", "Dashboard caricata", dashboard));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Response("error", "Errore recupero dashboard: " + e.getMessage(), null));
        }
    }
    
    /**
     * Endpoint principale per dashboard agente autenticato.
     * 
     * Recupera tutti i dati dashboard dell'agente attualmente loggato.
     * L'identificazione dell'agente avviene tramite {@link Principal},
     * estratto automaticamente da Spring Security dalla sessione HTTP.
     * 
     * Dati restituiti:
     * - {@link DashboardDTO.StatisticheCard} x4: valutazioni, acquisizioni, trattative, vendite
     * - {@link DashboardDTO.PerformanceData[]}: performance ultimi 6 mesi
     * - {@link DashboardDTO.ImmobileItem[]}: prossimi immobili da gestire
     * - {@link DashboardDTO.PipelineData}: conteggio pipeline fasi
     * - {@link DashboardDTO.ImmobiliPerStato}: distribuzione immobili per stato
     * 
     * @param principal {@link Principal} dell'utente autenticato (iniettato da Spring Security).
     *                 Contiene l'email dell'utente loggato.
     * @return ResponseEntity con status 200 OK e Dashboard completa
     * @return ResponseEntity con status 401 UNAUTHORIZED se non autenticato
     * @return ResponseEntity con status 404 NOT_FOUND se agente non trovato
     * @return ResponseEntity con status 500 INTERNAL_SERVER_ERROR se errore generico
     * 
     * @see Principal
     * @see DashboardDTO.DashboardResponse
     */
    @GetMapping("/agente")
    @Operation(
        summary = "Dashboard agente autenticato",
        description = "Recupera dashboard completo dell'agente attualmente loggato con statistiche, performance, pipeline e attività"
    )
    @SecurityRequirement(name = "session")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dashboard caricata correttamente"),
        @ApiResponse(responseCode = "401", description = "Utente non autenticato"),
        @ApiResponse(responseCode = "404", description = "Agente non trovato nel sistema"),
        @ApiResponse(responseCode = "500", description = "Errore interno nel caricamento dashboard")
    })
    public ResponseEntity<?> getDashboard(Principal principal) {
        try {
            if(principal == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new Response("error", "Utente non autenticato", null));
            }
            
            // Recupera l'utente autenticato dalla email
            String email = principal.getName();
            var agenteOpt = userRepository.findByEmail(email);
            
            if(agenteOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Response("error", "Agente non trovato", null));
            }
            
            Long agenteId = agenteOpt.get().getIdUtente();
            
            DashboardDTO.DashboardResponse dashboard = new DashboardDTO.DashboardResponse();
            
            // 1. Statistiche principali
            dashboard.valutazioniRichieste = getValutazioniRichieste(agenteId);
            dashboard.immobiliAcquisiti = getImmobiliAcquisiti(agenteId);
            dashboard.trattativeAttive = getTrattativeAttive(agenteId);
            dashboard.venditeChiuse = getVenditeChiuse(agenteId);
            
            // 2. Performance ultimi 6 mesi (per grafico)
            dashboard.performance = getPerformance(agenteId);
            
            // 3. Prossime attività (prima pagina, 5 immobili)
            dashboard.prossimiAttivita = getProssimiAttivita(agenteId, 0);
            
            // 4. Pipeline (richieste in attesa, trattative, vendite chiuse)
            dashboard.pipeline = getPipeline(agenteId);
            
            // 5. Distribuzione immobili per stato
            dashboard.immobiliPerStato = getImmobiliPerStato(agenteId);
            
            return ResponseEntity.ok(new Response("success", "Dashboard caricata", dashboard));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Response("error", "Errore recupero dashboard: " + e.getMessage(), null));
        }
    }
    
    // ========== METODI PRIVATI DI ELABORAZIONE DATI ==========
    
    /**
     * Calcola statistiche valutazioni richieste all'agente.
     * 
     * Recupera:
     * - Numero totale valutazioni assegnate all'agente
     * - Numero valutazioni del mese corrente
     * 
     * @param agenteId ID dell'agente
     * @return {@link DashboardDTO.StatisticheCard} con numero totale e delta mensile
     */
    private DashboardDTO.StatisticheCard getValutazioniRichieste(Long agenteId) {
        LocalDate oggi = LocalDate.now();
        LocalDate inizioMese = oggi.withDayOfMonth(1);
        
        // Valutazioni create da agente questo mese
        long valutazioni = valutazioneRepository.findAll().stream()
            .filter(v -> v.getAgente() != null && v.getAgente().getIdUtente() != null && 
                    v.getAgente().getIdUtente().equals(agenteId) &&
                    v.getDataValutazione() != null && !v.getDataValutazione().toLocalDate().isBefore(inizioMese))
            .count();
        
        // Total valutazioni
        long totalValutazioni = valutazioneRepository.findAll().stream()
            .filter(v -> v.getAgente() != null && v.getAgente().getIdUtente() != null && v.getAgente().getIdUtente().equals(agenteId))
            .count();
        
        return new DashboardDTO.StatisticheCard(
            (int)totalValutazioni,
            "+ " + valutazioni + " questo mese",
            "Valutazioni richieste"
        );
    }
    
    /**
     * Calcola statistiche immobili acquisiti dall'agente.
     * 
     * Recupera:
     * - Numero totale immobili associati all'agente
     * - Numero immobili in stato "attivo" (esclusiva)
     * 
     * @param agenteId ID dell'agente
     * @return {@link DashboardDTO.StatisticheCard} con totale e immobili in esclusiva
     */
    private DashboardDTO.StatisticheCard getImmobiliAcquisiti(Long agenteId) {
        // Immobili assegnati all'agente (tramite proprietario)
        List<Immobile> immobili = immobileRepository.findAll().stream()
            .filter(i -> i.getProprietario() != null && i.getProprietario().getIdUtente() != null && 
                    i.getProprietario().getIdUtente().equals(agenteId))
            .toList();
        
        // In esclusiva (stato "attivo")
        long esclusiva = immobili.stream()
            .filter(i -> i.getStatoImmobile() != null && "attivo".equalsIgnoreCase(i.getStatoImmobile().getNome()))
            .count();
        
        return new DashboardDTO.StatisticheCard(
            immobili.size(),
            "+ " + esclusiva + " in esclusiva",
            "Immobili acquisiti"
        );
    }
    
    /**
     * Calcola statistiche trattative attive dell'agente.
     * 
     * Recupera:
     * - Numero totale contratti associati all'agente
     * - Numero contratti in stato "attivo" o "bozza" (fase finale)
     * 
     * @param agenteId ID dell'agente
     * @return {@link DashboardDTO.StatisticheCard} con totale e in fase finale
     */
    private DashboardDTO.StatisticheCard getTrattativeAttive(Long agenteId) {
        // Contratti in corso o fase finale
        List<Contratto> contratti = contrattoRepository.findAll().stream()
            .filter(c -> c.getAgente() != null && c.getAgente().getIdUtente().equals(agenteId))
            .toList();
        
        long attivi = contratti.stream()
            .filter(c -> c.getStatoContratto() != null && 
                    ("attivo".equalsIgnoreCase(c.getStatoContratto().getNome()) ||
                     "bozza".equalsIgnoreCase(c.getStatoContratto().getNome())))
            .count();
        
        return new DashboardDTO.StatisticheCard(
            contratti.size(),
            "+ " + attivi + " fase finale",
            "Trattative attive"
        );
    }
    
    /**
     * Calcola statistiche vendite chiuse dall'agente.
     * 
     * Recupera:
     * - Numero totale contratti con stato "chiuso"
     * - Valore totale dei contratti chiusi (somma prezzi immobili)
     * - Formatta valore in milioni di euro (€ X.XM)
     * 
     * @param agenteId ID dell'agente
     * @return {@link DashboardDTO.StatisticheCard} con numero vendite e valore totale
     */
    private DashboardDTO.StatisticheCard getVenditeChiuse(Long agenteId) {
        // Contratti chiusi
        List<Contratto> contratti = contrattoRepository.findAll().stream()
            .filter(c -> c.getAgente() != null && c.getAgente().getIdUtente().equals(agenteId) &&
                    c.getStatoContratto() != null && "chiuso".equalsIgnoreCase(c.getStatoContratto().getNome()))
            .toList();
        
        // Calcola valore totale
        long valoreTotal = contratti.stream()
            .mapToLong(c -> c.getImmobile() != null && c.getImmobile().getPrezzo() != null ? 
                    c.getImmobile().getPrezzo() : 0)
            .sum();
        
        String valoreTotalStr = valoreTotal > 0 ? String.format("€ %.1fM", valoreTotal / 1_000_000.0) : "€ 0";
        
        return new DashboardDTO.StatisticheCard(
            contratti.size(),
            valoreTotalStr + " valore totale",
            "Vendite chiuse"
        );
    }
    
    /**
     * Calcola performance agente ultimi 6 mesi.
     * 
     * Per ogni mese dei 6 precedenti:
     * - Conta acquisizioni (immobili registrati nel mese)
     * - Conta vendite (contratti chiusi nel mese)
     * 
     * @param agenteId ID dell'agente
     * @return List di {@link DashboardDTO.PerformanceData} ordinati cronologicamente (mese più vecchio prima)
     */
    private List<DashboardDTO.PerformanceData> getPerformance(Long agenteId) {
        List<DashboardDTO.PerformanceData> performance = new ArrayList<>();
        
        // Ultimi 6 mesi completi
        for (int i = 6; i >= 1; i--) {
            YearMonth ym = YearMonth.now().minusMonths(i);
            String mese = ym.getMonth().toString().substring(0, 3);
            
            LocalDate inizio = ym.atDay(1);
            LocalDate fine = ym.atEndOfMonth();
            
            // Acquisizioni (immobili creati nel mese)
            long acquisizioni = immobileRepository.findAll().stream()
                .filter(im -> {
                    if (im.getDataRegistrazione() != null) {
                        LocalDate data = im.getDataRegistrazione().toLocalDate();
                        return !data.isBefore(inizio) && !data.isAfter(fine);
                    }
                    return false;
                })
                .count();
            
            // Vendite (contratti chiusi nel mese)
            long vendite = contrattoRepository.findAll().stream()
                .filter(c -> c.getAgente() != null && c.getAgente().getIdUtente().equals(agenteId) &&
                        c.getDataFine() != null && 
                        !c.getDataFine().toLocalDate().isBefore(inizio) && 
                        !c.getDataFine().toLocalDate().isAfter(fine) &&
                        c.getStatoContratto() != null && "chiuso".equalsIgnoreCase(c.getStatoContratto().getNome()))
                .count();
            
            performance.add(new DashboardDTO.PerformanceData(mese, (int)acquisizioni, (int)vendite));
        }
        
        return performance;
    }
    
    /**
     * Recupera prossime attività (immobili) da gestire.
     * 
     * Implementa paginazione con page size di 5 immobili per pagina.
     * Ordina immobili per data_registrazione decrescente (più recenti prima).
     * 
     * @param agenteId ID dell'agente (non utilizzato nel filtro - recupera tutti)
     * @param page Numero pagina (0-based)
     * @return List di {@link DashboardDTO.ImmobileItem} per la pagina richiesta
     */
    private List<DashboardDTO.ImmobileItem> getProssimiAttivita(Long agenteId, int page) {
        // 5 immobili per volta, ordinati per data_registrazione DESC
        int pageSize = 5;
        
        List<DashboardDTO.ImmobileItem> items = new ArrayList<>();
        
        List<Immobile> immobiliOrdinati = immobileRepository.findAll().stream()
            .sorted(Comparator.comparing((Immobile i) -> i.getDataRegistrazione() != null ? i.getDataRegistrazione() : LocalDateTime.now()).reversed())
            .skip((long) page * pageSize)
            .limit(pageSize)
            .toList();
        
        for (Immobile immobile : immobiliOrdinati) {
            String proprietario = immobile.getProprietario() != null ? 
                immobile.getProprietario().getNome() + " " + immobile.getProprietario().getCognome() : "N/A";
            
            String stato = immobile.getStatoImmobile() != null ? 
                immobile.getStatoImmobile().getNome() : "N/A";
            
            items.add(new DashboardDTO.ImmobileItem(
                immobile.getId(),
                immobile.getVia(),
                immobile.getCitta(),
                immobile.getMetratura(),
                proprietario,
                stato,
                immobile.getPrezzo(),
                immobile.getDataRegistrazione() != null ? immobile.getDataRegistrazione().toString() : "N/A"
            ));
        }
        
        return items;
    }
    
    /**
     * Calcola dati pipeline agente.
     * 
     * Pipeline indica lo stato di avanzamento dei lavori:
     * - Richieste in attesa: valutazioni non ancora assegnate ad alcun agente
     * - Trattative in corso: contratti in stato "attivo"
     * - Vendite chiuse: contratti in stato "chiuso"
     * 
     * @param agenteId ID dell'agente
     * @return {@link DashboardDTO.PipelineData} con conteggi per fase
     */
    private DashboardDTO.PipelineData getPipeline(Long agenteId) {
        // Valutazioni in richiesta (non assegnate a questo agente)
        long richieste = valutazioneRepository.findAll().stream()
            .filter(v -> v.getAgente() == null)
            .count();
        
        // Trattative (contratti in corso)
        long trattative = contrattoRepository.findAll().stream()
            .filter(c -> c.getAgente() != null && c.getAgente().getIdUtente().equals(agenteId) &&
                    c.getStatoContratto() != null && "attivo".equalsIgnoreCase(c.getStatoContratto().getNome()))
            .count();
        
        // Vendite chiuse
        long vendite = contrattoRepository.findAll().stream()
            .filter(c -> c.getAgente() != null && c.getAgente().getIdUtente().equals(agenteId) &&
                    c.getStatoContratto() != null && "chiuso".equalsIgnoreCase(c.getStatoContratto().getNome()))
            .count();
        
        return new DashboardDTO.PipelineData((int)richieste, (int)trattative, (int)vendite);
    }
    
    /**
     * Calcola distribuzione immobili per stato.
     * 
     * Raggruppa immobili dell'agente per stato:
     * - In vendita: stato "attivo"
     * - In valutazione: stato "in valutazione"
     * - In trattativa: immobili con contratti "attivo"
     * - Venduti: stato "venduto"
     * 
     * @param agenteId ID dell'agente
     * @return {@link DashboardDTO.ImmobiliPerStato} con conteggio per stato
     */
    private DashboardDTO.ImmobiliPerStato getImmobiliPerStato(Long agenteId) {
        List<Immobile> immobili = immobileRepository.findAll().stream()
            .filter(i -> i.getProprietario() != null && i.getProprietario().getIdUtente() != null &&
                    i.getProprietario().getIdUtente().equals(agenteId))
            .toList();
        
        long inVendita = immobili.stream()
            .filter(i -> i.getStatoImmobile() != null && "attivo".equalsIgnoreCase(i.getStatoImmobile().getNome()))
            .count();
        
        long inValutazione = immobili.stream()
            .filter(i -> i.getStatoImmobile() != null && "in valutazione".equalsIgnoreCase(i.getStatoImmobile().getNome()))
            .count();
        
        long inTrattativa = contrattoRepository.findAll().stream()
            .filter(c -> c.getAgente() != null && c.getAgente().getIdUtente().equals(agenteId) &&
                    c.getStatoContratto() != null && "attivo".equalsIgnoreCase(c.getStatoContratto().getNome()))
            .map(c -> c.getImmobile().getId())
            .distinct()
            .count();
        
        long venduti = immobili.stream()
            .filter(i -> i.getStatoImmobile() != null && "venduto".equalsIgnoreCase(i.getStatoImmobile().getNome()))
            .count();
        
        return new DashboardDTO.ImmobiliPerStato((int)inVendita, (int)inValutazione, (int)inTrattativa, (int)venduti);
    }
    
    /**
     * DTO interno per risposta standardizzata.
     * Utilizzato per tutte le risposte JSON del controller.
     */
    static class Response {
        /** Stato della risposta: "success" o "error" */
        public String status;
        
        /** Messaggio descrittivo della risposta */
        public String message;
        
        /** Dati payload (può essere null per messaggi di errore) */
        public Object data;
        
        /**
         * Costruttore Response.
         * @param status Stato: "success" o "error"
         * @param message Messaggio per l'utente
         * @param data Dati aggiuntivi (null se errore)
         */
        public Response(String status, String message, Object data) {
            this.status = status;
            this.message = message;
            this.data = data;
        }
    }
}