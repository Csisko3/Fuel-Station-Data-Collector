package fhtechnikum.com.example.datacollectionreceiver.service;

import fhtechnikum.com.example.datacollectionreceiver.model.Charge;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DataService {

    public static String collectData(String dbUrl, String customerId) {
        StringBuilder collectedData = new StringBuilder();
        java.sql.Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://" + dbUrl + "/stationdb", "postgres", "postgres");

            String query = "SELECT * FROM charge WHERE customer_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(customerId));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Charge charge = new Charge(rs.getInt("id"), rs.getFloat("kwh"), rs.getInt("customer_id"));
                collectedData.append(charge.getId()).append(",");
                collectedData.append(charge.getKwh()).append(",");
                collectedData.append(charge.getCustomerId()).append(";");
            }

            rs.close();
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            collectedData.append(" [x] Error: No data found for customer ID ").append(customerId);
        }
        return collectedData.toString();
    }
}
