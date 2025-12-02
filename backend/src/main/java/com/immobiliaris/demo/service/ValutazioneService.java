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
    @Autowired
    private ValutazioneJpaRepository valutazioneJpaRepository;

    @Autowired
    private StatoValutazioneRepository statoValutazioneRepository;
    
    @Autowired
    private ZonaRepository zonaRepository; 

    /**
     * Esegue la valutazione automatica dell'immobile e la salva
     */
    public Valutazione valutaImmobile(Immobile immobile) {
        Valutazione valutazione = new Valutazione();
        valutazione.setImmobile(immobile);
        int prezzo = calcolaPrezzoAI(immobile);
        valutazione.setPrezzoAI(prezzo);
        
        StatoValutazione statoSoloAI = statoValutazioneRepository.findByNome("solo_AI")
            .orElseThrow(() -> new RuntimeException("Stato valutazione 'solo_AI' non trovato"));
        valutazione.setStatoValutazione(statoSoloAI);

        return valutazioneJpaRepository.save(valutazione);
    }

    /**
     * Calcola il prezzo stimato AI con logica avanzata.
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
