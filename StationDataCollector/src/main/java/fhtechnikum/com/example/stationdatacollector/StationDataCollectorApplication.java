package fhtechnikum.com.example.stationdatacollector;

import fhtechnikum.com.example.stationdatacollector.communication.Receiver;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class StationDataCollectorApplication {

	private static final String QUEUE_NAME = "COLLECTOR";
	private static final String BROKER_URL = "localhost";

	public static void main(String[] args) {
		try {
			Receiver.receive(QUEUE_NAME, BROKER_URL);
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}
	}
}