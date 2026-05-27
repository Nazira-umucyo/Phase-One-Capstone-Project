package com.igirepay;

import com.igirepay.lab1.model.Customer;
import com.igirepay.lab1.model.Transaction;
import com.igirepay.lab2.dao.TransactionDao;
import com.igirepay.lab3.service.TransactionService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.List;

public class TransactionController {

    @FXML private VBox depositSection;
    @FXML private VBox depositForm;
    @FXML private VBox withdrawForm;
    @FXML private VBox transferForm;
    @FXML private VBox historySection;
    @FXML private Label messageLabel;
    @FXML private Label historyLabel;
    @FXML private TextField depositAccountId;
    @FXML private TextField depositAmount;
    @FXML private TextField withdrawAccountId;
    @FXML private TextField withdrawAmount;
    @FXML private TextField fromAccountId;
    @FXML private TextField toAccountId;
    @FXML private TextField transferAmount;
    @FXML private TextField historyAccountId;

    private Customer loggedInCustomer;
    private TransactionService transactionService = new TransactionService();
    private TransactionDao transactionDAO = new TransactionDao();

    public void setCustomer(Customer customer) {
        this.loggedInCustomer = customer;
        depositSection.setPrefWidth(Double.MAX_VALUE);
        depositForm.setPrefWidth(Double.MAX_VALUE);
        withdrawForm.setPrefWidth(Double.MAX_VALUE);
        transferForm.setPrefWidth(Double.MAX_VALUE);
        historySection.setPrefWidth(Double.MAX_VALUE);
    }

    private void hideAllSections() {
        depositSection.setVisible(false);
        depositSection.setManaged(false);
        depositForm.setVisible(false);
        depositForm.setManaged(false);
        withdrawForm.setVisible(false);
        withdrawForm.setManaged(false);
        transferForm.setVisible(false);
        transferForm.setManaged(false);
        historySection.setVisible(false);
        historySection.setManaged(false);
    }

    @FXML
    protected void handleDeposit() {
        hideAllSections();
        depositForm.setVisible(true);
        depositForm.setManaged(true);
    }

    @FXML
    protected void handleConfirmDeposit() {
        try {
            int accountId = Integer.parseInt(depositAccountId.getText());
            double amount = Double.parseDouble(depositAmount.getText());
            transactionService.deposit(accountId, amount);
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Deposit successful!");
            depositAccountId.clear();
            depositAmount.clear();
        } catch (SQLException e) {
            messageLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    protected void handleWithdraw() {
        hideAllSections();
        withdrawForm.setVisible(true);
        withdrawForm.setManaged(true);
    }

    @FXML
    protected void handleConfirmWithdraw() {
        try {
            int accountId = Integer.parseInt(withdrawAccountId.getText());
            double amount = Double.parseDouble(withdrawAmount.getText());
            transactionService.withdraw(accountId, amount);
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Withdrawal successful!");
            withdrawAccountId.clear();
            withdrawAmount.clear();
        } catch (SQLException e) {
            messageLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    protected void handleTransfer() {
        hideAllSections();
        transferForm.setVisible(true);
        transferForm.setManaged(true);
    }

    @FXML
    protected void handleConfirmTransfer() {
        try {
            int from = Integer.parseInt(fromAccountId.getText());
            int to = Integer.parseInt(toAccountId.getText());
            double amount = Double.parseDouble(transferAmount.getText());
            transactionService.transfer(from, to, amount);
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Transfer successful!");
            fromAccountId.clear();
            toAccountId.clear();
            transferAmount.clear();
        } catch (SQLException e) {
            messageLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    protected void handleHistory() {
        hideAllSections();
        historySection.setVisible(true);
        historySection.setManaged(true);
    }

    @FXML
    protected void handleViewHistory() {
        try {
            int accountId = Integer.parseInt(historyAccountId.getText());
            List<Transaction> transactions = transactionDAO.getTransactionsByAccountId(accountId);
            if (transactions.isEmpty()) {
                historyLabel.setText("No transactions found!");
                return;
            }
            StringBuilder sb = new StringBuilder();
            for (Transaction transaction : transactions) {
                sb.append("ID: ").append(transaction.getId())
                        .append(" | Type: ").append(transaction.getTransactionType())
                        .append(" | Amount: ").append(transaction.getAmount()).append(" RWF")
                        .append(" | Date: ").append(transaction.getCreatedAt()).append("\n");
            }
            historyLabel.setText(sb.toString());
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