package com.immobiliaris.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.immobiliaris.demo.entity.Valutazione;
import com.immobiliaris.demo.entity.Immobile;
import com.immobiliaris.demo.repository.ValutazioneJpaRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

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
                + "<div class='field'><span class='field-label'>Prezzo:</span><span class='field-value'>" + (immobile.getPrezzo() != null ? immobile.getPrezzo() : "N/A") + "€</span></div>"
                + "<div class='field'><span class='field-label'>Data inserimento:</span><span class='field-value'>" + (immobile.getDataRegistrazione() != null ? immobile.getDataRegistrazione() : "N/A") + "</span></div>"
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

}