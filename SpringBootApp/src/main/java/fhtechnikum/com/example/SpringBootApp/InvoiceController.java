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
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    @PostMapping("/{customerId}")
    public void sendCustomerID(@PathVariable String customerId) {
        System.out.println("ID send: " + customerId);
        Invoice.sendCustomerID(customerId);
    }

    private static final String INVOICES_DIRECTORY = "FileStorage" + File.separator;

    @GetMapping("/{customerId}")
    public ResponseEntity<Map<String, Object>> getInvoice(@PathVariable String customerId) {
        // Erstelle den Dateipfad basierend auf der customerId
        Path invoicePath = Paths.get(INVOICES_DIRECTORY, "invoice_" + customerId + ".pdf");
        File invoiceFile = invoicePath.toFile();

        if (!invoiceFile.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Downloadlink + creation time
        Map<String, Object> response = new HashMap<>();
        String downloadLink = "http://localhost:8080/invoices/" + customerId + "/download";
        response.put("downloadLink", downloadLink);
        response.put("creationTime", Instant.ofEpochMilli(invoiceFile.lastModified()).toString());

        return ResponseEntity.ok(response);
    }

    // Create Download
    @GetMapping("/{customerId}/download")
    public ResponseEntity<Resource> downloadInvoice(@PathVariable String customerId) {
        // create path
        Path invoicePath = Paths.get(INVOICES_DIRECTORY, "invoice_" + customerId + ".pdf");
        File invoiceFile = invoicePath.toFile();

        if (!invoiceFile.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Resource resource = new FileSystemResource(invoiceFile);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + invoiceFile.getName());
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}
