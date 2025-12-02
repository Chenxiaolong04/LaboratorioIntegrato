package com.immobiliaris.demo.dto;

import java.util.List;

public class DashboardDTO {
    
    public static class StatisticheCard {
        public Integer numero;
        public String sottotitolo;
        public String descrizione;
        
        public StatisticheCard(Integer numero, String sottotitolo, String descrizione) {
            this.numero = numero;
            this.sottotitolo = sottotitolo;
            this.descrizione = descrizione;
        }
    }
    
    public static class PerformanceData {
        public String mese;
        public Integer acquisizioni;
        public Integer vendite;
        
        public PerformanceData(String mese, Integer acquisizioni, Integer vendite) {
            this.mese = mese;
            this.acquisizioni = acquisizioni;
            this.vendite = vendite;
        }
    }
    
    public static class ImmobileItem {
        public Integer id;
        public String via;
        public String citta;
        public Integer metratura;
        public String proprietario;
        public String stato;
        public Integer prezzo;
        public String dataInserimento;
        
        public ImmobileItem(Integer id, String via, String citta, Integer metratura,
                          String proprietario, String stato, Integer prezzo, String dataInserimento) {
            this.id = id;
            this.via = via;
            this.citta = citta;
            this.metratura = metratura;
            this.proprietario = proprietario;
            this.stato = stato;
            this.prezzo = prezzo;
            this.dataInserimento = dataInserimento;
        }
    }
    
    public static class PipelineData {
        public Integer richieste;
        public Integer trattative;
        public Integer vendite;
        
        public PipelineData(Integer richieste, Integer trattative, Integer vendite) {
            this.richieste = richieste;
            this.trattative = trattative;
            this.vendite = vendite;
        }
    }
    
    public static class ImmobiliPerStato {
        public Integer inVendita;
        public Integer inValutazione;
        public Integer inTrattativa;
        public Integer venduti;
        
        public ImmobiliPerStato(Integer inVendita, Integer inValutazione, Integer inTrattativa, Integer venduti) {
            this.inVendita = inVendita;
            this.inValutazione = inValutazione;
            this.inTrattativa = inTrattativa;
            this.venduti = venduti;
        }
    }
    
    public static class DashboardResponse {
        public StatisticheCard valutazioniRichieste;
        public StatisticheCard immobiliAcquisiti;
        public StatisticheCard trattativeAttive;
        public StatisticheCard venditeChiuse;
        public List<PerformanceData> performance;
        public List<ImmobileItem> prossimiAttivita;
        public PipelineData pipeline;
        public ImmobiliPerStato immobiliPerStato;
    }
}