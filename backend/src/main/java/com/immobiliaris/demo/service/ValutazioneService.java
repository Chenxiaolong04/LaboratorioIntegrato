package com.immobiliaris.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.immobiliaris.demo.entity.Immobile;
import com.immobiliaris.demo.entity.Valutazione;
import com.immobiliaris.demo.repository.ValutazioneJpaRepository;

@Service
public class ValutazioneService {
    @Autowired
    private ValutazioneJpaRepository valutazioneJpaRepository;

    /**
     * Esegue la valutazione automatica dell'immobile e la salva
     */
    public Valutazione valutaImmobile(Immobile immobile) {
        Valutazione valutazione = new Valutazione();
        valutazione.setImmobile(immobile);
        int prezzo = calcolaPrezzoAI(immobile);
        valutazione.setPrezzoAI(prezzo);
        valutazione.setDataValutazione(java.time.LocalDateTime.now(java.time.ZoneId.of("Europe/Rome")));
        return valutazioneJpaRepository.save(valutazione);
    }

    /**
     * Calcola il prezzo stimato: base 50.000€ + 1.000€ per mq
     */
    private int calcolaPrezzoAI(Immobile immobile) {
        int base = 50000;
        Integer metratura = immobile.getMetratura();
        int mq = metratura != null ? metratura : 0;
        return base + mq * 1000;
    }
}
