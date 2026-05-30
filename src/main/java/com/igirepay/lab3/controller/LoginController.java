package com.igirepay;

import com.igirepay.lab1.model.Customer;
import com.igirepay.lab3.service.AuthService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField phoneField;

    @FXML
    private PasswordField pinField;

    @FXML
    private Label messageLabel;

    private AuthService authService = new AuthService();

    @FXML
    protected void handleLogin() {
        String phoneNumber = phoneField.getText().trim();
        String pin = pinField.getText().trim();
        if (phoneNumber.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Phone number is required!");
            return;
        }
        if (pin.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("PIN is required!");
            return;
        }
        try {
            Customer customer = authService.login(phoneNumber, pin);
            if (customer != null) {
                if (customer.getRole().equals("ADMIN")) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("admindashboard.fxml"));
                    Stage stage = (Stage) phoneField.getScene().getWindow();
                    Scene scene = new Scene(loader.load(), 700, 500);
                    AdminDashboardController controller = loader.getController();
                    controller.setCustomer(customer);
                    stage.setScene(scene);
                } else {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
                    Stage stage = (Stage) phoneField.getScene().getWindow();
                    Scene scene = new Scene(loader.load(), 700, 500);
                    DashboardController controller = loader.getController();
                    controller.setCustomer(customer);
                    stage.setScene(scene);
                }
            } else {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Invalid phone number or PIN!");
                phoneField.clear();
                pinField.clear();
            }
        } catch (SQLException e) {
            messageLabel.setText("Error: " + e.getMessage());
        } catch (Exception e) {
            messageLabel.setText("Error loading dashboard!");
        }
    }

    @FXML
    protected void handleRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("register.fxml"));
            Stage stage = (Stage) phoneField.getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 700, 500));
        } catch (Exception e) {
            messageLabel.setText("Error opening register screen!");
        }
    }
}