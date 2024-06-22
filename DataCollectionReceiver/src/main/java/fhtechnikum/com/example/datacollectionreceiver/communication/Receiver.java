package fhtechnikum.com.example.datacollectionreceiver.communication;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import fhtechnikum.com.example.datacollectionreceiver.service.DataService;

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

            try {
                // Extract db_URL and customer_id from the message
                String[] parts = message.split(";");
                if (parts.length < 2) {
                    System.err.println(" [x] Invalid message format: " + message);
                    return;
                }
                String customerId = parts[0];
                String dbUrl = parts[1];

                // Collect data from the station database
                String collectedData = DataService.collectData(dbUrl, customerId);
                if (collectedData.isEmpty()) {
                    System.err.println(" [x] No data found for customer ID: " + customerId + " at URL: " + dbUrl);
                } else {
                    System.out.println(" [x] Collected data: " + collectedData);
                    // Send collected data to the DataCollectionReceiver queue
                    Sender.send(collectedData, "RECEIVER", brokerUrl);
                }
            } catch (Exception e) {
                System.err.println(" [x] Error processing message: " + e.getMessage());
                e.printStackTrace();
            }
        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
        System.out.println(" [x] Consumer started and waiting for messages...");
    }
}
