package com.igirepay;

import com.igirepay.lab1.model.Customer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import com.igirepay.ChangepinController;


public class DashboardController {

    @FXML private Text welcomeText;
    @FXML private Label messageLabel;

    private Customer loggedInCustomer;

    public void setCustomer(Customer customer) {
        this.loggedInCustomer = customer;
        welcomeText.setText("Welcome, " + customer.getFullName() + "!");
    }

    @FXML
    protected void handleCustomerManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("customer.fxml"));
            Stage stage = (Stage) welcomeText.getScene().getWindow();
            Scene scene = new Scene(loader.load(), 700, 500);
            CustomerController controller = loader.getController();
            controller.setCustomer(loggedInCustomer);
            stage.setScene(scene);
        } catch (Exception e) {
            messageLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    protected void handleAccountManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("account.fxml"));
            Stage stage = (Stage) welcomeText.getScene().getWindow();
            Scene scene = new Scene(loader.load(), 700, 500);
            AccountController controller = loader.getController();
            controller.setCustomer(loggedInCustomer);
            stage.setScene(scene);
        } catch (Exception e) {
            messageLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    protected void handleTransactionManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("transaction.fxml"));
            Stage stage = (Stage) welcomeText.getScene().getWindow();
            Scene scene = new Scene(loader.load(), 700, 500);
            TransactionController controller = loader.getController();
            controller.setCustomer(loggedInCustomer);
            stage.setScene(scene);
        } catch (Exception e) {
            messageLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    protected void handleReports() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("reports.fxml"));
            Stage stage = (Stage) welcomeText.getScene().getWindow();
            Scene scene = new Scene(loader.load(), 700, 500);
            ReportsController controller = loader.getController();
            controller.setCustomer(loggedInCustomer);
            stage.setScene(scene);
        } catch (Exception e) {
            messageLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    protected void handleChangePin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("changepin.fxml"));
            Stage stage = (Stage) welcomeText.getScene().getWindow();
            Scene scene = new Scene(loader.load(), 700, 500);
            ChangepinController controller = loader.getController();
            controller.setCustomer(loggedInCustomer);
            stage.setScene(scene);
        } catch (Exception e) {
            messageLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    protected void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Stage stage = (Stage) welcomeText.getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 700, 500));
        } catch (Exception e) {
            messageLabel.setText("Error logging out!");
        }
    }
}