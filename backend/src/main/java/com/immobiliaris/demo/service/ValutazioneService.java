/**
 * Service per gestione valutazioni immobili.
 * 
 * Responsabilità:
 * - Valutazione automatica immobili con algoritmo AI multi-fattoriale
 * - Calcolo prezzo basato su:
 *   1. Quotazione base per CAP (tabella Zone)
 *   2. Coefficiente efficienza funzionale (bagni/stanze vs metratura)
 *   3. Coefficiente qualitativo (condizioni + tipologia)
 *   4. Moltiplicatore accessori (garage, terrazzo, giardino, ecc.)
 *   5. Fattori riscaldamento e piano
 * 
 * Formula finale:
 * Prezzo_AI = (metratura × quotazione_CAP) × C_Funzionale × C_Qualitativo × M_Finale
 * 
 * @author Sistema IMMOBILIARIS
 * @version 1.0
 * @since 2025-12-01
 */
package com.immobiliaris.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.immobiliaris.demo.entity.Immobile;
import com.immobiliaris.demo.entity.Valutazione;
import com.immobiliaris.demo.entity.StatoValutazione;
import com.immobiliaris.demo.repository.ValutazioneJpaRepository;
import com.immobiliaris.demo.repository.StatoValutazioneRepository;
import com.immobiliaris.demo.repository.ZonaRepository;

@Service
public class ValutazioneService {
    
    /** Repository per operazioni su Valutazioni */
    @Autowired
    private ValutazioneJpaRepository valutazioneJpaRepository;

    /** Repository per stati valutazione (solo_AI, in_verifica, approvata) */
    @Autowired
    private StatoValutazioneRepository statoValutazioneRepository;
    
    /** Repository per zone CAP e prezzi medi al mq */
    @Autowired
    private ZonaRepository zonaRepository; 

    /**
     * Esegue la valutazione automatica dell'immobile e la salva.
     * 
     * Procedura:
     * 1. Crea nuova istanza Valutazione
     * 2. Assegna immobile
     * 3. Calcola prezzo AI tramite algoritmo multi-fattoriale
     * 4. Imposta stato "solo_AI" (nessun agente assegnato)
     * 5. Salva nel database
     * 
     * La valutazione rimane in stato solo_AI finché un agente non la verifica.
     * 
     * @param immobile Immobile da valutare
     * @return Valutazione salvata con prezzoAI calcolato
     * @throws RuntimeException se stato "solo_AI" non trovato nel database
     * 
     * @see #calcolaPrezzoAI(Immobile)
     */
    public Valutazione valutaImmobile(Immobile immobile) {
        Valutazione valutazione = new Valutazione();
        valutazione.setImmobile(immobile);
        
        // Calcola prezzo AI con algoritmo avanzato
        int prezzo = calcolaPrezzoAI(immobile);
        valutazione.setPrezzoAI(prezzo);
        
        // Imposta stato "solo_AI"
        StatoValutazione statoSoloAI = statoValutazioneRepository.findByNome("solo_AI")
            .orElseThrow(() -> new RuntimeException("Stato valutazione 'solo_AI' non trovato"));
        valutazione.setStatoValutazione(statoSoloAI);

        return valutazioneJpaRepository.save(valutazione);
    }

