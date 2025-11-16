package com.immobiliaris.demo.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.immobiliaris.demo.model.Property;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Invia email di conferma all'utente dopo la compilazione del form
     */
    public void sendConfirmationEmail(String to, String contactName, Property property) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Conferma ricezione richiesta immobile");
            message.setText(buildEmailContent(contactName, property));
            
            mailSender.send(message);
            
        } catch (Exception e) {
            // Log dell'errore ma non bloccare il salvataggio
            System.err.println("Errore invio email: " + e.getMessage());
        }
    }

    /**
     * Costruisce il contenuto dell'email di conferma
     */
    private String buildEmailContent(String contactName, Property property) {
        return String.format(
            "Gentile %s,\n\n" +
            "Grazie per aver compilato il form per la richiesta di valutazione del tuo immobile.\n\n" +
            "Dettagli immobile:\n" +
            "- Indirizzo: %s, %s (%s)\n" +
            "- Tipo: %s\n" +
            "- Metratura: %d mq\n" +
            "- Locali: %d\n" +
            "- Bagni: %d\n" +
            "- Piano: %d\n" +
            "- Ascensore: %s\n" +
            "- Garage: %s\n\n" +
            "Ti contatteremo presto per una valutazione dettagliata.\n\n" +
            "Cordiali saluti,\n" +
            "Team Immobiliaris",
            contactName,
            property.getStreet(),
            property.getCity(),
            property.getProvince(),
            property.getType(),
            property.getSquareMeters(),
            property.getRooms(),
            property.getBathrooms(),
            property.getFloor(),
            property.isElevator() ? "Sì" : "No",
            property.isGarage() ? "Sì" : "No"
        );
    }

    /**
     * Invia notifica interna all'admin/agente
     */
    public void sendInternalNotification(String adminEmail, Property property, String contactName, String contactEmail, String contactPhone) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(adminEmail);
            message.setSubject("Nuova richiesta valutazione immobile");
            message.setText(buildInternalNotificationContent(property, contactName, contactEmail, contactPhone));
            
            mailSender.send(message);
            
        } catch (Exception e) {
            System.err.println("Errore invio notifica interna: " + e.getMessage());
        }
    }

    /**
     * Costruisce il contenuto della notifica interna
     */
    private String buildInternalNotificationContent(Property property, String contactName, String contactEmail, String contactPhone) {
        return String.format(
            "Nuova richiesta di valutazione immobile ricevuta.\n\n" +
            "CONTATTO:\n" +
            "- Nome: %s\n" +
            "- Email: %s\n" +
            "- Telefono: %s\n\n" +
            "IMMOBILE:\n" +
            "- Indirizzo: %s, %s (%s)\n" +
            "- Tipo: %s\n" +
            "- Metratura: %d mq\n" +
            "- Locali: %d\n" +
            "- Bagni: %d\n" +
            "- Piano: %d\n" +
            "- Ascensore: %s\n" +
            "- Garage: %s\n" +
            "- Descrizione: %s\n\n" +
            "ID Immobile nel sistema: %d",
            contactName,
            contactEmail,
            contactPhone != null ? contactPhone : "Non fornito",
            property.getStreet(),
            property.getCity(),
            property.getProvince(),
            property.getType(),
            property.getSquareMeters(),
            property.getRooms(),
            property.getBathrooms(),
            property.getFloor(),
            property.isElevator() ? "Sì" : "No",
            property.isGarage() ? "Sì" : "No",
            property.getDescription() != null ? property.getDescription() : "Nessuna",
            property.getPropertyId()
        );
    }
}
