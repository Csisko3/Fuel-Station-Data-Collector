package fhtechnikum.com.example.stationdatacollector.communication;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Sender {
    public static void send(String msg, String queueName, String brokerUrl) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(brokerUrl);
        factory.setPort(30003);

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(queueName, false, false, false, null);
            channel.basicPublish("", queueName, null, msg.getBytes());
            System.out.println(" [x] Sent '" + msg + "' to queue: " + queueName);
        } catch (Exception e) {
            System.out.println(" [x] Unexpected exception at Sender: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void sendEnd(String customerId, String queueName, String brokerUrl) {
        String endMessage = customerId + ";end";
        send(endMessage, queueName, brokerUrl);
    }
}
