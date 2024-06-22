package disys;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class PdfGeneratorService {

    @Value("${pdf.storage.path}")
    private String pdfStoragePath;

    private static final Logger LOGGER = Logger.getLogger(PdfGeneratorService.class.getName());

    public void generatePdf(String customerId) {
        // Use static values for customer data
        String firstName = "John";
        String lastName = "Doe";

        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText("Invoice for Customer ID: " + customerId);
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Customer Name: " + firstName + " " + lastName);
            contentStream.endText();
            contentStream.close(); // Ensure the content stream is properly closed

            // Ensure the directory exists
            File directory = new File(pdfStoragePath);
            if (!directory.exists()) {
                boolean dirCreated = directory.mkdirs();
                if (!dirCreated) {
                    throw new IOException("Failed to create directory: " + pdfStoragePath);
                }
            }

            // Save the file in the specified directory
            File file = new File(pdfStoragePath + File.separator + "invoice_" + customerId + ".pdf");
            document.save(file);
            LOGGER.log(Level.INFO, "PDF saved at: {0}", file.getAbsolutePath());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error while generating PDF", e);
        } finally {
            try {
                document.close();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error while closing PDF document", e);
            }
        }
    }

    // For testing purposes
    public void generateTestPdf() {
        generatePdf("1"); // Use a static customer ID for testing
    }
}
