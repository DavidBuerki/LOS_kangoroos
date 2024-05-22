package ch.hearc.cafheg.infrastructure.pdf;

import ch.hearc.cafheg.business.allocations.Allocataire;
import ch.hearc.cafheg.business.versements.Enfant;
import ch.hearc.cafheg.business.common.Montant;
import ch.hearc.cafheg.infrastructure.persistance.EnfantMapper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

public class PDFExporter {

  private static final Logger logger = LoggerFactory.getLogger(PDFExporter.class);
  private final EnfantMapper enfantMapper;

  public PDFExporter(EnfantMapper enfantMapper) {
    this.enfantMapper = enfantMapper;
  }

  public byte[] generatePDFVversement(Allocataire allocataire, Map<LocalDate, Montant> montantParMois) {
    logger.info("Génération du PDF des versements pour l'allocataire ID: {}", allocataire.getNoAVS().getValue());
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
         PDDocument document = new PDDocument()) {
      PDPage page = new PDPage();
      document.addPage(page);

      PDPageContentStream contentStream = new PDPageContentStream(document, page);
      contentStream.beginText();
      contentStream.newLineAtOffset(25, 500);
      contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
      contentStream.showText("Les versement suivants ont été fait à l'allocataire: " +
              allocataire.getNom() + " " + allocataire.getPrenom() + " (" +
              allocataire.getNoAVS().getValue() + ")");
      contentStream.endText();

      int i = 0;
      for (Map.Entry<LocalDate, Montant> entry : montantParMois.entrySet()) {
        LocalDate dateVersement = entry.getKey();
        contentStream.beginText();
        contentStream.newLineAtOffset(25, 450 - (i * 24));
        contentStream.showText(dateVersement.toString());
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(300, 450 - (i * 24));
        contentStream.showText(entry.getValue().getValue().toString() + " CHF");
        contentStream.endText();

        i++;
      }
      contentStream.close();
      document.save(baos);

      logger.info("PDF généré avec succès");
      return baos.toByteArray();
    } catch (IOException e) {
      logger.error("Erreur lors de la génération du PDF", e);
      throw new RuntimeException(e);
    }
  }

  public byte[] generatePDFAllocataire(Allocataire allocataire, Map<Long, Montant> montantsParEnfant) {
    logger.info("Génération du PDF pour un allocataire avec ID: {}", allocataire.getNoAVS().getValue());
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
         PDDocument document = new PDDocument()) {
      PDPage page = new PDPage();
      document.addPage(page);

      PDPageContentStream contentStream = new PDPageContentStream(document, page);
      contentStream.beginText();
      contentStream.newLineAtOffset(25, 500);
      contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
      contentStream.showText("L'allocataire " + allocataire.getNom() + " " +
              allocataire.getPrenom() + " (" + allocataire.getNoAVS().getValue() +
              ") possède des droits d'allocations pour " + montantsParEnfant.size() +
              " enfant(s):");
      contentStream.endText();

      int i = 0;
      for (Map.Entry<Long, Montant> entry : montantsParEnfant.entrySet()) {
        Enfant enfant = enfantMapper.findById(entry.getKey());
        contentStream.beginText();
        contentStream.newLineAtOffset(25, 450 - (i * 24));
        contentStream.showText(enfant.getNom() + " " + enfant.getPrenom() + " (" +
                enfant.getNoAVS().getValue() + ")");
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(300, 450 - (i * 24));
        contentStream.showText(entry.getValue().getValue().toString() + " CHF");
        contentStream.endText();

        i++;
      }
      contentStream.close();
      document.save(baos);

      logger.info("PDF pour allocataire généré avec succès");
      return baos.toByteArray();
    } catch (IOException e) {
      logger.error("Erreur lors de la génération du PDF pour allocataire", e);
      throw new RuntimeException(e);
    }
  }
}
