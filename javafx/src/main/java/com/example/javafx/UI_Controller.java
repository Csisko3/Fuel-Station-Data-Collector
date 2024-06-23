package com.example.javafx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.application.Platform;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class UI_Controller {
    @FXML
    private TextField customerIdField;
    @FXML
    private Button createInvoiceButton;
    @FXML
    private Button showStatusButton;
    @FXML
    private Label statusLabel;

    @FXML
    private void initialize() {
        createInvoiceButton.setOnAction(event -> createInvoice());
        showStatusButton.setOnAction(event -> showStatus());
    }

    private void createInvoice() {
        String customerId = customerIdField.getText();
        if (customerId == null || customerId.isEmpty()) {
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("Customer ID cannot be empty.");
            return;
        }

        new Thread(() -> {
            try {
                URL url = new URL("http://localhost:8080/invoices/" + customerId);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    Platform.runLater(() -> {
                        statusLabel.setTextFill(Color.GREEN);
                        statusLabel.setText("Invoice creation started for Customer ID: " + customerId);
                    });
                } else if (responseCode == 404) {
                    Platform.runLater(() -> {
                        statusLabel.setTextFill(Color.RED);
                        statusLabel.setText("Customer ID: " + customerId + " not found.");
                    });
                } else {
                    Platform.runLater(() -> {
                        statusLabel.setTextFill(Color.RED);
                        statusLabel.setText("Failed to start invoice creation. Response code: " + responseCode);
                    });
                }
                connection.disconnect();
            } catch (Exception e) {
                Platform.runLater(() -> {
                    statusLabel.setTextFill(Color.RED);
                    statusLabel.setText("Error: " + e.getMessage());
                });
            }
        }).start();
    }

    private void showStatus() {
        String customerId = customerIdField.getText();
        if (customerId == null || customerId.isEmpty()) {
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("Customer ID cannot be empty.");
            return;
        }

        new Thread(() -> {
            try {
                URL url = new URL("http://localhost:8080/invoices/" + customerId);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    Scanner scanner = new Scanner(connection.getInputStream());
                    StringBuilder response = new StringBuilder();
                    while (scanner.hasNextLine()) {
                        response.append(scanner.nextLine());
                    }
                    scanner.close();

                    // Log the response to check its content
                    System.out.println("Server Response: " + response.toString());

                    // Parse the JSON response
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    String downloadLink = jsonResponse.getString("downloadLink");
                    String creationTime = jsonResponse.getString("creationTime");

                    Platform.runLater(() -> {
                        statusLabel.setTextFill(Color.GREEN);
                        statusLabel.setText("Invoice ready for download:\nLink: " + downloadLink + "\nCreated at: " + creationTime);
                    });
                } else if (responseCode == 404) {
                    Platform.runLater(() -> {
                        statusLabel.setTextFill(Color.RED);
                        statusLabel.setText("Invoice not found for Customer ID: " + customerId);
                    });
                } else {
                    Platform.runLater(() -> {
                        statusLabel.setTextFill(Color.RED);
                        statusLabel.setText("Failed to get invoice status. Response code: " + responseCode);
                    });
                }
                connection.disconnect();
            } catch (Exception e) {
                Platform.runLater(() -> {
                    statusLabel.setTextFill(Color.RED);
                    statusLabel.setText("Error: " + e.getMessage());
                });
            }
        }).start();
    }
}
