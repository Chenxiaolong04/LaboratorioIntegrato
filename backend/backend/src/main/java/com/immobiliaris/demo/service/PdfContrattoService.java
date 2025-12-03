package com.immobiliaris.demo.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.immobiliaris.demo.entity.Contratto;
import com.immobiliaris.demo.entity.Immobile;
import com.immobiliaris.demo.entity.User;
import com.immobiliaris.demo.entity.Valutazione;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PdfContrattoService {

    private static final Font FONT_TITLE = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
    private static final Font FONT_SUBTITLE = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD);
    private static final Font FONT_NORMAL = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
    private static final Font FONT_SMALL = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL);
    private static final Font FONT_COMPANY = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.DARK_GRAY);
    private static final BaseColor COLOR_HEADER = new BaseColor(41, 128, 185); // Blu professionale

    public byte[] generaContrattoPdf(Contratto contratto) throws Exception {
        Document document = new Document(PageSize.A4, 60, 60, 60, 60);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        
        document.open();

        // Intestazione Azienda
        aggiungiIntestazione(document);
        
        // Titolo
        Paragraph titolo = new Paragraph("INCARICO DI MEDIAZIONE IN ESCLUSIVA\nPER LA VENDITA IMMOBILIARE", FONT_TITLE);
        titolo.setAlignment(Element.ALIGN_CENTER);
        titolo.setSpacingBefore(10);
        titolo.setSpacingAfter(25);
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
        intestazione.add(new Chunk("IMMOBILIARIS S.R.L.\n", FONT_COMPANY));
        intestazione.add(new Chunk("Sede Legale: Corso Duca degli Abruzzi, 24 - 10129 Torino (TO)\n", FONT_SMALL));
        intestazione.add(new Chunk("P.IVA / C.F.: 12345678901 — Iscrizione REA: TO-123456\n", FONT_SMALL));
        intestazione.add(new Chunk("Tel: 011.555.555 — Email: info@immobiliaris.demo\n", FONT_SMALL));
        intestazione.setAlignment(Element.ALIGN_CENTER);
        intestazione.setSpacingAfter(15);
        document.add(intestazione);
        
        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(COLOR_HEADER);
        lineSeparator.setLineWidth(2);
        Chunk linebreak = new Chunk(lineSeparator);
        document.add(linebreak);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
    }

    private void aggiungiSezioneParti(Document document, Contratto contratto) throws DocumentException {
        User proprietario = contratto.getUtente();
        User agente = contratto.getAgente();
        
        Paragraph titolo = new Paragraph("1. LE PARTI\n", FONT_SUBTITLE);
        titolo.setSpacingAfter(10);
        document.add(titolo);
        
        Paragraph intro = new Paragraph("Tra i sottoscritti:\n", FONT_NORMAL);
        intro.setSpacingAfter(12);
        intro.setAlignment(Element.ALIGN_CENTER);
        document.add(intro);
        
        PdfPTable partiTable = new PdfPTable(1);
        partiTable.setWidthPercentage(90);
        partiTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        partiTable.setSpacingBefore(5);
        partiTable.setSpacingAfter(18);
        
        // Mandante
        PdfPCell cellMandante = new PdfPCell();
        cellMandante.setBorder(Rectangle.BOX);
        cellMandante.setBorderColor(COLOR_HEADER);
        cellMandante.setBorderWidth(1);
        cellMandante.setPadding(12);
        cellMandante.setBackgroundColor(new BaseColor(245, 248, 250));
        
        Paragraph mandante = new Paragraph();
        mandante.add(new Chunk("LA PARTE VENDITRICE (di seguito \"Mandante\")\n", new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD)));
        mandante.add(new Chunk("\nSig./Sig.ra: ", FONT_NORMAL));
        mandante.add(new Chunk(proprietario.getNome() + " " + proprietario.getCognome() + "\n", new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD)));
        mandante.add(new Chunk("Email: " + proprietario.getEmail() + "\n", FONT_NORMAL));
        mandante.add(new Chunk("Telefono: " + (proprietario.getTelefono() != null ? proprietario.getTelefono() : "N/A"), FONT_NORMAL));
        mandante.setAlignment(Element.ALIGN_CENTER);
        cellMandante.addElement(mandante);
        partiTable.addCell(cellMandante);
        
        document.add(partiTable);
        
        Paragraph separatore = new Paragraph("E\n", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD));
        separatore.setAlignment(Element.ALIGN_CENTER);
        separatore.setSpacingBefore(10);
        separatore.setSpacingAfter(10);
        document.add(separatore);
        
        // Agente
        PdfPTable agenteTable = new PdfPTable(1);
        agenteTable.setWidthPercentage(90);
        agenteTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        agenteTable.setSpacingAfter(18);
        
        PdfPCell cellAgente = new PdfPCell();
        cellAgente.setBorder(Rectangle.BOX);
        cellAgente.setBorderColor(COLOR_HEADER);
        cellAgente.setBorderWidth(1);
        cellAgente.setPadding(12);
        cellAgente.setBackgroundColor(new BaseColor(245, 248, 250));
        
        Paragraph agenteP = new Paragraph();
        agenteP.add(new Chunk("L'AGENZIA IMMOBILIARE (di seguito \"Agente\")\n", new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD)));
        agenteP.add(new Chunk("\nIMMOBILIARIS S.R.L., in persona del Sig./Sig.ra ", FONT_NORMAL));
        agenteP.add(new Chunk(agente.getNome() + " " + agente.getCognome() + "\n", new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD)));
        agenteP.add(new Chunk("regolarmente iscritta al Ruolo Agenti di Affari in Mediazione presso la CCIAA di Torino.", FONT_NORMAL));
        agenteP.setAlignment(Element.ALIGN_CENTER);
        cellAgente.addElement(agenteP);
        agenteTable.addCell(cellAgente);
        
        document.add(agenteTable);
    }

    private void aggiungiSezioneOggetto(Document document, Contratto contratto) throws DocumentException {
        Immobile immobile = contratto.getImmobile();
        
        Paragraph titolo = new Paragraph("2. OGGETTO DELL'INCARICO\n", FONT_SUBTITLE);
        titolo.setSpacingAfter(10);
        document.add(titolo);
        
        Paragraph intro = new Paragraph("Il Mandante conferisce all'Agente l'incarico IN ESCLUSIVA di promuovere la vendita dell'unità immobiliare sita in:\n", FONT_NORMAL);
        intro.setSpacingAfter(12);
        intro.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(intro);
        
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(85);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
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
        
        document.add(table);
        
        Paragraph composizione = new Paragraph();
        composizione.add(new Chunk("\nDotazioni: ", new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD)));
        StringBuilder dotazioni = new StringBuilder();
        if (immobile.getBalcone() != null && immobile.getBalcone()) dotazioni.append("Balcone, ");
        if (immobile.getTerrazzo() != null && immobile.getTerrazzo()) dotazioni.append("Terrazzo, ");
        if (immobile.getCantina() != null && immobile.getCantina()) dotazioni.append("Cantina, ");
        if (immobile.getGarage() != null && immobile.getGarage()) dotazioni.append("Box Auto, ");
        if (immobile.getGiardino() != null && immobile.getGiardino()) dotazioni.append("Giardino, ");
        if (dotazioni.length() > 0) {
            dotazioni.setLength(dotazioni.length() - 2);
            composizione.add(new Chunk(dotazioni.toString() + "\n", FONT_NORMAL));
        } else {
            composizione.add(new Chunk("Nessuna dotazione aggiuntiva\n", FONT_NORMAL));
        }
        composizione.setSpacingAfter(18);
        composizione.setAlignment(Element.ALIGN_CENTER);
        document.add(composizione);
    }

    private void aggiungiSezioneCondizioni(Document document, Contratto contratto) throws DocumentException {
        Valutazione valutazione = contratto.getValutazione();
        Integer prezzoUmano = valutazione != null && valutazione.getPrezzoUmano() != null 
            ? valutazione.getPrezzoUmano() 
            : valutazione.getPrezzoAI();
        
        Paragraph titolo = new Paragraph("3. CONDIZIONI DI VENDITA\n", FONT_SUBTITLE);
        titolo.setSpacingAfter(12);
        document.add(titolo);
        
        Paragraph prezzo = new Paragraph();
        prezzo.add(new Chunk("Il prezzo richiesto per la vendita è stabilito in:\n\n", FONT_NORMAL));
        prezzo.add(new Chunk("€ " + String.format("%,d", prezzoUmano) + "\n", new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, COLOR_HEADER)));
        prezzo.setAlignment(Element.ALIGN_CENTER);
        prezzo.setSpacingAfter(12);
        document.add(prezzo);
        
        Paragraph nota = new Paragraph("Il Mandante dichiara che l'immobile al momento del rogito notarile sarà libero da persone e cose.\n", FONT_NORMAL);
        nota.setAlignment(Element.ALIGN_JUSTIFIED);
        nota.setSpacingAfter(18);
        document.add(nota);
    }

    private void aggiungiSezioneDurata(Document document, Contratto contratto) throws DocumentException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataInizio = contratto.getDataInizio() != null ? contratto.getDataInizio().format(formatter) : "___________";
        String dataFine = contratto.getDataFine() != null ? contratto.getDataFine().format(formatter) : "___________";
        
        Paragraph titolo = new Paragraph("4. DURATA DELL'INCARICO\n", FONT_SUBTITLE);
        titolo.setSpacingAfter(12);
        document.add(titolo);
        
        Paragraph durata = new Paragraph();
        durata.add(new Chunk("Il presente incarico ha validità dal ", FONT_NORMAL));
        durata.add(new Chunk(dataInizio, new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD)));
        durata.add(new Chunk(" al ", FONT_NORMAL));
        durata.add(new Chunk(dataFine + "\n\n", new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD)));
        durata.add(new Chunk("Alla scadenza, l'incarico cesserà automaticamente senza bisogno di disdetta.", FONT_NORMAL));
        durata.setAlignment(Element.ALIGN_JUSTIFIED);
        durata.setSpacingAfter(18);
        document.add(durata);
    }

    private void aggiungiSezioneCompenso(Document document, Contratto contratto) throws DocumentException {
        Double percentuale = contratto.getPercentualeCommissione() != null ? contratto.getPercentualeCommissione() : 3.0;
        
        Paragraph titolo = new Paragraph("5. COMPENSO (PROVVIGIONE)\n", FONT_SUBTITLE);
        titolo.setSpacingAfter(12);
        document.add(titolo);
        
        Paragraph compenso = new Paragraph();
        compenso.add(new Chunk("In caso di conclusione dell'affare durante la durata dell'incarico, il Mandante riconosce all'Agente una provvigione pari al ", FONT_NORMAL));
        compenso.add(new Chunk(String.format("%.2f", percentuale) + "%", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, COLOR_HEADER)));
        compenso.add(new Chunk(" + IVA di legge sul prezzo di vendita accettato.\n\n", FONT_NORMAL));
        compenso.add(new Chunk("Il diritto alla provvigione matura alla comunicazione dell'avvenuta accettazione della proposta di acquisto. ", FONT_NORMAL));
        compenso.add(new Chunk("La provvigione sarà altresì dovuta in caso di vendita conclusa anche successivamente alla scadenza dell'incarico a soggetti presentati o segnalati dall'Agente durante il periodo di validità dello stesso.", FONT_NORMAL));
        compenso.setAlignment(Element.ALIGN_JUSTIFIED);
        compenso.setSpacingAfter(18);
        document.add(compenso);
    }

    private void aggiungiSezioneDichiarazioni(Document document, Contratto contratto) throws DocumentException {
        Paragraph titolo = new Paragraph("6. DICHIARAZIONI DEL MANDANTE (Stato dell'immobile)\n", FONT_SUBTITLE);
        titolo.setSpacingAfter(10);
        document.add(titolo);
        
        Paragraph intro = new Paragraph("Il Mandante, sotto la propria responsabilità, dichiara che l'immobile:\n", FONT_NORMAL);
        intro.setSpacingAfter(8);
        document.add(intro);
        
        List list = new List(List.UNORDERED, 12);
        list.setListSymbol(new Chunk("• "));
        list.setIndentationLeft(30);
        list.add(new ListItem("È di sua piena ed esclusiva proprietà (o ha titolo legale per vendere).", FONT_NORMAL));
        list.add(new ListItem("È in regola con le norme edilizie ed urbanistiche vigenti (Conformità Urbanistica).", FONT_NORMAL));
        list.add(new ListItem("È conforme allo stato di fatto catastale.", FONT_NORMAL));
        list.add(new ListItem("Gli impianti sono a norma o saranno adeguati prima del rogito.", FONT_NORMAL));
        list.add(new ListItem("L'Attestato Prestazione Energetica (APE) sarà prodotto a cura e spese del Mandante prima del rogito.", FONT_NORMAL));
        
        document.add(list);
        document.add(new Paragraph("\n"));
    }

    private void aggiungiSezioneObblighi(Document document) throws DocumentException {
        Paragraph titolo = new Paragraph("7. OBBLIGHI E CLAUSOLA PENALE\n", FONT_SUBTITLE);
        titolo.setSpacingAfter(12);
        document.add(titolo);
        
        Paragraph obbligoMandante = new Paragraph();
        obbligoMandante.add(new Chunk("Obblighi del Mandante: ", new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD)));
        obbligoMandante.add(new Chunk("Durante il periodo di esclusiva, il Mandante si obbliga a non vendere direttamente l'immobile e a non conferire incarico ad altre agenzie.", FONT_NORMAL));
        obbligoMandante.setAlignment(Element.ALIGN_JUSTIFIED);
        obbligoMandante.setSpacingAfter(10);
        document.add(obbligoMandante);
        
        Paragraph penale = new Paragraph();
        penale.add(new Chunk("Penale: ", new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD)));
        penale.add(new Chunk("In caso di violazione dell'esclusiva, rifiuto di vendere al prezzo stabilito (o superiore) in presenza di una proposta conforme, o revoca anticipata dell'incarico, il Mandante verserà all'Agente una penale pari al 70% della provvigione pattuita calcolata sul prezzo di vendita indicato all'art. 3.", FONT_NORMAL));
        penale.setAlignment(Element.ALIGN_JUSTIFIED);
        penale.setSpacingAfter(10);
        document.add(penale);
        
        Paragraph obbligoAgente = new Paragraph();
        obbligoAgente.add(new Chunk("Obblighi dell'Agente: ", new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD)));
        obbligoAgente.add(new Chunk("L'Agente si impegna a promuovere la vendita con la diligenza professionale, pubblicizzando l'immobile sui propri canali, organizzando visite e tenendo informato il Mandante.", FONT_NORMAL));
        obbligoAgente.setAlignment(Element.ALIGN_JUSTIFIED);
        obbligoAgente.setSpacingAfter(18);
        document.add(obbligoAgente);
    }

    private void aggiungiSezioneGdpr(Document document) throws DocumentException {
        Paragraph titolo = new Paragraph("8. TRATTAMENTO DATI PERSONALI (GDPR)\n", FONT_SUBTITLE);
        titolo.setSpacingAfter(10);
        document.add(titolo);
        
        Paragraph gdpr = new Paragraph("Il Mandante autorizza l'Agente al trattamento dei propri dati personali ai sensi del Regolamento UE 2016/679 (GDPR) esclusivamente per le finalità connesse al presente incarico di mediazione immobiliare.", FONT_NORMAL);
        gdpr.setAlignment(Element.ALIGN_JUSTIFIED);
        gdpr.setSpacingAfter(25);
        document.add(gdpr);
    }

    private void aggiungiFirme(Document document, Contratto contratto) throws DocumentException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String data = contratto.getDataInizio() != null ? contratto.getDataInizio().format(formatter) : "___________";
        
        Paragraph luogoData = new Paragraph("Luogo e Data: Torino, li " + data + "\n", new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD));
        luogoData.setSpacingBefore(25);
        luogoData.setSpacingAfter(20);
        luogoData.setAlignment(Element.ALIGN_CENTER);
        document.add(luogoData);
        
        PdfPTable firmeTable = new PdfPTable(2);
        firmeTable.setWidthPercentage(85);
        firmeTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        firmeTable.setSpacingBefore(15);
        
        PdfPCell cellMandante = new PdfPCell();
        cellMandante.setBorder(Rectangle.NO_BORDER);
        cellMandante.setPadding(10);
        Paragraph mandante = new Paragraph();
        mandante.add(new Chunk("IL MANDANTE\n", new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD)));
        mandante.add(new Chunk("(Firma)\n\n\n\n", FONT_SMALL));
        mandante.add(new Chunk("_________________________________", FONT_NORMAL));
        mandante.setAlignment(Element.ALIGN_CENTER);
        cellMandante.addElement(mandante);
        
        PdfPCell cellAgente = new PdfPCell();
        cellAgente.setBorder(Rectangle.NO_BORDER);
        cellAgente.setPadding(10);
        Paragraph agente = new Paragraph();
        agente.add(new Chunk("L'AGENTE\n", new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD)));
        agente.add(new Chunk("(Timbro e Firma)\n\n\n\n", FONT_SMALL));
        agente.add(new Chunk("_________________________________", FONT_NORMAL));
        agente.setAlignment(Element.ALIGN_CENTER);
        cellAgente.addElement(agente);
        
        firmeTable.addCell(cellMandante);
        firmeTable.addCell(cellAgente);
        
        document.add(firmeTable);
    }

    private void aggiungiClausoleVessatorie(Document document) throws DocumentException {
        document.newPage();
        
        Paragraph titolo = new Paragraph("APPROVAZIONE SPECIFICA CLAUSOLE VESSATORIE\n", FONT_SUBTITLE);
        titolo.setAlignment(Element.ALIGN_CENTER);
        titolo.setSpacingAfter(15);
        document.add(titolo);
        
        Paragraph intro = new Paragraph("Ai sensi e per gli effetti degli artt. 1341 e 1342 del Codice Civile, il Mandante dichiara di aver letto attentamente e di approvare specificamente le clausole contenute nei seguenti articoli:\n", FONT_NORMAL);
        intro.setAlignment(Element.ALIGN_JUSTIFIED);
        intro.setSpacingAfter(12);
        document.add(intro);
        
        List list = new List(List.UNORDERED, 12);
        list.setListSymbol(new Chunk("• "));
        list.setIndentationLeft(30);
        list.add(new ListItem("Art. 4 (Durata dell'incarico)", FONT_NORMAL));
        list.add(new ListItem("Art. 5 (Provvigione post-scadenza)", FONT_NORMAL));
        list.add(new ListItem("Art. 7 (Esclusiva e Penale)", FONT_NORMAL));
        
        document.add(list);
        document.add(new Paragraph("\n\n"));
        
        Paragraph firma = new Paragraph();
        firma.add(new Chunk("IL MANDANTE\n", new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD)));
        firma.add(new Chunk("(Firma per accettazione)\n\n\n\n", FONT_SMALL));
        firma.add(new Chunk("_________________________________", FONT_NORMAL));
        firma.setAlignment(Element.ALIGN_CENTER);
        firma.setSpacingAfter(40);
        document.add(firma);
        
        LineSeparator finalLine = new LineSeparator();
        finalLine.setLineColor(COLOR_HEADER);
        finalLine.setLineWidth(1);
        Chunk finalLinebreak = new Chunk(finalLine);
        document.add(finalLinebreak);
        
        Paragraph fine = new Paragraph("\n(Fine del Documento)", new Font(Font.FontFamily.HELVETICA, 9, Font.ITALIC, BaseColor.GRAY));
        fine.setAlignment(Element.ALIGN_CENTER);
        document.add(fine);
    }

    private void aggiungiRigaTabella(PdfPTable table, String label, String value) {
        PdfPCell cellLabel = new PdfPCell(new Phrase(label, new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD)));
        cellLabel.setBorder(Rectangle.BOTTOM);
        cellLabel.setBorderColor(new BaseColor(220, 220, 220));
        cellLabel.setBorderWidth(0.5f);
        cellLabel.setPadding(8);
        cellLabel.setBackgroundColor(new BaseColor(250, 250, 250));
        
        PdfPCell cellValue = new PdfPCell(new Phrase(value != null ? value : "N/A", FONT_NORMAL));
        cellValue.setBorder(Rectangle.BOTTOM);
        cellValue.setBorderColor(new BaseColor(220, 220, 220));
        cellValue.setBorderWidth(0.5f);
        cellValue.setPadding(8);
        
        table.addCell(cellLabel);
        table.addCell(cellValue);
    }
}
