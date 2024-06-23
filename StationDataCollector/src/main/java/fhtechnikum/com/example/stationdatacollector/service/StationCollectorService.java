package fhtechnikum.com.example.stationdatacollector.service;

import fhtechnikum.com.example.stationdatacollector.communication.Sender;

public class StationCollectorService {

    public static void collectAndSendData(String dbUrl, String customerId, String brokerUrl) {
        String collectedData = DataService.collectData(dbUrl, customerId);

        if (!collectedData.isEmpty()) {
            Sender.send(collectedData, "RECEIVER", brokerUrl);
        } else {
            System.out.println(" [x] No data found for customer_id: " + customerId);
        }

        // Check if this is the last station to process
        if (isLastStation(dbUrl)) {
            // Send end message to the DataCollectionReceiver queue
            Sender.sendEnd(customerId, "RECEIVER", brokerUrl);
        }
    }

    private static boolean isLastStation(String dbUrl) {
        // Logic to determine if this is the last station
        return dbUrl.equals("localhost:30013");
    }
}
