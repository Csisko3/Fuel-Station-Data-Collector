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
import java.util.concurrent.atomic.AtomicInteger;

public class Receiver {

    private static final int MESSAGE_THRESHOLD = 3;
    private static AtomicInteger messageCounter = new AtomicInteger(0);
    private static StringBuilder collectedData = new StringBuilder();

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

            // Extract db_URL and customer_id from the message
            String[] parts = message.split(";");
            if (parts.length < 2) {
                System.err.println(" [x] Invalid message format: " + message);
                return;
            }
            String customerId = parts[0];
            String dbUrl = parts[1];

            // Collect data from the station database
            String data = collectData(dbUrl, customerId);

            // Append collected data
            if (!data.isEmpty()) {
                synchronized (collectedData) {
                    collectedData.append(data);
                }
            } else {
                System.out.println(" [x] No data found for customer_id: " + customerId);
            }

            // Increment the counter and check if the threshold is reached
            if (messageCounter.incrementAndGet() >= MESSAGE_THRESHOLD) {
                // Send collected data to the DataCollectionReceiver queue
                Sender.send(collectedData.toString(), "RECEIVER", brokerUrl);
                System.out.println(" [x] Sent collected data to RECEIVER queue");
                // Reset the counter and collected data
                messageCounter.set(0);
                synchronized (collectedData) { //Ohne synchronized könnten mehrere threads
                    collectedData.setLength(0);
                }
            }
        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
        System.out.println(" [x] Consumer started and waiting for messages...");
    }

    private static String collectData(String dbUrl, String customerId) {
        StringBuilder collectedData = new StringBuilder();
        java.sql.Connection conn = null;
        try {
            // Establish connection to the database
            conn = DriverManager.getConnection("jdbc:postgresql://" + dbUrl + "/stationdb", "postgres", "postgres");

            // Use PreparedStatement for secure parameter binding
            String query = "SELECT * FROM charge WHERE customer_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(customerId));
            ResultSet rs = pstmt.executeQuery();

            if (!rs.isBeforeFirst()) { // Check if the ResultSet is empty
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
