package fhtechnikum.com.example.stationdatacollector;

import fhtechnikum.com.example.SpringBootApp.InvoiceController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

class StationDataCollectorTests {

    private MockMvc mockMvc;

    // Injects a mock of InvoiceController into the test
    @InjectMocks
    private InvoiceController invoiceController;

    //Set up test environment
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(invoiceController).build();

        // Initialize database with test data
        initializeDatabaseWithTestData();
    }

    //insert test data into db
    private void initializeDatabaseWithTestData() {
        String[] dbUrls = {
                "jdbc:postgresql://localhost:30011/stationdb",
                "jdbc:postgresql://localhost:30012/stationdb",
                "jdbc:postgresql://localhost:30013/stationdb"
        };
        String dbUser = "postgres";
        String dbPassword = "postgres";

        for (String dbUrl : dbUrls) {
            try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
                String insertSql = "INSERT INTO charge (id, kwh, customer_id) VALUES (?, ?, ?) ON CONFLICT DO NOTHING";
                try (PreparedStatement statement = connection.prepareStatement(insertSql)) {
                    statement.setInt(1, 1); // Sample ID
                    statement.setFloat(2, 50.0f); // Sample kWh
                    statement.setInt(3, 2); // Customer ID
                    statement.executeUpdate();
                }
            } catch (Exception e) {
                e.printStackTrace();
                fail("Failed to insert data into the database: " + dbUrl);
            }
        }
    }

    //Actuall test
    @Test
    void testAccessToStationDatabases() {
        // Database details
        String[] dbUrls = {
                "jdbc:postgresql://localhost:30011/stationdb",
                "jdbc:postgresql://localhost:30012/stationdb",
                "jdbc:postgresql://localhost:30013/stationdb"
        };
        String dbUser = "postgres";
        String dbPassword = "postgres";

        for (String dbUrl : dbUrls) {
            try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                 PreparedStatement statement = connection.prepareStatement("SELECT * FROM charge WHERE customer_id = ?")) {

                statement.setInt(1, 2);
                ResultSet resultSet = statement.executeQuery();

                List<String> dataRetrieved = new ArrayList<>();
                while (resultSet.next()) {
                    String data = resultSet.getInt("id") + "," + resultSet.getFloat("kwh") + "," + resultSet.getInt("customer_id");
                    dataRetrieved.add(data);
                }

                // Ensure that data has been retrieved
                assertThat(dataRetrieved).isNotEmpty();

            } catch (SQLException e) {
                e.printStackTrace();
                fail("Failed to retrieve data from the database: " + dbUrl);
            }
        }
    }
}
