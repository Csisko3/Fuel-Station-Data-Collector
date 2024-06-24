package fhtechnikum.com.example.stationdatacollector;

import fhtechnikum.com.example.SpringBootApp.InvoiceController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GetInvoiceTest {

	private MockMvc mockMvc;

	@InjectMocks
	private InvoiceController invoiceController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(invoiceController).build();
	}

	@Test
	void testGetInvoice() throws Exception {
		Path invoicePath = Paths.get("FileStorage", "invoice_1.pdf"); //erstellt Ordner im Collector
		File invoiceFile = new File(invoicePath.toUri());

		// Erstellen Sie die Datei, wenn sie nicht existiert
		if (!invoiceFile.exists()) {
			invoiceFile.createNewFile();
		}

		mockMvc.perform(get("/invoices/{customerId}", "1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.downloadLink").value("http://localhost:8080/invoices/1/download"))
				.andExpect(jsonPath("$.creationTime").isNotEmpty());
				//Check if downloadLink is rightly made and creationTime not empty

		invoiceFile.delete(); // bereinigt Dateien nach Test
	}
}
