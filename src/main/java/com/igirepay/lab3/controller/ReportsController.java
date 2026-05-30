package com.igirepay;

import com.igirepay.lab1.model.Account;
import com.igirepay.lab1.model.Customer;
import com.igirepay.lab1.model.Transaction;
import com.igirepay.lab2.dao.AccountDao;
import com.igirepay.lab2.dao.TransactionDao;
import com.igirepay.lab3.service.ReportService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.List;

public class ReportsController {

    @FXML private VBox defaultSection;
    @FXML private VBox summarySection;
    @FXML private VBox exportSection;
    @FXML private Label depositsLabel;
    @FXML private Label withdrawalsLabel;
    @FXML private Label transfersLabel;
    @FXML private Label totalLabel;
    @FXML private Label messageLabel;
    @FXML private TextField exportAccountId;
    @FXML private TextField exportFileName;

    private Customer loggedInCustomer;
    private ReportService reportService = new ReportService();

    public void setCustomer(Customer customer) {
        this.loggedInCustomer = customer;
        defaultSection.setPrefWidth(Double.MAX_VALUE);
        summarySection.setPrefWidth(Double.MAX_VALUE);
        exportSection.setPrefWidth(Double.MAX_VALUE);
    }

    private void hideAllSections() {
        defaultSection.setVisible(false);
        defaultSection.setManaged(false);
        summarySection.setVisible(false);
        summarySection.setManaged(false);
        exportSection.setVisible(false);
        exportSection.setManaged(false);
        messageLabel.setText("");
    }

    @FXML
    protected void handleDailySummary() {
        try {
            hideAllSections();
            summarySection.setVisible(true);
            summarySection.setManaged(true);
            List<Account> accounts = new AccountDao().getAccountsByCustomerId(loggedInCustomer.getId());
            double totalDeposits = 0;
            double totalWithdrawals = 0;
            double totalTransfers = 0;
            for (Account account : accounts) {
                List<Transaction> transactions = new TransactionDao().getTransactionsByAccountId(account.getId());
                for (Transaction transaction : transactions) {
                    if (transaction.getTransactionType().equals("DEPOSIT")) {
                        totalDeposits += transaction.getAmount();
                    } else if (transaction.getTransactionType().equals("WITHDRAW")) {
                        totalWithdrawals += transaction.getAmount();
                    } else if (transaction.getTransactionType().equals("TRANSFER")) {
                        totalTransfers += transaction.getAmount();
                    }
                }
            }
            double total = totalDeposits + totalWithdrawals + totalTransfers;
            depositsLabel.setText(totalDeposits + " RWF");
            withdrawalsLabel.setText(totalWithdrawals + " RWF");
            transfersLabel.setText(totalWithdrawals + " RWF");
            transfersLabel.setText(totalTransfers + " RWF");
            totalLabel.setText(total + " RWF");
        } catch (SQLException e) {
            messageLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    protected void handleExportCSV() {
        hideAllSections();
        exportSection.setVisible(true);
        exportSection.setManaged(true);
    }

    @FXML
    protected void handleConfirmExport() {
        try {
            int accountId = Integer.parseInt(exportAccountId.getText());
            String fileName = exportFileName.getText();
            reportService.exportToCSV(accountId, fileName);
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Exported successfully to " + fileName);
            exportAccountId.clear();
            exportFileName.clear();
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