package disys.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CustomerService {

    private static final String DB_URL = "jdbc:postgresql://localhost:30001/customerdb";
    private static final String USER = "postgres";
    private static final String PASS = "postgres";

    public static String getCustomerName(String customerId) {
        String customerName = "";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement("SELECT first_name, last_name FROM customer WHERE id = ?")) {

            pstmt.setInt(1, Integer.parseInt(customerId));
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                customerName = rs.getString("first_name") + " " + rs.getString("last_name");
            }

            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return customerName;
    }
}
