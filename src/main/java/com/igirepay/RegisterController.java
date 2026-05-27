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
        String fullName = fullNameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String pin = pinField.getText();
        try {
            Customer customer = new Customer(0, fullName, email, phone, pin);
            authService.registerCustomer(customer);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Stage stage = (Stage) fullNameField.getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 700, 500));
        } catch (Exception e) {
            messageLabel.setText("Error going back!");
        }
    }
}