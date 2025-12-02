package com.immobiliaris.demo.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.immobiliaris.demo.repository.*;
import com.immobiliaris.demo.entity.*;
import com.immobiliaris.demo.dto.DashboardDTO;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardApiController {
    
    @Autowired
    private ValutazioneJpaRepository valutazioneRepository;
    
    @Autowired
    private ImmobileJpaRepository immobileRepository;
    
    @Autowired
    private ContrattoJpaRepository contrattoRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/agente/{agenteId}")
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
            dashboard.immobiliPerStato = getImmobiliPerStato(agenteId); // ← AGGIUNGI
            
            return ResponseEntity.ok(new Response("success", "Dashboard caricata", dashboard));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Response("error", "Errore recupero dashboard: " + e.getMessage(), null));
        }
    }
    
    @GetMapping("/agente/{agenteId}/attivita")
    public ResponseEntity<?> getProssimiAttivitaPaginati(@PathVariable Long agenteId, 
                                                         @RequestParam(defaultValue = "0") int page) {
        try {
            List<DashboardDTO.ImmobileItem> attivita = getProssimiAttivita(agenteId, page);
            return ResponseEntity.ok(new Response("success", "Attività caricate", attivita));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Response("error", "Errore caricamento attività: " + e.getMessage(), null));
        }
    }
    
    // METODI PRIVATI
    
    private DashboardDTO.StatisticheCard getValutazioniRichieste(Long agenteId) {
        LocalDate oggi = LocalDate.now();
        LocalDate inizioMese = oggi.withDayOfMonth(1);
        
        // Valutazioni create da agente questo mese
        long valutazioni = valutazioneRepository.findAll().stream()
            .filter(v -> v.getAgente() != null && v.getAgente().getIdUtente().equals(agenteId) &&
                    v.getDataValutazione() != null && !v.getDataValutazione().isBefore(inizioMese))
            .count();
        
        // Total valutazioni
        long totalValutazioni = valutazioneRepository.findAll().stream()
            .filter(v -> v.getAgente() != null && v.getAgente().getIdUtente().equals(agenteId))
            .count();
        
        return new DashboardDTO.StatisticheCard(
            (int)totalValutazioni,
            "+ " + valutazioni + " questo mese",
            "Valutazioni richieste"
        );
    }
    
    private DashboardDTO.StatisticheCard getImmobiliAcquisiti(Long agenteId) {
        // Immobili assegnati all'agente
        List<Immobile> immobili = immobileRepository.findAll().stream()
            .filter(i -> i.getProprietario() != null && i.getProprietario().getIdUtente().equals(agenteId))
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
                .filter(im -> im.getDataInserimento() != null && 
                        !im.getDataInserimento().isBefore(inizio) && 
                        !im.getDataInserimento().isAfter(fine))
                .count();
            
            // Vendite (contratti chiusi nel mese)
            long vendite = contrattoRepository.findAll().stream()
                .filter(c -> c.getAgente() != null && c.getAgente().getIdUtente().equals(agenteId) &&
                        c.getDataFine() != null && 
                        !c.getDataFine().isBefore(inizio) && 
                        !c.getDataFine().isAfter(fine) &&
                        c.getStatoContratto() != null && "chiuso".equalsIgnoreCase(c.getStatoContratto().getNome()))
                .count();
            
            performance.add(new DashboardDTO.PerformanceData(mese, (int)acquisizioni, (int)vendite));
        }
        
        return performance;
    }
    
    private List<DashboardDTO.ImmobileItem> getProssimiAttivita(Long agenteId, int page) {
        // 5 immobili per volta, ordinati per data_inserimento DESC
        int pageSize = 5;
        
        List<DashboardDTO.ImmobileItem> items = new ArrayList<>();
        
        List<Immobile> immobiliOrdinati = immobileRepository.findAll().stream()
            .sorted(Comparator.comparing((Immobile i) -> i.getDataInserimento() != null ? i.getDataInserimento() : LocalDate.MIN).reversed())
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
                immobile.getDataInserimento() != null ? immobile.getDataInserimento().toString() : "N/A"
            ));
        }
        
        return items;
    }
    
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
    
    private DashboardDTO.ImmobiliPerStato getImmobiliPerStato(Long agenteId) {
        List<Immobile> immobili = immobileRepository.findAll().stream()
            .filter(i -> i.getProprietario() != null && i.getProprietario().getIdUtente().equals(agenteId))
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
    
    static class Response {
        public String status;
        public String message;
        public Object data;
        
        public Response(String status, String message, Object data) {
            this.status = status;
            this.message = message;
            this.data = data;
        }
    }
}