package sn.project.consultation.services;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.stereotype.Service;
import sn.project.consultation.data.entities.Paiement;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PDFGeneratorService {

    public byte[] genererFacturePDF(Paiement paiement, String numeroFacture) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, baos);

            document.open();

            // 🔷 En-tête avec logo et titre
            ajouterEnTete(document, numeroFacture);

            // 👤 Infos patient et pro
            ajouterInfosClient(document, paiement);

            // 📋 Détails de la facture
            ajouterTableFacture(document, paiement);

            // 🧾 Résumé final
            ajouterTotal(document, paiement);

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération de la facture PDF", e);
        }
    }

    private void ajouterEnTete(Document doc, String numero) throws Exception {
        Paragraph titre = new Paragraph("FACTURE", new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD, BaseColor.BLACK));
        titre.setAlignment(Element.ALIGN_CENTER);
        doc.add(titre);

        Paragraph ref = new Paragraph("Numéro : " + numero, new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.GRAY));
        ref.setAlignment(Element.ALIGN_RIGHT);
        doc.add(ref);

        doc.add(Chunk.NEWLINE);
    }

    private void ajouterInfosClient(Document doc, Paiement paiement) throws Exception {
        Paragraph infos = new Paragraph();
        infos.add(new Phrase("Patient : " + paiement.getPatient().getNom() + "\n"));
        infos.add(new Phrase("Email : " + paiement.getPatient().getCoordonnees().getEmail() + "\n"));
        infos.add(new Phrase("Téléphone : " + paiement.getPatient().getCoordonnees().getNumeroTelephone() + "\n\n"));

        infos.add(new Phrase("Professionnel : Dr. " + paiement.getProfessionnel().getNom() + "\n"));
        infos.add(new Phrase("Spécialité : " + paiement.getProfessionnel().getSpecialite() + "\n"));
        infos.setSpacingAfter(20);
        doc.add(infos);
    }

    private void ajouterTableFacture(Document doc, Paiement paiement) throws Exception {
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{6, 2, 2});

        Font headFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

        PdfPCell cell;

        cell = new PdfPCell(new Phrase("Description", headFont));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Quantité", headFont));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Montant (FCFA)", headFont));
        table.addCell(cell);

        // ✅ Exemple de ligne
        table.addCell("Consultation à domicile");
        table.addCell("1");
        table.addCell(String.format("%,.0f", paiement.getMontant()));

        doc.add(table);
    }

    private void ajouterTotal(Document doc, Paiement paiement) throws Exception {
        doc.add(Chunk.NEWLINE);
        Paragraph total = new Paragraph("Total payé : " + String.format("%,.0f FCFA", paiement.getMontant()),
                new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD));
        total.setAlignment(Element.ALIGN_RIGHT);
        doc.add(total);

        doc.add(new Paragraph("Date : " + paiement.getDatePaiement().format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm")),
                new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC)));
    }
}

