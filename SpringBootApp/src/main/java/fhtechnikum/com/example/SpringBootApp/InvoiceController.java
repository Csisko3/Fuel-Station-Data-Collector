package fhtechnikum.com.example.SpringBootApp;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {


    @PostMapping("/{customerId}")
    public void sendCustomerID(@PathVariable String customerId) {
        System.out.println("ID send: " + customerId);
        Invoice.sendCustomerID(customerId);
    }

    private static final String INVOICES_DIRECTORY = "invoices" + File.separator;

    @GetMapping("/{customerId}")
    public ResponseEntity<Resource> getInvoice(@PathVariable String customerId) {
        // Erstelle den Dateipfad basierend auf der customerId
        Path invoicePath = Paths.get(INVOICES_DIRECTORY, "invoice_" + customerId + ".pdf");
        File invoiceFile = invoicePath.toFile();

        // Prüfe, ob die Datei existiert
        if (!invoiceFile.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Erstelle die ResponseEntity mit der PDF-Datei und den entsprechenden HTTP-Headern
        Resource resource = new FileSystemResource(invoiceFile);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + invoiceFile.getName());
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}
