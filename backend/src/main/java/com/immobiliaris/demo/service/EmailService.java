package com.immobiliaris.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
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
        String htmlContent = generaHtmlRecap(nomeProprietario, cognomeProprietario, immobile, valutazione)
            + "<div style='text-align:center;margin-top:30px;'><img src='cid:logoImage' style='max-width:200px;'/></div>"
            + "<div style='text-align:center;margin-top:30px;'><img src='cid:logoImage' style='max-width:200px;'/></div>";

        // Invia mail HTML con immagine inline
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setFrom("xiao.chen@edu-its.it");
        helper.setTo(emailProprietario);
        helper.setSubject("📋 Riepilogo Valutazione Immobile - " + immobile.getVia());
        helper.setText(htmlContent, true); // true indica che è HTML

        // Allego immagine locale come inline (modifica il percorso se necessario)
        java.io.File imageFile = new java.io.File("backend/src/main/resources/static/logo.png");
        if (imageFile.exists()) {
            org.springframework.core.io.FileSystemResource image = new org.springframework.core.io.FileSystemResource(imageFile);
            helper.addInline("logoImage", image);
        }

        mailSender.send(mimeMessage);
    }
    
    /**
     * Genera il contenuto HTML della mail riepilogativa
     */
    private String generaHtmlRecap(String nome, String cognome, Immobile immobile, Valutazione valutazione) {
        return "<!DOCTYPE html>" +
               "<html lang='it'>" +
               "<head>" +
               "    <meta charset='UTF-8'>" +
               "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
               "    <style>" +
               "        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
               "        .container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
               "        .header { background-color: #2c3e50; color: white; padding: 20px; border-radius: 5px 5px 0 0; text-align: center; }" +
               "        .content { background-color: #ecf0f1; padding: 20px; }" +
               "        .section { background-color: white; padding: 15px; margin-bottom: 15px; border-left: 4px solid #3498db; }" +
               "        .section-title { font-size: 16px; font-weight: bold; color: #2c3e50; margin-bottom: 10px; }" +
               "        .field { display: flex; justify-content: space-between; padding: 8px 0; border-bottom: 1px solid #ecf0f1; }" +
               "        .field:last-child { border-bottom: none; }" +
               "        .label { font-weight: bold; color: #555; }" +
               "        .value { color: #333; text-align: right; }" +
               "        .price { font-size: 24px; font-weight: bold; color: #27ae60; text-align: center; padding: 20px; }" +
               "        .footer { text-align: center; padding: 15px; color: #777; font-size: 12px; }" +
               "    </style>" +
               "</head>" +
               "<body>" +
               "    <div class='container'>" +
               "        <div class='header'>" +
               "            <h2>📋 Riepilogo Valutazione Immobile</h2>" +
               "        </div>" +
               "        <div class='content'>" +
               "            <p>Caro/a <strong>" + nome + " " + cognome + "</strong>,</p>" +
               "            <p>La tua proprietà è stata registrata nel nostro sistema e valutata. Ecco il riepilogo completo:</p>" +
               
               "            <div class='section'>" +
               "                <div class='section-title'>📍 Localizzazione</div>" +
               "                <div class='field'>" +
               "                    <span class='label'>Indirizzo:</span>" +
               "                    <span class='value'>" + (immobile.getVia() != null ? immobile.getVia() : "N/A") + "</span>" +
               "                </div>" +
               "                <div class='field'>" +
               "                    <span class='label'>Città:</span>" +
               "                    <span class='value'>" + (immobile.getCitta() != null ? immobile.getCitta() : "N/A") + "</span>" +
               "                </div>" +
               "                <div class='field'>" +
               "                    <span class='label'>CAP:</span>" +
               "                    <span class='value'>" + (immobile.getCap() != null ? immobile.getCap() : "N/A") + "</span>" +
               "                </div>" +
               "                <div class='field'>" +
               "                    <span class='label'>Provincia:</span>" +
               "                    <span class='value'>" + (immobile.getProvincia() != null ? immobile.getProvincia() : "N/A") + "</span>" +
               "                </div>" +
               "            </div>" +
               
               "            <div class='section'>" +
               "                <div class='section-title'>🏠 Caratteristiche</div>" +
               "                <div class='field'>" +
               "                    <span class='label'>Tipologia:</span>" +
               "                    <span class='value'>" + (immobile.getTipologia() != null ? immobile.getTipologia() : "N/A") + "</span>" +
               "                </div>" +
               "                <div class='field'>" +
               "                    <span class='label'>Metratura:</span>" +
               "                    <span class='value'>" + (immobile.getMetratura() != null ? immobile.getMetratura() + " mq" : "N/A") + "</span>" +
               "                </div>" +
               "                <div class='field'>" +
               "                    <span class='label'>Stanze:</span>" +
               "                    <span class='value'>" + (immobile.getStanze() != null ? immobile.getStanze() : "N/A") + "</span>" +
               "                </div>" +
               "                <div class='field'>" +
               "                    <span class='label'>Bagni:</span>" +
               "                    <span class='value'>" + (immobile.getBagni() != null ? immobile.getBagni() : "N/A") + "</span>" +
               "                </div>" +
               "                <div class='field'>" +
               "                    <span class='label'>Piano:</span>" +
               "                    <span class='value'>" + (immobile.getPiano() != null ? immobile.getPiano() : "N/A") + "</span>" +
               "                </div>" +
               "                <div class='field'>" +
               "                    <span class='label'>Condizioni:</span>" +
               "                    <span class='value'>" + (immobile.getCondizioni() != null ? immobile.getCondizioni() : "N/A") + "</span>" +
               "                </div>" +
               "                <div class='field'>" +
               "                    <span class='label'>Riscaldamento:</span>" +
               "                    <span class='value'>" + (immobile.getRiscaldamento() != null ? immobile.getRiscaldamento() : "N/A") + "</span>" +
               "                </div>" +
               "            </div>" +
               
               "            <div class='section'>" +
               "                <div class='section-title'>✨ Dotazioni</div>" +
               "                <div class='field'>" +
               "                    <span class='label'>Ascensore:</span>" +
               "                    <span class='value'>" + (immobile.getAscensore() != null && immobile.getAscensore() ? "✅ Sì" : "❌ No") + "</span>" +
               "                </div>" +
               "                <div class='field'>" +
               "                    <span class='label'>Garage:</span>" +
               "                    <span class='value'>" + (immobile.getGarage() != null && immobile.getGarage() ? "✅ Sì" : "❌ No") + "</span>" +
               "                </div>" +
               "                <div class='field'>" +
               "                    <span class='label'>Giardino:</span>" +
               "                    <span class='value'>" + (immobile.getGiardino() != null && immobile.getGiardino() ? "✅ Sì" : "❌ No") + "</span>" +
               "                </div>" +
               "                <div class='field'>" +
               "                    <span class='label'>Balcone:</span>" +
               "                    <span class='value'>" + (immobile.getBalcone() != null && immobile.getBalcone() ? "✅ Sì" : "❌ No") + "</span>" +
               "                </div>" +
               "                <div class='field'>" +
               "                    <span class='label'>Terrazzo:</span>" +
               "                    <span class='value'>" + (immobile.getTerrazzo() != null && immobile.getTerrazzo() ? "✅ Sì" : "❌ No") + "</span>" +
               "                </div>" +
               "                <div class='field'>" +
               "                    <span class='label'>Cantina:</span>" +
               "                    <span class='value'>" + (immobile.getCantina() != null && immobile.getCantina() ? "✅ Sì" : "❌ No") + "</span>" +
               "                </div>" +
               "            </div>" +
               
               "            <div class='section'>" +
               "                <div class='section-title'>💰 Valutazione AI</div>" +
               "                <div class='price'>€ " + String.format("%,d", valutazione.getPrezzoAI()).replace(",", ".") + "</div>" +
               "                <p style='text-align: center; color: #777; font-size: 12px;'>Prezzo stimato dal sistema IA</p>" +
               "            </div>" +
               
               "        </div>" +
               "        <div class='footer'>" +
               "            <p>Questo è un messaggio automatico dal sistema di valutazione immobili.</p>" +
               "            <p>© 2025 Immobiliaris - Tutti i diritti riservati</p>" +
               "        </div>" +
               "    </div>" +
               "</body>" +
               "</html>";
    }
}
