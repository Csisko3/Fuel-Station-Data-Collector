package fhtechnikum.com.example.stationdatacollector.communication;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.TimeoutException;

public class Receiver {

    public static void receive(String queueName, String brokerUrl) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(brokerUrl);
        factory.setPort(30003);

        Connection rabbitConnection = factory.newConnection();
        Channel channel = rabbitConnection.createChannel();

        channel.queueDeclare(queueName, false, false, false, null);
        System.out.println(" [x] Listening on queue '" + queueName + "' at broker '" + brokerUrl + "'");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received message: " + message);

            // Extrahiere db_URL und customer_id aus der Nachricht
            String[] parts = message.split(";");
            if (parts.length < 2) {
                System.err.println(" [x] Invalid message format: " + message);
                return;
            }
            String customerId = parts[0];
            String dbUrl = parts[1];

            // Sammle Daten von der Station-Datenbank
            String collectedData = collectData(dbUrl, customerId);

            // Sende gesammelte Daten an die DataCollectionReceiver-Queue
            if (!collectedData.isEmpty()) {
                Sender.send(collectedData, "RECEIVER", brokerUrl);
            } else {
                System.out.println(" [x] No data found for customer_id: " + customerId);
            }
        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
        System.out.println(" [x] Consumer started and waiting for messages...");
    }

    private static String collectData(String dbUrl, String customerId) {
        StringBuilder collectedData = new StringBuilder();
        java.sql.Connection conn = null;
        try {
            // Verbindung zur Datenbank herstellen
            conn = DriverManager.getConnection("jdbc:postgresql://" + dbUrl + "/stationdb", "postgres", "postgres");

            // Verwende PreparedStatement für sichere Parameterbindung
            String query = "SELECT * FROM charge WHERE customer_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(customerId));
            ResultSet rs = pstmt.executeQuery();

            if (!rs.isBeforeFirst()) { // Prüfen, ob das ResultSet leer ist
                System.out.println(" [x] No records found for customer_id: " + customerId);
            } else {
                while (rs.next()) {
                    collectedData.append(rs.getInt("id")).append(",");
                    collectedData.append(rs.getFloat("kwh")).append(",");
                    collectedData.append(rs.getInt("customer_id")).append(";");
                }
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
