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
import javafx.scene.layout.HBox;
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
    @FXML private VBox historyTableRows;
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
        messageLabel.setText("");
    }

    @FXML
    protected void handleDeposit() {
        hideAllSections();
        depositForm.setVisible(true);
        depositForm.setManaged(true);
    }

    @FXML
    protected void handleConfirmDeposit() {
        String accountIdText = depositAccountId.getText().trim();
        String amountText = depositAmount.getText().trim();
        if (accountIdText.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Account ID is required!");
            return;
        }
        if (amountText.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Amount is required!");
            return;
        }
        try {
            int accountId = Integer.parseInt(accountIdText);
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Amount must be greater than zero!");
                return;
            }
            String result = transactionService.deposit(accountId, amount, loggedInCustomer.getId());
            if (result.isEmpty() || result.startsWith("SUCCESS:")) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText(result.isEmpty() ? "Deposited successful!" : result.replace("SUCCESS:", ""));
            } else {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText(result);
            }
            depositAccountId.clear();
            depositAmount.clear();
        } catch (NumberFormatException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Please enter valid numbers!");
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
        String accountIdText = withdrawAccountId.getText().trim();
        String amountText = withdrawAmount.getText().trim();
        if (accountIdText.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Account ID is required!");
            return;
        }
        if (amountText.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Amount is required!");
            return;
        }
        try {
            int accountId = Integer.parseInt(accountIdText);
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Amount must be greater than zero!");
                return;
            }
            String result = transactionService.withdraw(accountId, amount, loggedInCustomer.getId());
            if (result.isEmpty() || result.startsWith("SUCCESS:")) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText(result.isEmpty() ? "withdrawn successful!" : result.replace("SUCCESS:", ""));
            } else {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText(result);
            }
            withdrawAccountId.clear();
            withdrawAmount.clear();
        } catch (NumberFormatException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Please enter valid numbers!");
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
        String fromText = fromAccountId.getText().trim();
        String toText = toAccountId.getText().trim();
        String amountText = transferAmount.getText().trim();
        if (fromText.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Your Account ID is required!");
            return;
        }
        if (toText.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Recipient Account ID is required!");
            return;
        }
        if (amountText.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Amount is required!");
            return;
        }
        try {
            int from = Integer.parseInt(fromText);
            int to = Integer.parseInt(toText);
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Amount must be greater than zero!");
                return;
            }
            if (from == to) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Cannot transfer to the same account!");
                return;
            }
            String result = transactionService.transfer(from, to, amount, loggedInCustomer.getId());
            if (result.isEmpty() || result.startsWith("SUCCESS:")) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText(result.isEmpty() ? "transfered successfully!" : result.replace("SUCCESS:", ""));
            } else {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText(result);
            }
            fromAccountId.clear();
            toAccountId.clear();
            transferAmount.clear();
        } catch (NumberFormatException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Please enter valid numbers!");
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
        String accountIdText = historyAccountId.getText().trim();
        if (accountIdText.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Account ID is required!");
            return;
        }
        try {
            int accountId = Integer.parseInt(accountIdText);
            List<Transaction> transactions = transactionDAO.getTransactionsByAccountId(accountId);
            historyTableRows.getChildren().clear();
            if (transactions.isEmpty()) {
                Label noData = new Label("No transactions found!");
                noData.setStyle("-fx-text-fill: #7F8C8D; -fx-font-size: 13;");
                historyTableRows.getChildren().add(noData);
                return;
            }
            boolean alternate = false;
            for (Transaction transaction : transactions) {
                HBox row = new HBox();
                row.setStyle("-fx-padding: 8; -fx-background-radius: 4; -fx-background-color: " + (alternate ? "#F2F3F4" : "#FDFEFE") + ";");
                Label idLabel = new Label(String.valueOf(transaction.getId()));
                idLabel.setStyle("-fx-pref-width: 50;");
                Label typeLabel = new Label(transaction.getTransactionType());
                typeLabel.setStyle("-fx-pref-width: 100; -fx-font-weight: bold; -fx-text-fill: " +
                        (transaction.getTransactionType().equals("DEPOSIT") ? "#27AE60" :
                                transaction.getTransactionType().equals("WITHDRAW") ? "#E74C3C" : "#2980B9") + ";");
                Label amountLabel = new Label(transaction.getAmount() + " RWF");
                amountLabel.setStyle("-fx-pref-width: 120;");
                Label dateLabel = new Label(transaction.getCreatedAt());
                dateLabel.setStyle("-fx-pref-width: 200;");
                row.getChildren().addAll(idLabel, typeLabel, amountLabel, dateLabel);
                historyTableRows.getChildren().add(row);
                alternate = !alternate;
            }
            historyAccountId.clear();
        } catch (NumberFormatException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Please enter a valid account ID!");
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