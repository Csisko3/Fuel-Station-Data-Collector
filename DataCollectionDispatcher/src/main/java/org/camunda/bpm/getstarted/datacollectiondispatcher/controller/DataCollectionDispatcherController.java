package org.camunda.bpm.getstarted.datacollectiondispatcher.controller;

import org.camunda.bpm.getstarted.datacollectiondispatcher.communication.Receiver;
import org.camunda.bpm.getstarted.datacollectiondispatcher.communication.Sender;
import org.camunda.bpm.getstarted.datacollectiondispatcher.model.Station;
import org.camunda.bpm.getstarted.datacollectiondispatcher.service.DataCollectionService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class DataCollectionDispatcherController {

    private final DataCollectionService dataCollectionService;
    private String broker;

    public DataCollectionDispatcherController() {
        this.dataCollectionService = new DataCollectionService();
    }

    public void run(String queueName, String brokerUrl) throws IOException, TimeoutException {
        this.broker = brokerUrl;
        Receiver.receive(queueName, brokerUrl, this);
    }

    public void startDispatching(String customerId) throws SQLException {
        ArrayList<Station> stations = dataCollectionService.getStations();
        Sender.send("start", customerId, "RECEIVER", broker);
        for (Station station : stations) {
            Sender.send(station.getDb_url(), customerId, "COLLECTOR", broker);
        }
        Sender.send("end", customerId, "COLLECTOR", broker);
    }
}
