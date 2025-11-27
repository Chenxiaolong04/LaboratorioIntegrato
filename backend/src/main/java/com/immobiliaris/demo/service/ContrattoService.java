
package com.immobiliaris.demo.service;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.immobiliaris.demo.entity.Contratto;
import com.immobiliaris.demo.repository.ContrattoJpaRepository;
import java.util.List;

@Service
public class ContrattoService {

    @Autowired
    private ContrattoJpaRepository contrattoJpaRepository;

    // Recupera contratto per ID
    public Contratto getContrattoById(Integer id) {
        return contrattoJpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contratto non trovato"));
    }

    // Lista tutti i contratti
    public List<Contratto> getAllContratti() {
        return contrattoJpaRepository.findAll();
    }

    // Salva contratto
    public Contratto saveContratto(Contratto contratto) {
        return contrattoJpaRepository.save(contratto);
    }

    // Genera HTML del contratto
    public String generaHTMLContratto(Contratto contratto) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; margin: 40px; }");
        html.append("h1 { text-align: center; color: #333; }");
        html.append("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
        html.append("td { border: 1px solid #ddd; padding: 10px; }");
        html.append(".label { font-weight: bold; width: 30%; }");
        html.append(".footer { text-align: center; margin-top: 40px; font-size: 12px; color: #666; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");

        // Titolo
        html.append("<h1>CONTRATTO IMMOBILIARE</h1>");
        html.append("<p style='text-align: center;'>Numero: ").append(contratto.getNumeroContratto()).append("</p>");

        // Dati principali
        html.append("<table>");
        html.append("<tr><td class='label'>Numero Contratto:</td><td>").append(contratto.getNumeroContratto()).append("</td></tr>");
        html.append("<tr><td class='label'>Data Inizio:</td><td>").append(contratto.getDataInizio()).append("</td></tr>");
        html.append("<tr><td class='label'>Data Fine:</td><td>").append(contratto.getDataFine()).append("</td></tr>");
        html.append("<tr><td class='label'>Commissione:</td><td>").append(contratto.getPercentualeCommissione()).append("%</td></tr>");

        // Dati Stato
        if(contratto.getStatoContratto() != null) {
            html.append("<tr><td class='label'>Stato:</td><td>").append(contratto.getStatoContratto().getNome()).append("</td></tr>");
        }

        // Dati Immobile
        if(contratto.getImmobile() != null) {
            html.append("<tr><td class='label'>Indirizzo Immobile:</td><td>")
                .append(contratto.getImmobile().getVia()).append(", ")
                .append(contratto.getImmobile().getCitta()).append(" ")
                .append(contratto.getImmobile().getCap()).append(" (")
                .append(contratto.getImmobile().getProvincia()).append(")")
                .append("</td></tr>");
        }

        // Dati Agente
        if(contratto.getAgente() != null) {
            html.append("<tr><td class='label'>Agente:</td><td>")
                .append(contratto.getAgente().getNome()).append(" ")
                .append(contratto.getAgente().getCognome()).append("</td></tr>");
        }

        // Dati Cliente
        if(contratto.getUtente() != null) {
            html.append("<tr><td class='label'>Cliente:</td><td>")
                .append(contratto.getUtente().getNome()).append(" ")
                .append(contratto.getUtente().getCognome()).append("</td></tr>");
        }

        html.append("</table>");

        // Footer
        html.append("<div class='footer'>");
        html.append("<p>Documento generato automaticamente da Immobiliaris</p>");
        html.append("<p>Data generazione: ").append(java.time.LocalDate.now()).append("</p>");
        html.append("</div>");

        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }

    // Converti HTML a PDF
    public byte[] generaPDF(String html) throws Exception {
        try (java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(outputStream);
            builder.run();
            return outputStream.toByteArray();
        }
    }
}