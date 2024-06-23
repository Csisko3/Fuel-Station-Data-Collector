package org.camunda.bpm.getstarted.datacollectiondispatcher.controller;

import org.camunda.bpm.getstarted.datacollectiondispatcher.model.Station;
import org.camunda.bpm.getstarted.datacollectiondispatcher.service.DataCollectionService;
import org.camunda.bpm.getstarted.datacollectiondispatcher.communication.DataCollector;

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
        DataCollector.receive(queueName, brokerUrl, this);
    }

    public void startDispatching(String customerId) throws SQLException {
        ArrayList<Station> stations = dataCollectionService.getStations();
        DataCollector.send("start", customerId, "RECEIVER", broker);
        for (Station station : stations) {
            DataCollector.send(station.getDb_url(), customerId, "COLLECTOR", broker);
        }
    }
}
