package disys;

import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class CustomerService {

    private static final String DB_URL = "jdbc:postgresql://localhost:30001/customerdb";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";

    public Customer getCustomerData(String customerId) throws SQLException {
        Customer customer = null;
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT id, first_name, last_name FROM customer WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, customerId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        customer = new Customer(
                                resultSet.getInt("id"),
                                resultSet.getString("first_name"),
                                resultSet.getString("last_name")
                        );
                    }
                }
            }
        }
        return customer;
    }
}
