package com.igirepay;

import com.igirepay.lab1.model.Account;
import com.igirepay.lab1.model.Customer;
import com.igirepay.lab2.dao.AccountDao;
import com.igirepay.lab2.dao.CustomerDao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.List;

public class CustomerController {

    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Label phoneLabel;
    @FXML private Label accountsLabel;
    @FXML private Label messageLabel;
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private VBox profileSection;
    @FXML private VBox updateSection;
    @FXML private VBox accountsSection;

    private Customer loggedInCustomer;
    private CustomerDao customerDAO = new CustomerDao();
    private AccountDao accountDAO = new AccountDao();

    public void setCustomer(Customer customer) {
        this.loggedInCustomer = customer;
        profileSection.setPrefWidth(Double.MAX_VALUE);
        updateSection.setPrefWidth(Double.MAX_VALUE);
        accountsSection.setPrefWidth(Double.MAX_VALUE);
        showProfile();
    }

    private void hideAllSections() {
        profileSection.setVisible(false);
        profileSection.setManaged(false);
        updateSection.setVisible(false);
        updateSection.setManaged(false);
        accountsSection.setVisible(false);
        accountsSection.setManaged(false);
    }

    private void showProfile() {
        hideAllSections();
        profileSection.setVisible(true);
        profileSection.setManaged(true);
        nameLabel.setText("Name: " + loggedInCustomer.getFullName());
        emailLabel.setText("Email: " + loggedInCustomer.getEmail());
        phoneLabel.setText("Phone: " + loggedInCustomer.getPhoneNumber());
    }

    @FXML
    protected void handleViewProfile() {
        showProfile();
    }

    @FXML
    protected void handleUpdateInfo() {
        hideAllSections();
        updateSection.setVisible(true);
        updateSection.setManaged(true);
        fullNameField.setText(loggedInCustomer.getFullName());
        emailField.setText(loggedInCustomer.getEmail());
        phoneField.setText(loggedInCustomer.getPhoneNumber());
    }

    @FXML
    protected void handleSaveInfo() {
        try {
            loggedInCustomer.setFullName(fullNameField.getText());
            loggedInCustomer.setEmail(emailField.getText());
            loggedInCustomer.setPhoneNumber(phoneField.getText());
            customerDAO.updateCustomer(loggedInCustomer);
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Information updated successfully!");
            showProfile();
        } catch (SQLException e) {
            messageLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    protected void handleViewAccounts() {
        try {
            hideAllSections();
            accountsSection.setVisible(true);
            accountsSection.setManaged(true);
            List<Account> accounts = accountDAO.getAccountsByCustomerId(loggedInCustomer.getId());
            if (accounts.isEmpty()) {
                accountsLabel.setText("No accounts found!");
                return;
            }
            StringBuilder sb = new StringBuilder();
            for (Account account : accounts) {
                sb.append("ID: ").append(account.getId())
                        .append(" | Type: ").append(account.getAccountType())
                        .append(" | Balance: ").append(account.getBalance()).append(" RWF\n");
            }
            accountsLabel.setText(sb.toString());
        } catch (SQLException e) {
            messageLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    protected void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
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