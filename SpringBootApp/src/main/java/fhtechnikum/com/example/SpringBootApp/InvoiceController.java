package fhtechnikum.com.example.SpringBootApp;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {


    @PostMapping("/{customerId}")
    public void sendCustomerID(@PathVariable String customerId) {
        System.out.println("ID send: " + customerId);
        Invoice.sendCustomerID(customerId);
    }


    @GetMapping("/{customerId}")
    public String getInvoice (@PathVariable String customerId) {
    return null;
    }
}
