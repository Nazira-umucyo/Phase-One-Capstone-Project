package com.igirepay.lab3.controller;

import com.igirepay.lab1.model.Customer;
import com.igirepay.lab3.service.AuthService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterController {

    @FXML
    private TextField fullNameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    @FXML
    private PasswordField pinField;

    @FXML
    private Label messageLabel;

    private AuthService authService = new AuthService();

    @FXML
    protected void handleRegister() {
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String pin = pinField.getText().trim();

        if (fullName.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Full name is required!");
            return;
        }
        if (email.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Email is required!");
            return;
        }
        if (!email.contains("@")) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Please enter a valid email!");
            return;
        }
        if (phone.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Phone number is required!");
            return;
        }
        if (pin.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("PIN is required!");
            return;
        }
        if (pin.length() != 4) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("PIN must be exactly 4 digits!");
            return;
        }
        if (!pin.matches("[0-9]+")) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("PIN must contain numbers only!");
            return;
        }
        try {
            Customer customer = new Customer(0, fullName, email, phone, pin, "USER");
            authService.registerCustomer(customer);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/igirepay/login.fxml"));
            Stage stage = (Stage) fullNameField.getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 700, 500));
        } catch (Exception e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    protected void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/igirepay/login.fxml"));
            Stage stage = (Stage) fullNameField.getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 700, 500));
        } catch (Exception e) {
            messageLabel.setText("Error going back!");
        }
    }
}