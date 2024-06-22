package fhtechnikum.com.example.SpringBootApp;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

public class Invoice {

    private final static String QUEUE_NAME = "sendID";

    public static void sendCustomerID(String customerId) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(30003);

        try (
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
        ) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            String id = customerId;

            channel.basicPublish("", QUEUE_NAME, null, id.getBytes(StandardCharsets.UTF_8));

            System.out.println(" [x] Sent CustomerID: " + id);

        } catch (Exception e) {
            System.out.println(" [x] Unexpected exception at Invoice: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

