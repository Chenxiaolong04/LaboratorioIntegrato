package com.immobiliaris.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ByteArrayResource;
import com.immobiliaris.demo.entity.Valutazione;
import com.immobiliaris.demo.entity.Immobile;
import com.immobiliaris.demo.entity.Contratto;
import com.immobiliaris.demo.repository.ValutazioneJpaRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ValutazioneJpaRepository valutazioneJpaRepository;

    public void send(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("xiao.chen@edu-its.it");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }

    /**
     * Invia mail HTML riepilogativa della valutazione immobile al proprietario
     *
     * @param idValutazione ID della valutazione
     * @throws MessagingException se errore invio mail
     */
    public void sendValutazioneRecap(Integer idValutazione) throws MessagingException {
        // Recupera la valutazione e l'immobile collegato
        Valutazione valutazione = valutazioneJpaRepository.findById(idValutazione)
                .orElseThrow(() -> new RuntimeException("Valutazione non trovata: " + idValutazione));

        Immobile immobile = valutazione.getImmobile();
        if (immobile == null) {
            throw new RuntimeException("Immobile non associato alla valutazione");
        }

        // Recupera email e nome proprietario
        String emailProprietario = immobile.getProprietario().getEmail();
        String nomeProprietario = immobile.getProprietario().getNome();
        String cognomeProprietario = immobile.getProprietario().getCognome();

        // Genera HTML della mail (con <img src='cid:logoImage'/>)
        String htmlContent = generaHtmlRecap(nomeProprietario, cognomeProprietario, immobile, valutazione);

        // Invia mail HTML con immagine inline
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setFrom("xiao.chen@edu-its.it");
        helper.setTo(emailProprietario);
        helper.setSubject("📋 Riepilogo Valutazione Immobile - " + immobile.getVia());
        helper.setText(htmlContent, true);

        ClassPathResource image = new ClassPathResource("static/logo.png");
        helper.addInline("logoImage", image);

        mailSender.send(mimeMessage);
    }

    /**
     * Genera il contenuto HTML della mail riepilogativa
     */
    private String generaHtmlRecap(String nome, String cognome, Immobile immobile, Valutazione valutazione) {
        return "<!DOCTYPE html>"
                + "<html lang='it'>"
                + "<head>"
                + "<meta charset='UTF-8' />"
                + "<meta name='viewport' content='width=device-width, initial-scale=1.0' />"
                + "<style>"
                + "body { font-family: Arial, sans-serif; background-color: #f3f6fa; margin:0; padding:0; color:#1f2d3d; }"
                + ".container { margin: 0 auto; padding: 10px; max-width:650px; }"
                + ".card { background:#ffffff; border-radius:12px; padding:20px; }"
                + ".section-title { font-size:20px; color:#21305d; margin:10px 0; font-weight:bold; }"
                + ".field { margin-bottom:6px; }"
                + ".field-label { font-weight:bold; color:#1f2d3d; }"
                + ".field-value { color:#475466; margin-left:4px; }"
                + ".spese-box { background:#ffffff; border-radius:12px; padding:0 20px; }"
                + ".totale-box { background:#1e3a56; color:white; padding:12px; border-radius:10px; text-align:center; margin-top:10px; font-weight:bold; }"
                + ".domande { text-align:center; margin-top:40px; }"
                + ".domande-title { font-weight:900; font-size:22px; }"
                + ".domande p { color:#4a5568; font-size:14px; margin-top:5px; }"
                + ".footer { text-align:center; font-size:11px; color:#7d8a97; margin-top:10px; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='container'>"
                + "<div style='text-align:center; margin-bottom:10px;'>"
                + "<img src='cid:logoImage' width='50' style='display:block; margin:0 auto;' />"
                + "</div>"
                + "<div style='background:#c9e3f0; padding:12px 28px; border-radius:10px; font-size:20px; font-weight:bold; margin:0 auto; text-align:center;'>"
                + "Proposta di valutazione"
                + "</div>"
                + "<div class='card'>"
                + "<p>Caro/a <strong>" + nome + " " + cognome + "</strong>,</p>"
                + "<p>Grazie per aver scelto il nostro servizio di valutazione immobiliare. Ecco i dettagli completi del tuo immobile.</p>"
                + "<hr style='border:0; border-top:4px solid #1e3a56;' />"
                + "<h3 class='section-title'>I dettagli del tuo immobile</h3>"
                + "<h3 class='section-title'>Indirizzo</h3>"
                + "<div class='field'><span class='field-label'>Via:</span><span class='field-value'>" + (immobile.getVia() != null ? immobile.getVia() : "N/A") + "</span></div>"
                + "<div class='field'><span class='field-label'>Città:</span><span class='field-value'>" + (immobile.getCitta() != null ? immobile.getCitta() : "N/A") + "</span></div>"
                + "<div class='field'><span class='field-label'>CAP:</span><span class='field-value'>" + (immobile.getCap() != null ? immobile.getCap() : "N/A") + "</span></div>"
                + "<div class='field'><span class='field-label'>Provincia:</span><span class='field-value'>" + (immobile.getProvincia() != null ? immobile.getProvincia() : "N/A") + "</span></div>"
                + "<hr />"
                + "<h3 class='section-title'>Caratteristiche dell'immobile</h3>"
                + "<div class='field'><span class='field-label'>Tipologia:</span><span class='field-value'>" + (immobile.getTipologia() != null ? immobile.getTipologia() : "N/A") + "</span></div>"
                + "<div class='field'><span class='field-label'>Metratura:</span><span class='field-value'>" + (immobile.getMetratura() != null ? immobile.getMetratura() + " mq" : "N/A") + "</span></div>"
                + "<div class='field'><span class='field-label'>Condizioni:</span><span class='field-value'>" + (immobile.getCondizioni() != null ? immobile.getCondizioni() : "N/A") + "</span></div>"
                + "<hr />"
                + "<h3 class='section-title'>Dettagli tecnici</h3>"
                + "<div class='field'><span class='field-label'>Stanze:</span><span class='field-value'>" + (immobile.getStanze() != null ? immobile.getStanze() : "N/A") + "</span></div>"
                + "<div class='field'><span class='field-label'>Bagni:</span><span class='field-value'>" + (immobile.getBagni() != null ? immobile.getBagni() : "N/A") + "</span></div>"
                + "<div class='field'><span class='field-label'>Riscaldamento:</span><span class='field-value'>" + (immobile.getRiscaldamento() != null ? immobile.getRiscaldamento() : "N/A") + "</span></div>"
                + "<div class='field'><span class='field-label'>Piano:</span><span class='field-value'>" + (immobile.getPiano() != null ? immobile.getPiano() : "N/A") + "</span></div>"
                + "<hr />"
                + "<h3 class='section-title'>Plus</h3>"
                + "<div class='field'><span class='field-label'>Ascensore:</span><span class='field-value'>" + (immobile.getAscensore() != null && immobile.getAscensore() ? "Sì" : "No") + "</span></div>"
                + "<div class='field'><span class='field-label'>Garage:</span><span class='field-value'>" + (immobile.getGarage() != null && immobile.getGarage() ? "Sì" : "No") + "</span></div>"
                + "<div class='field'><span class='field-label'>Giardino:</span><span class='field-value'>" + (immobile.getGiardino() != null && immobile.getGiardino() ? "Sì" : "No") + "</span></div>"
                + "<div class='field'><span class='field-label'>Balcone:</span><span class='field-value'>" + (immobile.getBalcone() != null && immobile.getBalcone() ? "Sì" : "No") + "</span></div>"
                + "<div class='field'><span class='field-label'>Terrazzo:</span><span class='field-value'>" + (immobile.getTerrazzo() != null && immobile.getTerrazzo() ? "Sì" : "No") + "</span></div>"
                + "<div class='field'><span class='field-label'>Cantina:</span><span class='field-value'>" + (immobile.getCantina() != null && immobile.getCantina() ? "Sì" : "No") + "</span></div>"
                + "<hr />"
                + "<h3 class='section-title'>Altri dettagli</h3>"
                + "<div class='field'><span class='field-label'>Descrizione:</span><span class='field-value'>" + (immobile.getDescrizione() != null ? immobile.getDescrizione() : "N/A") + "</span></div>"
                + "<div class='field'><span class='field-label'>Data inserimento:</span><span class='field-value'>" + formatDataOra(immobile.getDataRegistrazione()) + "</span></div>"
                + "<div style='display:block; margin:15px auto; padding:12px 20px; background:#1e3a56; color:#fff; text-align:center; border-radius:10px; font-weight:bold; font-size:18px;'>"
                + "Valutazione stimata: " + (valutazione.getPrezzoAI() != null ? valutazione.getPrezzoAI() : "N/A") + "€"
                + "</div>"
                + "</div>"
                + "<div class='spese-box'>"
                + "<h3 class='section-title'>Spese extra</h3>"
                + "<p>Costi utenze: 300€ - 800€ (luce, gas, acqua - volture/nuovi allacci)</p>"
                + "<p>Onorario notaio: 2000€ - 4000€</p>"
                + "<p>Onorario geometra/tecnico: fino a 1500€</p>"
                + "<p>Altre spese minori: 200€ - 500€ (bolli, visure ecc.)</p>"
                + "<div class='totale-box'><h2>Totale stimato (range): 3000€ - 6300€</h2><h3>in linea con la stima iniziale</h3></div>"
                + "</div>"
                + "<div class='domande'><h2 class='domande-title'>HAI DOMANDE?</h2>"
                + "<p>Entro 72 ore verrai contattato da un nostro agente immobiliare dedicato, che ti fornirà una valutazione completa e ti accompagnerà in ogni passo del processo.</p></div>"
                + "<hr />"
                + "<div style='text-align:center; margin:10px 0;'><img src='cid:logoImage' width='50' style='display:block; margin:0 auto;' /></div>"
                + "<div class='footer'>© 2025 Immobiliaris — Tutti i diritti riservati</div>"
                + "</div></body></html>";
    }

    /**
     * Invia contratto PDF via email al proprietario e all'agente
     *
     * @param contratto Contratto da inviare
     * @param pdfBytes Contenuto PDF in byte array
     * @throws MessagingException se errore invio mail
     */
    public void sendContrattoPdf(Contratto contratto, byte[] pdfBytes) throws MessagingException {
        Immobile immobile = contratto.getImmobile();
        String emailProprietario = contratto.getUtente().getEmail();
        String nomeProprietario = contratto.getUtente().getNome() + " " + contratto.getUtente().getCognome();
        String emailAgente = contratto.getAgente().getEmail();
        String nomeAgente = contratto.getAgente().getNome() + " " + contratto.getAgente().getCognome();
        
        // Genera nome file
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dataStr = contratto.getDataInizio() != null 
            ? contratto.getDataInizio().format(formatter) 
            : "senza_data";
        String nomeFile = "Contratto_" + contratto.getId() + "_" + dataStr + ".pdf";
        
        // HTML per il proprietario
        String htmlProprietario = generaHtmlContratto(
            nomeProprietario, 
            immobile.getVia() + ", " + immobile.getCitta(),
            "Gentile " + nomeProprietario,
            "In allegato trova il contratto di mediazione in esclusiva per la vendita del suo immobile sito in " + 
            immobile.getVia() + ", " + immobile.getCitta() + ".\n\n" +
            "Il contratto è stato generato in data odierna e contiene tutti i dettagli dell'incarico conferito alla nostra agenzia.\n\n" +
            "La preghiamo di leggerlo attentamente e di contattarci per qualsiasi chiarimento."
        );
        
        // HTML per l'agente
        String htmlAgente = generaHtmlContratto(
            nomeAgente,
            immobile.getVia() + ", " + immobile.getCitta(),
            "Gentile " + nomeAgente,
            "In allegato trova copia del contratto di mediazione in esclusiva per l'immobile sito in " +
            immobile.getVia() + ", " + immobile.getCitta() + ".\n\n" +
            "Il contratto è stato generato e inviato al proprietario " + nomeProprietario + ".\n\n" +
            "Potrà procedere con le attività di promozione e vendita dell'immobile."
        );
        
        // Invia al proprietario
        inviaEmailConAllegato(
            emailProprietario,
            "📄 Contratto di Mediazione Immobiliare - " + immobile.getVia(),
            htmlProprietario,
            nomeFile,
            pdfBytes
        );
        
        // Invia all'agente
        inviaEmailConAllegato(
            emailAgente,
            "📄 [COPIA] Contratto di Mediazione - " + immobile.getVia(),
            htmlAgente,
            nomeFile,
            pdfBytes
        );
    }
    
    /**
     * Metodo helper per inviare email con allegato PDF
     */
    private void inviaEmailConAllegato(String destinatario, String oggetto, String htmlContent, 
                                       String nomeFile, byte[] pdfBytes) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        
        helper.setFrom("xiao.chen@edu-its.it");
        helper.setTo(destinatario);
        helper.setSubject(oggetto);
        helper.setText(htmlContent, true);
        
        // Aggiungi logo inline
        ClassPathResource image = new ClassPathResource("static/logo.png");
        helper.addInline("logoImage", image);
        
        // Aggiungi PDF come allegato
        helper.addAttachment(nomeFile, new ByteArrayResource(pdfBytes));
        
        mailSender.send(mimeMessage);
    }
    
    /**
     * Genera HTML per email contratto
     */
    private String generaHtmlContratto(String destinatario, String indirizzo, 
                                       String saluto, String messaggio) {
        return "<!DOCTYPE html>"
                + "<html lang='it'>"
                + "<head>"
                + "<meta charset='UTF-8' />"
                + "<style>"
                + "body { font-family: Arial, sans-serif; background-color: #f3f6fa; margin:0; padding:0; }"
                + ".container { max-width:600px; margin:40px auto; background:#fff; border-radius:12px; padding:30px; box-shadow:0 2px 10px rgba(0,0,0,0.1); }"
                + ".header { text-align:center; margin-bottom:30px; }"
                + ".title { color:#1e3a56; font-size:24px; font-weight:bold; margin:15px 0; }"
                + ".content { color:#475466; font-size:15px; line-height:1.8; white-space:pre-line; }"
                + ".highlight { background:#e3f2fd; padding:15px; border-radius:8px; margin:20px 0; border-left:4px solid #1e3a56; }"
                + ".footer { text-align:center; margin-top:30px; padding-top:20px; border-top:2px solid #e0e0e0; color:#7d8a97; font-size:12px; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='container'>"
                + "<div class='header'>"
                + "<img src='cid:logoImage' width='60' />"
                + "<div class='title'>IMMOBILIARIS S.R.L.</div>"
                + "</div>"
                + "<div class='content'>"
                + "<p>" + saluto + ",</p>"
                + "<div class='highlight'>"
                + "<strong>Oggetto:</strong> Contratto di Mediazione Immobiliare<br>"
                + "<strong>Immobile:</strong> " + indirizzo
                + "</div>"
                + "<p>" + messaggio + "</p>"
                + "<p style='margin-top:25px;'>Cordiali saluti,<br><strong>Il Team IMMOBILIARIS</strong></p>"
                + "</div>"
                + "<div class='footer'>"
                + "<img src='cid:logoImage' width='40' style='margin-bottom:10px;' /><br>"
                + "IMMOBILIARIS S.R.L. - Corso Duca degli Abruzzi, 24 - 10129 Torino<br>"
                + "Tel: 011.555.555 - Email: info@immobiliaris.demo<br>"
                + "© 2025 Immobiliaris - Tutti i diritti riservati"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
    }

    /**
     * Formatta la data/ora nel formato dd-MM-yyyy HH:mm:ss
     */
    private String formatDataOra(java.time.LocalDateTime dateTime) {
        if (dateTime == null) {
            return "N/A";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return dateTime.format(formatter);
    }

}