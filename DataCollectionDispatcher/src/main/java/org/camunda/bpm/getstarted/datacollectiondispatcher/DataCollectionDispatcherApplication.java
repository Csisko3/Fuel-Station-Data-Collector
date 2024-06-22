package org.camunda.bpm.getstarted.datacollectiondispatcher;

import org.camunda.bpm.getstarted.datacollectiondispatcher.controller.DataCollectionDispatcherController;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@SpringBootApplication
public class DataCollectionDispatcherApplication {
    private final static String QUEUE_NAME = "sendID";
    private final static String BROKER = "localhost";


    public static void main(String[] args) {
        try {
            DataCollectionDispatcherController dispatcherController = new DataCollectionDispatcherController();
            dispatcherController.run(QUEUE_NAME, BROKER);
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

}
