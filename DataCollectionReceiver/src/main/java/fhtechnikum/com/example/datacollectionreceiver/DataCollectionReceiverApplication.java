package fhtechnikum.com.example.datacollectionreceiver;

import fhtechnikum.com.example.datacollectionreceiver.communication.Receiver;

public class DataCollectionReceiverApplication {

	private final static String QUEUE_NAME = "COLLECTOR";
	private final static String BROKER = "localhost";

	public static void main(String[] args) {
		try {
			Receiver.receive(QUEUE_NAME, BROKER);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
