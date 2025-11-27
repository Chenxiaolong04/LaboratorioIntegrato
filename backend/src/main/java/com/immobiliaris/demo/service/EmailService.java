package com.immobiliaris.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
        // Invia email HTML
        public void sendHtml(String to, String subject, String htmlContent) {
            try {
                jakarta.mail.internet.MimeMessage message = mailSender.createMimeMessage();
                org.springframework.mail.javamail.MimeMessageHelper helper = new org.springframework.mail.javamail.MimeMessageHelper(message, false, "UTF-8");
                helper.setFrom("xiao.chen@edu-its.it");
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(htmlContent, true); // true = HTML
                mailSender.send(message);
            } catch (Exception e) {
                throw new RuntimeException("Errore nell'invio email HTML: " + e.getMessage());
            }
        }
    // Metodo compatibile con MailController
    public void send(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("xiao.chen@edu-its.it");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    @Autowired
    private JavaMailSender mailSender;

    // ...altri metodi (inviaEmail, inviaEmailConAllegato) se servono...
}
