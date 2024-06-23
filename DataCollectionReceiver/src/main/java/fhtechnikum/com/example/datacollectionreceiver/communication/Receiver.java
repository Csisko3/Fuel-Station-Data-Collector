package fhtechnikum.com.example.datacollectionreceiver.communication;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

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

        StringBuilder collectedData = new StringBuilder();

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received message: " + message);

            if (message.endsWith(";end")) {
                // Send collected data to the PDF_GENERATOR queue
                Sender.send(collectedData.toString(), "PDF_GENERATOR", brokerUrl);
                System.out.println(" [x] Sent collected data to PDF_GENERATOR queue");
                // Reset the collected data
                collectedData.setLength(0);
            } else if (!message.endsWith(";start")) {
                // Add data to collectedData
                collectedData.append(message);
            }
        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
        System.out.println(" [x] Consumer started and waiting for messages...");
    }
}
