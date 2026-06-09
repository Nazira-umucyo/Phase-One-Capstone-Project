package com.igirepay.lab3.controller;

import com.igirepay.lab1.model.Account;
import com.igirepay.lab1.model.Customer;
import com.igirepay.lab2.dao.AccountDao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.List;

public class AccountController {

    @FXML private VBox createSection;
    @FXML private VBox balanceSection;
    @FXML private VBox myAccountsSection;
    @FXML private VBox deleteSection;
    @FXML private VBox createFormSection;
    @FXML private Label messageLabel;
    @FXML private Label balanceLabel;
    @FXML private Label createMessage;
    @FXML private Text createTitle;
    @FXML private TextField accountIdField;
    @FXML private TextField deleteAccountIdField;
    @FXML private TextField phoneNumberField;
    @FXML private ComboBox<String> accountTypeComboBox;
    @FXML private VBox accountsTableRows;

    private Customer loggedInCustomer;
    private AccountDao accountDAO = new AccountDao();



    public void setCustomer(Customer customer) {
        this.loggedInCustomer = customer;
        createSection.setPrefWidth(Double.MAX_VALUE);
        balanceSection.setPrefWidth(Double.MAX_VALUE);
        deleteSection.setPrefWidth(Double.MAX_VALUE);
        myAccountsSection.setPrefWidth(Double.MAX_VALUE);
        createFormSection.setPrefWidth(Double.MAX_VALUE);
    }

    private void hideAllSections() {
        createSection.setVisible(false);
        createSection.setManaged(false);
        balanceSection.setVisible(false);
        balanceSection.setManaged(false);
        deleteSection.setVisible(false);
        deleteSection.setManaged(false);
        myAccountsSection.setVisible(false);
        myAccountsSection.setManaged(false);
        createFormSection.setVisible(false);
        createFormSection.setManaged(false);
        messageLabel.setText("");
        phoneNumberField.clear();
        accountIdField.clear();
        deleteAccountIdField.clear();
        balanceLabel.setText("");
    }
    @FXML
    public void initialize() {
        accountTypeComboBox.getItems().addAll("WALLET", "SAVINGS");
    }

    @FXML
    protected void handleCreateAccount() {
        hideAllSections();
        createFormSection.setVisible(true);
        createFormSection.setManaged(true);
        accountTypeComboBox.setValue(null);
    }

    @FXML
    protected void handleConfirmCreate() {
        String phoneNumber = phoneNumberField.getText().trim();
        String accountType = accountTypeComboBox.getValue();
        if (phoneNumber.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Phone number is required!");
            return;
        }
        if (!phoneNumber.equals(loggedInCustomer.getPhoneNumber())) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Phone number does not match your account!");
            return;
        }
        try {
            List<Account> accounts = accountDAO.getAccountsByCustomerId(loggedInCustomer.getId());
            for (Account account : accounts) {
                if (account.getAccountType().equals(accountType)) {
                    messageLabel.setStyle("-fx-text-fill: red;");
                    messageLabel.setText("You already have a " + accountType + " account!");
                    return;
                }
            }
            Account account = new Account(0, loggedInCustomer.getId(), accountType, 0.0, "");
            int accountId = accountDAO.createAccount(account);
            hideAllSections();
            createSection.setVisible(true);
            createSection.setManaged(true);
            createTitle.setText(accountType + " Account Created!");
            createMessage.setText("Your account ID is: " + accountId);
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Account created successfully!");
            phoneNumberField.clear();
        } catch (SQLException e) {
            messageLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    protected void handleViewBalance() {
        hideAllSections();
        balanceSection.setVisible(true);
        balanceSection.setManaged(true);
    }

    @FXML
    protected void handleCheckBalance() {
        String accountIdText = accountIdField.getText().trim();
        if (accountIdText.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Account ID is required!");
            return;
        }
        try {
            int accountId = Integer.parseInt(accountIdText);
            Account account = accountDAO.getAccountById(accountId);
            if (account == null) {
                balanceLabel.setText("Account not found!");
                return;
            }
            if (account.getCustomerId() != loggedInCustomer.getId()) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("You can only view your own account!");
                return;
            }
            balanceLabel.setText(
                    "Account ID: " + account.getId() + "\n" +
                            "Type: " + account.getAccountType() + "\n" +
                            "Balance: " + account.getBalance() + " RWF"
            );
        } catch (NumberFormatException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Please enter a valid account ID!");
        } catch (SQLException e) {
            messageLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    protected void handleMyAccounts() {
        try {
            hideAllSections();
            myAccountsSection.setVisible(true);
            myAccountsSection.setManaged(true);
            List<Account> accounts = accountDAO.getAccountsByCustomerId(loggedInCustomer.getId());
            accountsTableRows.getChildren().clear();
            if (accounts.isEmpty()) {
                Label noData = new Label("No accounts found!");
                noData.setStyle("-fx-text-fill: #7F8C8D; -fx-font-size: 13;");
                accountsTableRows.getChildren().add(noData);
                return;
            }
            boolean alternate = false;
            for (Account account : accounts) {
                HBox row = new HBox();
                row.setStyle("-fx-padding: 8; -fx-background-radius: 4; -fx-background-color: " + (alternate ? "#F2F3F4" : "#FDFEFE") + ";");
                Label idLabel = new Label(String.valueOf(account.getId()));
                idLabel.setStyle("-fx-pref-width: 50;");
                Label typeLabel = new Label(account.getAccountType());
                typeLabel.setStyle("-fx-pref-width: 150; -fx-font-weight: bold; -fx-text-fill: " +
                        (account.getAccountType().equals("WALLET") ? "#2980B9" : "#27AE60") + ";");
                Label balLabel = new Label(account.getBalance() + " RWF");
                balLabel.setStyle("-fx-pref-width: 150;");
                Label dateLabel = new Label(account.getCreatedAt());
                dateLabel.setStyle("-fx-pref-width: 200;");
                row.getChildren().addAll(idLabel, typeLabel, balLabel, dateLabel);
                accountsTableRows.getChildren().add(row);
                alternate = !alternate;
            }
        } catch (SQLException e) {
            messageLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    protected void handleDeleteAccount() {
        hideAllSections();
        deleteSection.setVisible(true);
        deleteSection.setManaged(true);
    }

    @FXML
    protected void handleConfirmDelete() {
        String accountIdText = deleteAccountIdField.getText().trim();
        if (accountIdText.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Account ID is required!");
            return;
        }
        try {
            int accountId = Integer.parseInt(accountIdText);
            Account account = accountDAO.getAccountById(accountId);
            if (account == null) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Account not found!");
                return;
            }
            if (account.getCustomerId() != loggedInCustomer.getId()) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("You can only delete your own account!");
                return;
            }
            if (account.getBalance() > 0) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Cannot delete account with balance! Please withdraw all funds first.");
                return;
            }
            accountDAO.deleteAccount(accountId);
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Account deleted successfully!");
            deleteAccountIdField.clear();
            hideAllSections();
            createSection.setVisible(true);
            createSection.setManaged(true);
            createTitle.setText("Account Management");
            createMessage.setText("Select an option from the sidebar.");
        } catch (NumberFormatException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Please enter a valid account ID!");
        } catch (SQLException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
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