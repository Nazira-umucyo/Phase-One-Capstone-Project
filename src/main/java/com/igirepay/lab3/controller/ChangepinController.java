package com.igirepay.lab3.controller;

import com.igirepay.lab1.model.Customer;
import com.igirepay.lab3.service.AuthService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import java.sql.SQLException;

public class ChangepinController {

    @FXML private PasswordField oldPinField;
    @FXML private PasswordField newPinField;
    @FXML private Label messageLabel;

    private Customer loggedInCustomer;
    private AuthService authService = new AuthService();

    public void setCustomer(Customer customer) {
        this.loggedInCustomer = customer;
    }

    @FXML
    protected void handleChangePin() {
        String oldPin = oldPinField.getText().trim();
        String newPin = newPinField.getText().trim();

        if (oldPin.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Old PIN is required!");
            return;
        }
        if (newPin.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("New PIN is required!");
            return;
        }
        if (newPin.length() != 4) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("New PIN must be exactly 4 digits!");
            return;
        }
        if (!newPin.matches("[0-9]+")) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("PIN must contain numbers only!");
            return;
        }
        if (oldPin.equals(newPin)) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("New PIN must be different from old PIN!");
            return;
        }
        try {
            boolean success = authService.changePin(loggedInCustomer, oldPin, newPin);
            if (success) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("PIN changed successfully!");
                oldPinField.clear();
                newPinField.clear();
            } else {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Old PIN is incorrect!");
            }
        } catch (SQLException e) {
            messageLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    protected void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/igirepay/dashboard.fxml"));
            Stage stage = (Stage) messageLabel.getScene().getWindow();
            Scene scene = new Scene(loader.load(), 700, 500);
            DashboardController controller = loader.getController();
            controller.setCustomer(loggedInCustomer);
            stage.setScene(scene);
        } catch (Exception e) {
            messageLabel.setText("Error going back!");
        }
    }
}