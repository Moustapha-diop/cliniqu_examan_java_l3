package com.example.clinique.utilitaire;

import com.example.clinique.model.Consultation;
import com.example.clinique.model.Facture;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class PDFGenerator {

    private static final String CLINIQUE_NOM = "Clinique Médicale Al Shifa";
    private static final String CLINIQUE_ADRESSE = "123 Rue de la Santé, Tunis 1000";
    private static final String CLINIQUE_TEL = "Tél: +216 71 000 000";

    private static final Font FONT_TITLE  = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.DARK_GRAY);
    private static final Font FONT_HEADER = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
    private static final Font FONT_BODY   = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
    private static final Font FONT_BOLD   = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
    private static final Font FONT_SMALL  = new Font(Font.FontFamily.HELVETICA, 9, Font.ITALIC, BaseColor.GRAY);

    private PDFGenerator() {}

    public static void genererOrdonnance(Consultation c, String cheminFichier)
            throws DocumentException, IOException {

        Document doc = new Document(PageSize.A4, 50, 50, 60, 60);
        PdfWriter.getInstance(doc, new FileOutputStream(cheminFichier));
        doc.open();

        ajouterEnTete(doc);

        Paragraph titre = new Paragraph("ORDONNANCE MÉDICALE", FONT_TITLE);
        titre.setAlignment(Element.ALIGN_CENTER);
        titre.setSpacingBefore(10);
        titre.setSpacingAfter(15);
        doc.add(titre);
        addLine(doc);

        PdfPTable infos = new PdfPTable(2);
        infos.setWidthPercentage(100);
        infos.setSpacingBefore(10);
        infos.setSpacingAfter(10);
        addInfoRow(infos, "Patient :", c.getPatient().getNomComplet());
        addInfoRow(infos, "Né(e) le :", c.getPatient().getDateNaissance().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        addInfoRow(infos, "Médecin :", c.getMedecin().getNomComplet());
        addInfoRow(infos, "Spécialité :", c.getMedecin().getSpecialite());
        addInfoRow(infos, "Date :", c.getDateConsultation().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        doc.add(infos);
        addLine(doc);

        doc.add(createSectionTitle("DIAGNOSTIC"));
        doc.add(new Paragraph(c.getDiagnostic() != null ? c.getDiagnostic() : "-", FONT_BODY));
        doc.add(createSectionTitle("OBSERVATIONS"));
        doc.add(new Paragraph(c.getObservations() != null ? c.getObservations() : "-", FONT_BODY));
        doc.add(createSectionTitle("PRESCRIPTION"));
        doc.add(new Paragraph(c.getPrescription() != null ? c.getPrescription() : "-", FONT_BODY));

        doc.add(new Paragraph("\n\n"));
        Paragraph sig = new Paragraph("Signature du médecin : ________________________", FONT_BOLD);
        sig.setAlignment(Element.ALIGN_RIGHT);
        doc.add(sig);

        Paragraph footer = new Paragraph(CLINIQUE_NOM + " | " + CLINIQUE_ADRESSE + " | " + CLINIQUE_TEL, FONT_SMALL);
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(20);
        doc.add(footer);
        doc.close();
    }

    public static void genererFacture(Facture f, String cheminFichier)
            throws DocumentException, IOException {

        Document doc = new Document(PageSize.A4, 50, 50, 60, 60);
        PdfWriter.getInstance(doc, new FileOutputStream(cheminFichier));
        doc.open();

        ajouterEnTete(doc);

        Paragraph titre = new Paragraph("FACTURE", FONT_TITLE);
        titre.setAlignment(Element.ALIGN_CENTER);
        titre.setSpacingBefore(10);
        titre.setSpacingAfter(15);
        doc.add(titre);
        addLine(doc);

        Consultation c = f.getConsultation();
        PdfPTable infos = new PdfPTable(2);
        infos.setWidthPercentage(100);
        infos.setSpacingBefore(10);
        infos.setSpacingAfter(10);
        addInfoRow(infos, "N° Facture :", f.getNumeroFacture());
        addInfoRow(infos, "Date :", f.getDateFacture().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        addInfoRow(infos, "Patient :", c.getPatient().getNomComplet());
        addInfoRow(infos, "Médecin :", c.getMedecin().getNomComplet());
        addInfoRow(infos, "Mode de paiement :", f.getModePaiement().toString());
        addInfoRow(infos, "Statut :", f.getStatut().toString());
        doc.add(infos);
        addLine(doc);

        doc.add(createSectionTitle("DÉTAIL"));
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{50f, 25f, 25f});
        addHeaderCell(table, "Description");
        addHeaderCell(table, "Quantité");
        addHeaderCell(table, "Montant (DT)");
        addTableCell(table, "Consultation — " + c.getMedecin().getSpecialite());
        addTableCell(table, "1");
        addTableCell(table, String.format("%.2f", f.getMontantTotal()));
        doc.add(table);

        Paragraph footer = new Paragraph(CLINIQUE_NOM + " | " + CLINIQUE_ADRESSE + " | " + CLINIQUE_TEL, FONT_SMALL);
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(30);
        doc.add(footer);
        doc.close();
    }

    // ── helpers ──

    private static void ajouterEnTete(Document doc) throws DocumentException {
        PdfPTable header = new PdfPTable(1);
        header.setWidthPercentage(100);
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(new BaseColor(0, 90, 160));
        cell.setPadding(12);
        cell.setBorder(Rectangle.NO_BORDER);
        Paragraph name = new Paragraph(CLINIQUE_NOM, new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.WHITE));
        name.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(name);
        Paragraph addr = new Paragraph(CLINIQUE_ADRESSE + " | " + CLINIQUE_TEL,
            new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.LIGHT_GRAY));
        addr.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(addr);
        header.addCell(cell);
        doc.add(header);
        doc.add(new Paragraph("\n"));
    }

    private static void addLine(Document doc) throws DocumentException {
        LineSeparator ls = new LineSeparator();
        ls.setLineColor(new BaseColor(0, 90, 160));
        doc.add(new Chunk(ls));
        doc.add(new Paragraph("\n"));
    }

    private static Paragraph createSectionTitle(String text) {
        Paragraph p = new Paragraph(text, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, new BaseColor(0, 90, 160)));
        p.setSpacingBefore(10);
        p.setSpacingAfter(5);
        return p;
    }

    private static void addInfoRow(PdfPTable table, String label, String value) {
        PdfPCell lc = new PdfPCell(new Phrase(label, FONT_BOLD));
        lc.setBorder(Rectangle.NO_BORDER); lc.setPaddingBottom(4);
        PdfPCell vc = new PdfPCell(new Phrase(value != null ? value : "-", FONT_BODY));
        vc.setBorder(Rectangle.NO_BORDER); vc.setPaddingBottom(4);
        table.addCell(lc); table.addCell(vc);
    }

    private static void addHeaderCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FONT_HEADER));
        cell.setBackgroundColor(new BaseColor(0, 90, 160));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(8);
        table.addCell(cell);
    }

    private static void addTableCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FONT_BODY));
        cell.setPadding(6);
        table.addCell(cell);
    }
}
