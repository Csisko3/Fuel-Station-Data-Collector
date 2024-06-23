package disys;

import disys.communication.Receiver;

public class PDFGeneratorApplication {

    private final static String QUEUE_NAME = "PDF_GENERATOR";
    private final static String BROKER = "localhost";

    public static void main(String[] args) {
        try {
            Receiver.receive(QUEUE_NAME, BROKER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
