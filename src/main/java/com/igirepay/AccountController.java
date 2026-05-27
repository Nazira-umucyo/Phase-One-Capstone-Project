package com.igirepay;

import com.igirepay.lab1.model.Account;
import com.igirepay.lab1.model.Customer;
import com.igirepay.lab2.dao.AccountDao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.sql.SQLException;

public class AccountController {

    @FXML private VBox createSection;
    @FXML private VBox balanceSection;
    @FXML private VBox deleteSection;
    @FXML private Label messageLabel;
    @FXML private Label balanceLabel;
    @FXML private Label createMessage;
    @FXML private Text createTitle;
    @FXML private TextField accountIdField;
    @FXML private TextField deleteAccountIdField;

    private Customer loggedInCustomer;
    private AccountDao accountDAO = new AccountDao();

    public void setCustomer(Customer customer) {
        this.loggedInCustomer = customer;
        createSection.setPrefWidth(Double.MAX_VALUE);
        balanceSection.setPrefWidth(Double.MAX_VALUE);
        deleteSection.setPrefWidth(Double.MAX_VALUE);
    }

    private void hideAllSections() {
        createSection.setVisible(false);
        createSection.setManaged(false);
        balanceSection.setVisible(false);
        balanceSection.setManaged(false);
        deleteSection.setVisible(false);
        deleteSection.setManaged(false);
    }

    @FXML
    protected void handleCreateWallet() {
        try {
            Account account = new Account(0, loggedInCustomer.getId(), "WALLET", 0.0, "");
            int accountId = accountDAO.createAccount(account);
            hideAllSections();
            createSection.setVisible(true);
            createSection.setManaged(true);
            createTitle.setText("Wallet Account Created!");
            createMessage.setText("Your account ID is: " + accountId);
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Wallet account created successfully!");
        } catch (SQLException e) {
            messageLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    protected void handleCreateSavings() {
        try {
            Account account = new Account(0, loggedInCustomer.getId(), "SAVINGS", 0.0, "");
            int accountId = accountDAO.createAccount(account);
            hideAllSections();
            createSection.setVisible(true);
            createSection.setManaged(true);
            createTitle.setText("Savings Account Created!");
            createMessage.setText("Your account ID is: " + accountId);
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Savings account created successfully!");
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
        try {
            int accountId = Integer.parseInt(accountIdField.getText());
            Account account = accountDAO.getAccountById(accountId);
            if (account == null) {
                balanceLabel.setText("Account not found!");
                return;
            }
            balanceLabel.setText(
                    "Account ID: " + account.getId() + "\n" +
                            "Type: " + account.getAccountType() + "\n" +
                            "Balance: " + account.getBalance() + " RWF"
            );
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
        try {
            int accountId = Integer.parseInt(deleteAccountIdField.getText());
            accountDAO.deleteAccount(accountId);
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Account deleted successfully!");
            hideAllSections();
            createSection.setVisible(true);
            createSection.setManaged(true);
            createTitle.setText("Account Management");
            createMessage.setText("Select an option from the sidebar.");
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