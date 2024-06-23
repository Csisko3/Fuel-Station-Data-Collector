module com.example.invoiceapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;

    opens com.example.javafx to javafx.fxml;
    exports com.example.javafx;
}
