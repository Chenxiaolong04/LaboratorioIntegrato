package com.immobiliaris.demo.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.immobiliaris.demo.entity.Contratto;
import com.immobiliaris.demo.entity.Immobile;
import com.immobiliaris.demo.entity.User;
import com.immobiliaris.demo.entity.Valutazione;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PdfContrattoService {

    private static final Font FONT_TITLE = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
    private static final Font FONT_SUBTITLE = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static final Font FONT_NORMAL = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
    private static final Font FONT_SMALL = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);

    public byte[] generaContrattoPdf(Contratto contratto) throws Exception {
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        
        document.open();

        // Intestazione Azienda
        aggiungiIntestazione(document);
        
        // Titolo
        Paragraph titolo = new Paragraph("INCARICO DI MEDIAZIONE IN ESCLUSIVA\nPER LA VENDITA IMMOBILIARE", FONT_TITLE);
        titolo.setAlignment(Element.ALIGN_CENTER);
        titolo.setSpacingAfter(20);
        document.add(titolo);

        // Sezione 1: LE PARTI
        aggiungiSezioneParti(document, contratto);
        
        // Sezione 2: OGGETTO DELL'INCARICO
        aggiungiSezioneOggetto(document, contratto);
        
        // Sezione 3: CONDIZIONI DI VENDITA
        aggiungiSezioneCondizioni(document, contratto);
        
        // Sezione 4: DURATA DELL'INCARICO
        aggiungiSezioneDurata(document, contratto);
        
        // Sezione 5: COMPENSO (PROVVIGIONE)
        aggiungiSezioneCompenso(document, contratto);
        
        // Sezione 6: DICHIARAZIONI DEL MANDANTE
        aggiungiSezioneDichiarazioni(document, contratto);
        
        // Sezione 7: OBBLIGHI E CLAUSOLA PENALE
        aggiungiSezioneObblighi(document);
        
        // Sezione 8: TRATTAMENTO DATI PERSONALI
        aggiungiSezioneGdpr(document);
        
        // Firme
        aggiungiFirme(document, contratto);
        
        // Clausole Vessatorie
        aggiungiClausoleVessatorie(document);

        document.close();
        return baos.toByteArray();
    }

    private void aggiungiIntestazione(Document document) throws DocumentException {
        Paragraph intestazione = new Paragraph();
        intestazione.add(new Chunk("IMMOBILIARIS S.R.L.\n", new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD)));
        intestazione.add(new Chunk("Sede Legale: Corso Duca degli Abruzzi, 24 - 10129 Torino (TO)\n", FONT_SMALL));
        intestazione.add(new Chunk("P.IVA / C.F.: 12345678901 — Iscrizione REA: TO-123456\n", FONT_SMALL));
        intestazione.add(new Chunk("Tel: 011.555.555 — Email: info@immobiliaris.demo\n", FONT_SMALL));
        intestazione.setAlignment(Element.ALIGN_CENTER);
        intestazione.setSpacingAfter(20);
        document.add(intestazione);
        
        document.add(new Paragraph("_____________________________________________________________________________________"));
        document.add(Chunk.NEWLINE);
    }

    private void aggiungiSezioneParti(Document document, Contratto contratto) throws DocumentException {
        User proprietario = contratto.getUtente();
        User agente = contratto.getAgente();
        
        Paragraph sezione = new Paragraph();
        sezione.add(new Chunk("1. LE PARTI\n\n", FONT_SUBTITLE));
        sezione.add(new Chunk("Tra i sottoscritti:\n\n", FONT_NORMAL));
        
        sezione.add(new Chunk("LA PARTE VENDITRICE (di seguito \"Mandante\"):\n", FONT_NORMAL));
        sezione.add(new Chunk("Sig./Sig.ra: ", FONT_NORMAL));
        sezione.add(new Chunk(proprietario.getNome() + " " + proprietario.getCognome() + "\n", new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD)));
        sezione.add(new Chunk("Email: " + proprietario.getEmail() + "\n", FONT_NORMAL));
        sezione.add(new Chunk("Telefono: " + (proprietario.getTelefono() != null ? proprietario.getTelefono() : "N/A") + "\n\n", FONT_NORMAL));
        
        sezione.add(new Chunk("E\n\n", FONT_NORMAL));
        
        sezione.add(new Chunk("L'AGENZIA IMMOBILIARE (di seguito \"Agente\"):\n", FONT_NORMAL));
        sezione.add(new Chunk("IMMOBILIARIS S.R.L., in persona del Sig./Sig.ra ", FONT_NORMAL));
        sezione.add(new Chunk(agente.getNome() + " " + agente.getCognome() + "\n", new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD)));
        sezione.add(new Chunk("regolarmente iscritta al Ruolo Agenti di Affari in Mediazione presso la CCIAA di Torino.\n\n", FONT_NORMAL));
        
        sezione.setSpacingAfter(15);
        document.add(sezione);
    }

    private void aggiungiSezioneOggetto(Document document, Contratto contratto) throws DocumentException {
        Immobile immobile = contratto.getImmobile();
        
        Paragraph sezione = new Paragraph();
        sezione.add(new Chunk("2. OGGETTO DELL'INCARICO\n\n", FONT_SUBTITLE));
        sezione.add(new Chunk("Il Mandante conferisce all'Agente l'incarico IN ESCLUSIVA di promuovere la vendita dell'unità immobiliare sita in:\n\n", FONT_NORMAL));
        
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setSpacingAfter(10);
        
        aggiungiRigaTabella(table, "Comune:", immobile.getCitta() + " (Prov. " + immobile.getProvincia() + ")");
        aggiungiRigaTabella(table, "CAP:", immobile.getCap());
        aggiungiRigaTabella(table, "Via/Piazza:", immobile.getVia());
        aggiungiRigaTabella(table, "Piano:", immobile.getPiano() != null ? immobile.getPiano().toString() : "N/A");
        aggiungiRigaTabella(table, "Ascensore:", immobile.getAscensore() != null && immobile.getAscensore() ? "Sì" : "No");
        aggiungiRigaTabella(table, "Tipologia:", immobile.getTipologia());
        aggiungiRigaTabella(table, "Metratura:", immobile.getMetratura() + " mq");
        aggiungiRigaTabella(table, "Stanze:", immobile.getStanze() != null ? immobile.getStanze().toString() : "N/A");
        aggiungiRigaTabella(table, "Bagni:", immobile.getBagni() != null ? immobile.getBagni().toString() : "N/A");
        aggiungiRigaTabella(table, "Condizioni:", immobile.getCondizioni());
        
        sezione.add(table);
        
        Paragraph composizione = new Paragraph();
        composizione.add(new Chunk("Dotazioni: ", FONT_NORMAL));
        StringBuilder dotazioni = new StringBuilder();
        if (immobile.getBalcone() != null && immobile.getBalcone()) dotazioni.append("Balcone, ");
        if (immobile.getTerrazzo() != null && immobile.getTerrazzo()) dotazioni.append("Terrazzo, ");
        if (immobile.getCantina() != null && immobile.getCantina()) dotazioni.append("Cantina, ");
        if (immobile.getGarage() != null && immobile.getGarage()) dotazioni.append("Box Auto, ");
        if (immobile.getGiardino() != null && immobile.getGiardino()) dotazioni.append("Giardino, ");
        if (dotazioni.length() > 0) {
            dotazioni.setLength(dotazioni.length() - 2); // Rimuove ultima virgola
            composizione.add(new Chunk(dotazioni.toString() + "\n", FONT_NORMAL));
        } else {
            composizione.add(new Chunk("Nessuna dotazione aggiuntiva\n", FONT_NORMAL));
        }
        composizione.setSpacingAfter(15);
        sezione.add(composizione);
        
        document.add(sezione);
    }

    private void aggiungiSezioneCondizioni(Document document, Contratto contratto) throws DocumentException {
        Valutazione valutazione = contratto.getValutazione();
        Integer prezzoUmano = valutazione != null && valutazione.getPrezzoUmano() != null 
            ? valutazione.getPrezzoUmano() 
            : valutazione.getPrezzoAI();
        
        Paragraph sezione = new Paragraph();
        sezione.add(new Chunk("3. CONDIZIONI DI VENDITA\n\n", FONT_SUBTITLE));
        sezione.add(new Chunk("Il prezzo richiesto per la vendita è stabilito in: ", FONT_NORMAL));
        sezione.add(new Chunk("€ " + String.format("%,d", prezzoUmano) + "\n\n", new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD)));
        sezione.add(new Chunk("Il Mandante dichiara che l'immobile al momento del rogito notarile sarà libero da persone e cose.\n\n", FONT_NORMAL));
        sezione.setSpacingAfter(15);
        document.add(sezione);
    }

    private void aggiungiSezioneDurata(Document document, Contratto contratto) throws DocumentException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataInizio = contratto.getDataInizio() != null ? contratto.getDataInizio().format(formatter) : "___________";
        String dataFine = contratto.getDataFine() != null ? contratto.getDataFine().format(formatter) : "___________";
        
        Paragraph sezione = new Paragraph();
        sezione.add(new Chunk("4. DURATA DELL'INCARICO\n\n", FONT_SUBTITLE));
        sezione.add(new Chunk("Il presente incarico ha validità dal ", FONT_NORMAL));
        sezione.add(new Chunk(dataInizio, new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD)));
        sezione.add(new Chunk(" al ", FONT_NORMAL));
        sezione.add(new Chunk(dataFine + "\n", new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD)));
        sezione.add(new Chunk("Alla scadenza, l'incarico cesserà automaticamente senza bisogno di disdetta.\n\n", FONT_NORMAL));
        sezione.setSpacingAfter(15);
        document.add(sezione);
    }

    private void aggiungiSezioneCompenso(Document document, Contratto contratto) throws DocumentException {
        Double percentuale = contratto.getPercentualeCommissione() != null ? contratto.getPercentualeCommissione() : 3.0;
        
        Paragraph sezione = new Paragraph();
        sezione.add(new Chunk("5. COMPENSO (PROVVIGIONE)\n\n", FONT_SUBTITLE));
        sezione.add(new Chunk("In caso di conclusione dell'affare durante la durata dell'incarico, il Mandante riconosce all'Agente una provvigione pari al ", FONT_NORMAL));
        sezione.add(new Chunk(String.format("%.2f", percentuale) + "%", new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD)));
        sezione.add(new Chunk(" + IVA di legge sul prezzo di vendita accettato.\n\n", FONT_NORMAL));
        sezione.add(new Chunk("Il diritto alla provvigione matura alla comunicazione dell'avvenuta accettazione della proposta di acquisto. ", FONT_NORMAL));
        sezione.add(new Chunk("La provvigione sarà altresì dovuta in caso di vendita conclusa anche successivamente alla scadenza dell'incarico a soggetti presentati o segnalati dall'Agente durante il periodo di validità dello stesso.\n\n", FONT_NORMAL));
        sezione.setSpacingAfter(15);
        document.add(sezione);
    }

    private void aggiungiSezioneDichiarazioni(Document document, Contratto contratto) throws DocumentException {
        Paragraph sezione = new Paragraph();
        sezione.add(new Chunk("6. DICHIARAZIONI DEL MANDANTE (Stato dell'immobile)\n\n", FONT_SUBTITLE));
        sezione.add(new Chunk("Il Mandante, sotto la propria responsabilità, dichiara che l'immobile:\n\n", FONT_NORMAL));
        
        List list = new List(List.UNORDERED, 10);
        list.setListSymbol(new Chunk("• "));
        list.add(new ListItem("È di sua piena ed esclusiva proprietà (o ha titolo legale per vendere).", FONT_NORMAL));
        list.add(new ListItem("È in regola con le norme edilizie ed urbanistiche vigenti (Conformità Urbanistica).", FONT_NORMAL));
        list.add(new ListItem("È conforme allo stato di fatto catastale.", FONT_NORMAL));
        list.add(new ListItem("Gli impianti sono a norma o saranno adeguati prima del rogito.", FONT_NORMAL));
        list.add(new ListItem("L'Attestato Prestazione Energetica (APE) sarà prodotto a cura e spese del Mandante prima del rogito.", FONT_NORMAL));
        
        sezione.add(list);
        sezione.setSpacingAfter(15);
        document.add(sezione);
    }

    private void aggiungiSezioneObblighi(Document document) throws DocumentException {
        Paragraph sezione = new Paragraph();
        sezione.add(new Chunk("7. OBBLIGHI E CLAUSOLA PENALE\n\n", FONT_SUBTITLE));
        
        sezione.add(new Chunk("Obblighi del Mandante: ", new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD)));
        sezione.add(new Chunk("Durante il periodo di esclusiva, il Mandante si obbliga a non vendere direttamente l'immobile e a non conferire incarico ad altre agenzie.\n\n", FONT_NORMAL));
        
        sezione.add(new Chunk("Penale: ", new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD)));
        sezione.add(new Chunk("In caso di violazione dell'esclusiva, rifiuto di vendere al prezzo stabilito (o superiore) in presenza di una proposta conforme, o revoca anticipata dell'incarico, il Mandante verserà all'Agente una penale pari al 70% della provvigione pattuita calcolata sul prezzo di vendita indicato all'art. 3.\n\n", FONT_NORMAL));
        
        sezione.add(new Chunk("Obblighi dell'Agente: ", new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD)));
        sezione.add(new Chunk("L'Agente si impegna a promuovere la vendita con la diligenza professionale, pubblicizzando l'immobile sui propri canali, organizzando visite e tenendo informato il Mandante.\n\n", FONT_NORMAL));
        
        sezione.setSpacingAfter(15);
        document.add(sezione);
    }

    private void aggiungiSezioneGdpr(Document document) throws DocumentException {
        Paragraph sezione = new Paragraph();
        sezione.add(new Chunk("8. TRATTAMENTO DATI PERSONALI (GDPR)\n\n", FONT_SUBTITLE));
        sezione.add(new Chunk("Il Mandante autorizza l'Agente al trattamento dei propri dati personali ai sensi del Regolamento UE 2016/679 (GDPR) esclusivamente per le finalità connesse al presente incarico di mediazione immobiliare.\n\n", FONT_NORMAL));
        sezione.setSpacingAfter(20);
        document.add(sezione);
    }

    private void aggiungiFirme(Document document, Contratto contratto) throws DocumentException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String data = contratto.getDataInizio() != null ? contratto.getDataInizio().format(formatter) : "___________";
        
        Paragraph luogoData = new Paragraph("Luogo e Data: Torino, li " + data + "\n\n", FONT_NORMAL);
        luogoData.setSpacingBefore(20);
        document.add(luogoData);
        
        PdfPTable firmeTable = new PdfPTable(2);
        firmeTable.setWidthPercentage(100);
        firmeTable.setSpacingBefore(10);
        
        PdfPCell cellMandante = new PdfPCell();
        cellMandante.setBorder(Rectangle.NO_BORDER);
        Paragraph mandante = new Paragraph();
        mandante.add(new Chunk("IL MANDANTE\n", FONT_NORMAL));
        mandante.add(new Chunk("(Firma)\n\n\n", FONT_SMALL));
        mandante.add(new Chunk("_____________________________", FONT_NORMAL));
        cellMandante.addElement(mandante);
        
        PdfPCell cellAgente = new PdfPCell();
        cellAgente.setBorder(Rectangle.NO_BORDER);
        Paragraph agente = new Paragraph();
        agente.add(new Chunk("L'AGENTE\n", FONT_NORMAL));
        agente.add(new Chunk("(Timbro e Firma)\n\n\n", FONT_SMALL));
        agente.add(new Chunk("_____________________________", FONT_NORMAL));
        cellAgente.addElement(agente);
        
        firmeTable.addCell(cellMandante);
        firmeTable.addCell(cellAgente);
        
        document.add(firmeTable);
    }

    private void aggiungiClausoleVessatorie(Document document) throws DocumentException {
        document.newPage();
        
        Paragraph sezione = new Paragraph();
        sezione.add(new Chunk("APPROVAZIONE SPECIFICA CLAUSOLE VESSATORIE\n\n", FONT_SUBTITLE));
        sezione.add(new Chunk("Ai sensi e per gli effetti degli artt. 1341 e 1342 del Codice Civile, il Mandante dichiara di aver letto attentamente e di approvare specificamente le clausole contenute nei seguenti articoli:\n\n", FONT_NORMAL));
        
        List list = new List(List.UNORDERED, 10);
        list.setListSymbol(new Chunk("• "));
        list.add(new ListItem("Art. 4 (Durata dell'incarico)", FONT_NORMAL));
        list.add(new ListItem("Art. 5 (Provvigione post-scadenza)", FONT_NORMAL));
        list.add(new ListItem("Art. 7 (Esclusiva e Penale)", FONT_NORMAL));
        
        sezione.add(list);
        sezione.setSpacingAfter(30);
        document.add(sezione);
        
        Paragraph firma = new Paragraph();
        firma.add(new Chunk("IL MANDANTE\n", FONT_NORMAL));
        firma.add(new Chunk("(Firma per accettazione)\n\n\n", FONT_SMALL));
        firma.add(new Chunk("_____________________________\n\n\n", FONT_NORMAL));
        document.add(firma);
        
        document.add(new Paragraph("_____________________________________________________________________________________"));
        
        Paragraph fine = new Paragraph("\n(Fine del Documento)", new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC));
        fine.setAlignment(Element.ALIGN_CENTER);
        document.add(fine);
    }

    private void aggiungiRigaTabella(PdfPTable table, String label, String value) {
        PdfPCell cellLabel = new PdfPCell(new Phrase(label, new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD)));
        cellLabel.setBorder(Rectangle.NO_BORDER);
        cellLabel.setPaddingBottom(5);
        
        PdfPCell cellValue = new PdfPCell(new Phrase(value != null ? value : "N/A", FONT_NORMAL));
        cellValue.setBorder(Rectangle.NO_BORDER);
        cellValue.setPaddingBottom(5);
        
        table.addCell(cellLabel);
        table.addCell(cellValue);
    }
}
