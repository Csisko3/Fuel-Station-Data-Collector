module com.example.ui_javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.httpcomponents.httpclient;
    requires java.desktop;


    opens org.example.javafx to javafx.fxml;
    exports org.example.javafx;
}