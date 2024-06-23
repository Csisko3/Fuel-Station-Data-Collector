package fhtechnikum.com.example.stationdatacollector.communication;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import fhtechnikum.com.example.stationdatacollector.service.DataService;

import java.io.IOException;
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

            // Send collected data to the DataCollectionReceiver queue
            Sender.send(collectedData, "COLLECTOR_RECEIVER", brokerUrl);
        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
        System.out.println(" [x] Consumer started and waiting for messages...");
    }
}
