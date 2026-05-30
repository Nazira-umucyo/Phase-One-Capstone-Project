module com.ogire.igirepay {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.management;

    opens com.igirepay to javafx.fxml;
    exports com.igirepay;
    exports com.igirepay.lab3.controller;
    opens com.igirepay.lab3.controller to javafx.fxml;
}