package fhtechnikum.com.example.stationdatacollector.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DataService {

    public static String collectData(String dbUrl, String customerId) {
        StringBuilder collectedData = new StringBuilder();
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://" + dbUrl + "/stationdb", "postgres", "postgres");

            String query = "SELECT * FROM charge WHERE customer_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(customerId));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                collectedData.append(rs.getInt("id")).append(",");
                collectedData.append(rs.getFloat("kwh")).append(",");
                collectedData.append(rs.getInt("customer_id")).append(";");
            }

            rs.close();
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return collectedData.toString();
    }
}
