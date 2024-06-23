package disys.communication;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import disys.service.CustomerService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PDFGenerator {

    private static final String INVOICES_DIRECTORY = "FileStorage/";
    private static final double PRICE_PER_KWH = 0.20;

    public static void generatePDF(String data) {
        if (data.isEmpty()) {
            System.out.println(" [x] No data to generate PDF");
            return;
        }

        try {
            // Ensure the directory exists
            File directory = new File(INVOICES_DIRECTORY);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String[] entries = data.split(";");
            String firstEntry = entries[0];
            String[] firstParts = firstEntry.split(",");
            String customerId = firstParts[2];
            String customerName = CustomerService.getCustomerName(customerId);
            String filePath = INVOICES_DIRECTORY + "Invoice_" + customerId + ".pdf";

            // Create a new PDF document
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Add title
            Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
            Paragraph title = new Paragraph("Invoice", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Add customer details
            document.add(new Paragraph("Customer ID: " + customerId));
            document.add(new Paragraph("Customer Name: " + customerName));
            document.add(new Paragraph("Price per kWh: €" + PRICE_PER_KWH));
            document.add(new Paragraph(" ")); // Empty line for spacing

            // Add table with data
            PdfPTable table = new PdfPTable(4); // 4 columns: Charge ID, kWh, Customer ID, Price
            table.addCell("Charge ID");
            table.addCell("kWh");
            table.addCell("Customer ID");
            table.addCell("Price (€)");

            for (String entry : entries) {
                String[] parts = entry.split(",");
                table.addCell(parts[0]);
                table.addCell(parts[1]);
                table.addCell(parts[2]);

                // Calculate price and add to table
                double kwh = Double.parseDouble(parts[1]);
                double price = kwh * PRICE_PER_KWH;
                table.addCell(String.format("%.2f", price));
            }

            document.add(table);
            document.close();

            System.out.println(" [x] PDF generated at: " + filePath);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
}