    /**
     * Calcola il prezzo stimato dall'AI con algoritmo multi-fattoriale.
     * 
     * FASE 1: BASE (metratura × quotazione_CAP per zona)
     * - Recupera prezzo medio al mq dalla tabella Zone per CAP
     * - Se CAP non mappato, ritorna 0
     * - Se metratura ≤ 0, ritorna 0
     * 
     * FASE 2: C_FUNZIONALE (efficienza bagni e stanze)
     * - Penalità -5% se bagni < (metratura / 70)
     * - Penalità -3% se stanze > (metratura / 20)
     * 
     * FASE 3: C_QUALITATIVO (condizioni + tipologia)
     * Condizioni:
     * - Nuovo: +15%
     * - Ottimo: +10%
     * - Buono: 0% (neutro)
     * - Da ristrutturare: -25%
     * Tipologia:
     * - Villa: +30%
     * - Attico/Loft: +12%
     * - Appartamento: 0% (neutro)
     * 
     * FASE 4: M_FINALE (accessori + riscaldamento + piano)
     * Accessori:
     * - Garage: +25% (bonus maggiorato)
     * - Terrazzo: +12%
     * - Giardino: +10%
     * - Ascensore: +8%
     * - Balcone: +5%
     * - Cantina: +3%
     * Riscaldamento:
     * - Teleriscaldamento: +8%
     * - Autonomo: +5%
     * - Centralizzato: -3%
     * Piano:
     * - Villa multipiano (piano > 1): +10% fisso
     * - Appartamento/Loft/Attico: +2% per piano
     * 
     * @param immobile Immobile da valutare
     * @return Prezzo stimato in euro (intero), oppure 0 se invalido
     */
    private int calcolaPrezzoAI(Immobile immobile) {
        // --- 1. Raccolta Dati Essenziali e Quotazione Base ---
        
        Integer metratura = immobile.getMetratura();
        Integer piano = immobile.getPiano();
        Integer stanze = immobile.getStanze();
        Integer bagni = immobile.getBagni();
        String tipologia = immobile.getTipologia();
        String condizioni = immobile.getCondizioni();
        String riscaldamento = immobile.getRiscaldamento();

        if (metratura == null || metratura <= 0) return 0;
        
        // Recupera quotazione base dal CAP
        String cap = immobile.getCap();
        Double quotazioneBase = zonaRepository.findByCap(cap)
            .map(zona -> zona.getPrezzoMedioMq().doubleValue())
            .orElse(0.0);
        
        if (quotazioneBase <= 0) return 0;

        // --- 2. FASE 0: COEFFICIENTE DI EFFICIENZA FUNZIONALE (C_Funzionale) ---
        Double moltiplicatoreBagni = 1.00;
        Double moltiplicatoreStanze = 1.00;

        // 2.1 Verifica Adeguatezza Bagni (Metratura / 70)
        if (bagni != null && bagni > 0) {
            int bagniMinimiRichiesti = (int) Math.ceil(metratura / 70.0);
            if (bagni < bagniMinimiRichiesti) {
                moltiplicatoreBagni = 0.95; // Penalità -5%
            }
        }
        
        // 2.2 Verifica Efficienza Stanze (Metratura / 20)
        if (stanze != null && stanze > 0) {
            int stanzeMaxFunzionali = (int) Math.floor(metratura / 20.0);
            if (stanze > stanzeMaxFunzionali) {
                moltiplicatoreStanze = 0.97; // Penalità -3%
            }
        }
        
        Double coefficienteFunzionale = moltiplicatoreBagni * moltiplicatoreStanze;

        // --- 3. FASE 1: COEFFICIENTE QUALITATIVO (C_Qualitativo) ---
        
        // 3.1 Coefficiente Condizioni
        Double coefficienteCondizioni = 1.00;
        if (condizioni != null) {
            if ("Nuovo".equalsIgnoreCase(condizioni)) coefficienteCondizioni = 1.15;
            else if ("Ottimo".equalsIgnoreCase(condizioni)) coefficienteCondizioni = 1.10;
            else if ("Buono".equalsIgnoreCase(condizioni)) coefficienteCondizioni = 1.00;
            else if ("Da Ristrutturare".equalsIgnoreCase(condizioni) || "Da ristrutturare".equalsIgnoreCase(condizioni)) coefficienteCondizioni = 0.75;
        }

        // 3.2 Coefficiente Tipologia
        Double coefficienteTipologia = 1.00;
        if (tipologia != null) {
            if ("Villa".equalsIgnoreCase(tipologia)) coefficienteTipologia = 1.30;
            else if ("Attico".equalsIgnoreCase(tipologia) || "Loft".equalsIgnoreCase(tipologia)) coefficienteTipologia = 1.12;
            else if ("Appartamento".equalsIgnoreCase(tipologia)) coefficienteTipologia = 1.00;
        }

        Double coefficienteQualitativo = coefficienteCondizioni * coefficienteTipologia;

        // --- 4. FASE 2: MOLTIPLICATORE FINALE (M_Finale) ---
        
        Double percentualeAccessori = 0.0;
        
        // 4.1 Accessori Standard e di Lusso
        if (immobile.getAscensore() != null && immobile.getAscensore()) percentualeAccessori += 0.08;
        if (immobile.getBalcone() != null && immobile.getBalcone()) percentualeAccessori += 0.05;
        if (immobile.getTerrazzo() != null && immobile.getTerrazzo()) percentualeAccessori += 0.12;
        if (immobile.getGiardino() != null && immobile.getGiardino()) percentualeAccessori += 0.10;
        if (immobile.getCantina() != null && immobile.getCantina()) percentualeAccessori += 0.03;
        if (immobile.getGarage() != null && immobile.getGarage()) percentualeAccessori += 0.25; // Bonus maggiorato

        // 4.2 Riscaldamento (Logica a 3 livelli)
        if (riscaldamento != null) {
            if ("Teleriscaldamento".equalsIgnoreCase(riscaldamento)) {
                percentualeAccessori += 0.08; // Premio alto +8%
            } else if ("Autonomo".equalsIgnoreCase(riscaldamento)) {
                percentualeAccessori += 0.05; // Premio standard +5%
            } else if ("Centralizzato (Obsoleto)".equalsIgnoreCase(riscaldamento)) {
                percentualeAccessori -= 0.03; // Penalità -3%
            }
        }
        
        // 4.3 Logica Condizionale Piano/Livelli
        Double percentualePianoPerAltezza = 0.0;
        
        if ("Villa".equalsIgnoreCase(tipologia)) {
            // Logica Villa: Premio per la complessità strutturale su più livelli (se piano > 1)
            if (piano != null && piano > 1) { 
                percentualeAccessori += 0.10; // Premio fisso +10% per multi-livello
            }
            // La Villa non riceve premio per l'altezza (percentualePianoPerAltezza rimane 0.0)
        } else {
            // Logica Appartamento/Loft/Attico: Premio per l'altezza del piano (+2% per piano)
            if (piano != null && piano > 0) {
                percentualePianoPerAltezza = piano * 0.02;
            }
        }

        // M_Finale = 1 + Somma Accessori + Premio Altezza (se applicabile)
        Double moltiplicatoreFinale = 1.0 + percentualeAccessori + percentualePianoPerAltezza;

        // --- 5. CALCOLO FINALE ---
        
        Double prezzo = (metratura * quotazioneBase) 
                         * coefficienteFunzionale 
                         * coefficienteQualitativo 
                         * moltiplicatoreFinale;

        return prezzo.intValue();
    }
}
