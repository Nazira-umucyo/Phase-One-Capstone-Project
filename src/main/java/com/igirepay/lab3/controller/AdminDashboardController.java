package com.igirepay.lab3.controller;

import com.igirepay.lab1.model.Account;
import com.igirepay.lab1.model.Customer;
import com.igirepay.lab1.model.Transaction;
import com.igirepay.lab2.dao.AccountDao;
import com.igirepay.lab2.dao.CustomerDao;
import com.igirepay.lab2.dao.TransactionDao;
import com.igirepay.lab3.service.ReportService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminDashboardController {

    @FXML private VBox defaultSection;
    @FXML private VBox customersSection;
    @FXML private VBox customerAccountsSection;
    @FXML private VBox transactionsSection;
    @FXML private VBox statementSection;
    @FXML private VBox deleteAccountSection;
    @FXML private VBox customersTableRows;
    @FXML private VBox customerAccountsTableRows;
    @FXML private VBox transactionsTableRows;
    @FXML private Label messageLabel;
    @FXML private Text adminNameText;
    @FXML private Text totalCustomersText;
    @FXML private Text totalAccountsText;
    @FXML private Text totalTransactionsText;
    @FXML private TextField customerSearchField;
    @FXML private TextField transactionSearchField;
    @FXML private TextField customerIdField;
    @FXML private TextField deleteAccountIdField;
    @FXML private TextField statementCustomerIdField;
    @FXML private TextField statementFileName;

    private Customer loggedInCustomer;
    private CustomerDao customerDAO = new CustomerDao();
    private AccountDao accountDAO = new AccountDao();
    private TransactionDao transactionDAO = new TransactionDao();
    private ReportService reportService = new ReportService();

    public void setCustomer(Customer customer) {
        this.loggedInCustomer = customer;
        adminNameText.setText(customer.getFullName());
        defaultSection.setPrefWidth(Double.MAX_VALUE);
        customersSection.setPrefWidth(Double.MAX_VALUE);
        customerAccountsSection.setPrefWidth(Double.MAX_VALUE);
        transactionsSection.setPrefWidth(Double.MAX_VALUE);
        statementSection.setPrefWidth(Double.MAX_VALUE);
        deleteAccountSection.setPrefWidth(Double.MAX_VALUE);
        loadStats();
    }

    private void loadStats() {
        try {
            List<Customer> customers = customerDAO.getAllCustomers();
            List<Account> accounts = accountDAO.getAllAccounts();
            List<Transaction> transactions = transactionDAO.getAllTransactions();
            totalCustomersText.setText(String.valueOf(customers.size()));
            totalAccountsText.setText(String.valueOf(accounts.size()));
            totalTransactionsText.setText(String.valueOf(transactions.size()));
        } catch (SQLException e) {
            messageLabel.setText("Error loading stats: " + e.getMessage());
        }
    }

    private void hideAllSections() {
        defaultSection.setVisible(false);
        defaultSection.setManaged(false);
        customersSection.setVisible(false);
        customersSection.setManaged(false);
        customerAccountsSection.setVisible(false);
        customerAccountsSection.setManaged(false);
        transactionsSection.setVisible(false);
        transactionsSection.setManaged(false);
        statementSection.setVisible(false);
        statementSection.setManaged(false);
        deleteAccountSection.setVisible(false);
        deleteAccountSection.setManaged(false);
        messageLabel.setText("");
    }

    @FXML
    protected void handleDashboard() {
        hideAllSections();
        defaultSection.setVisible(true);
        defaultSection.setManaged(true);
        loadStats();
    }

    @FXML
    protected void handleAllCustomers() {
        try {
            hideAllSections();
            customersSection.setVisible(true);
            customersSection.setManaged(true);
            loadCustomersTable(customerDAO.getAllCustomers());
        } catch (SQLException e) {
            messageLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    protected void handleSearchCustomers() {
        String searchText = customerSearchField.getText().trim();
        if (searchText.isEmpty()) {
            try {
                loadCustomersTable(customerDAO.getAllCustomers());
            } catch (SQLException e) {
                messageLabel.setText("Error: " + e.getMessage());
            }
            return;
        }
        try {
            List<Customer> customers = customerDAO.getAllCustomers();
            List<Customer> filtered = new ArrayList<>();
            for (Customer customer : customers) {
                if (customer.getFullName().toLowerCase().contains(searchText.toLowerCase()) ||
                        customer.getPhoneNumber().contains(searchText)) {
                    filtered.add(customer);
                }
            }
            loadCustomersTable(filtered);
        } catch (SQLException e) {
            messageLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    protected void handleShowAllCustomers() {
        try {
            customerSearchField.clear();
            loadCustomersTable(customerDAO.getAllCustomers());
        } catch (SQLException e) {
            messageLabel.setText("Error: " + e.getMessage());
        }
    }

    private void loadCustomersTable(List<Customer> customers) {
        customersTableRows.getChildren().clear();
        if (customers.isEmpty()) {
            Label noData = new Label("No customers found!");
            noData.setStyle("-fx-text-fill: #7F8C8D; -fx-font-size: 13;");
            customersTableRows.getChildren().add(noData);
            return;
        }
        boolean alternate = false;
        for (Customer customer : customers) {
            HBox row = new HBox();
            row.setStyle("-fx-padding: 10; -fx-background-radius: 4; -fx-background-color: " + (alternate ? "#F8F9FA" : "white") + ";");
            Label idLabel = new Label(String.valueOf(customer.getId()));
            idLabel.setStyle("-fx-pref-width: 50;");
            Label nameLabel = new Label(customer.getFullName());
            nameLabel.setStyle("-fx-pref-width: 180;");
            Label emailLabel = new Label(customer.getEmail());
            emailLabel.setStyle("-fx-pref-width: 220;");
            Label phoneLabel = new Label(customer.getPhoneNumber());
            phoneLabel.setStyle("-fx-pref-width: 150;");
            Label roleLabel = new Label(customer.getRole());
            roleLabel.setStyle("-fx-pref-width: 80; -fx-font-weight: bold; -fx-text-fill: " +
                    (customer.getRole().equals("ADMIN") ? "#E74C3C" : "#2980B9") + ";");
            row.getChildren().addAll(idLabel, nameLabel, emailLabel, phoneLabel, roleLabel);
            customersTableRows.getChildren().add(row);
            alternate = !alternate;
        }
    }

    @FXML
    protected void handleCustomerAccounts() {
        hideAllSections();
        customerAccountsSection.setVisible(true);
        customerAccountsSection.setManaged(true);
    }

    @FXML
    protected void handleViewCustomerAccounts() {
        String customerIdText = customerIdField.getText().trim();
        if (customerIdText.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Customer ID is required!");
            return;
        }
        try {
            int customerId = Integer.parseInt(customerIdText);
            List<Account> accounts = accountDAO.getAccountsByCustomerId(customerId);
            customerAccountsTableRows.getChildren().clear();
            if (accounts.isEmpty()) {
                Label noData = new Label("No accounts found for this customer!");
                noData.setStyle("-fx-text-fill: #7F8C8D; -fx-font-size: 13;");
                customerAccountsTableRows.getChildren().add(noData);
                return;
            }
            boolean alternate = false;
            for (Account account : accounts) {
                HBox row = new HBox();
                row.setStyle("-fx-padding: 10; -fx-background-radius: 4; -fx-background-color: " + (alternate ? "#F8F9FA" : "white") + ";");
                Label idLabel = new Label(String.valueOf(account.getId()));
                idLabel.setStyle("-fx-pref-width: 50;");
                Label typeLabel = new Label(account.getAccountType());
                typeLabel.setStyle("-fx-pref-width: 150; -fx-font-weight: bold; -fx-text-fill: " +
                        (account.getAccountType().equals("WALLET") ? "#2980B9" : "#27AE60") + ";");
                Label balLabel = new Label(account.getBalance() + " RWF");
                balLabel.setStyle("-fx-pref-width: 200;");
                Label dateLabel = new Label(account.getCreatedAt());
                dateLabel.setStyle("-fx-pref-width: 250;");
                row.getChildren().addAll(idLabel, typeLabel, balLabel, dateLabel);
                customerAccountsTableRows.getChildren().add(row);
                alternate = !alternate;
            }
        } catch (NumberFormatException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Please enter a valid customer ID!");
        } catch (SQLException e) {
            messageLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    protected void handleAllTransactions() {
        try {
            hideAllSections();
            transactionsSection.setVisible(true);
            transactionsSection.setManaged(true);
            loadTransactionsTable(transactionDAO.getAllTransactions());
        } catch (SQLException e) {
            messageLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    protected void handleSearchTransactions() {
        String searchText = transactionSearchField.getText().trim();
        if (searchText.isEmpty()) {
            try {
                loadTransactionsTable(transactionDAO.getAllTransactions());
            } catch (SQLException e) {
                messageLabel.setText("Error: " + e.getMessage());
            }
            return;
        }
        try {
            List<Customer> customers = customerDAO.getAllCustomers();
            List<Transaction> allTransactions = transactionDAO.getAllTransactions();
            List<Account> allAccounts = accountDAO.getAllAccounts();
            List<Transaction> filtered = new ArrayList<>();
            for (Customer customer : customers) {
                if (customer.getFullName().toLowerCase().contains(searchText.toLowerCase())) {
                    for (Account account : allAccounts) {
                        if (account.getCustomerId() == customer.getId()) {
                            for (Transaction transaction : allTransactions) {
                                if (transaction.getAccountId() == account.getId()) {
                                    filtered.add(transaction);
                                }
                            }
                        }
                    }
                }
            }
            loadTransactionsTable(filtered);
        } catch (SQLException e) {
            messageLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    protected void handleShowAllTransactions() {
        try {
            transactionSearchField.clear();
            loadTransactionsTable(transactionDAO.getAllTransactions());
        } catch (SQLException e) {
            messageLabel.setText("Error: " + e.getMessage());
        }
    }

    private void loadTransactionsTable(List<Transaction> transactions) {
        transactionsTableRows.getChildren().clear();
        if (transactions.isEmpty()) {
            Label noData = new Label("No transactions found!");
            noData.setStyle("-fx-text-fill: #7F8C8D; -fx-font-size: 13;");
            transactionsTableRows.getChildren().add(noData);
            return;
        }
        try {
            List<Customer> customers = customerDAO.getAllCustomers();
            List<Account> accounts = accountDAO.getAllAccounts();
            boolean alternate = false;
            for (Transaction transaction : transactions) {
                String customerName = "Unknown";
                for (Account account : accounts) {
                    if (account.getId() == transaction.getAccountId()) {
                        for (Customer customer : customers) {
                            if (customer.getId() == account.getCustomerId()) {
                                customerName = customer.getFullName();
                                break;
                            }
                        }
                        break;
                    }
                }
                HBox row = new HBox();
                row.setStyle("-fx-padding: 10; -fx-background-radius: 4; -fx-background-color: " + (alternate ? "#F8F9FA" : "white") + ";");
                Label idLabel = new Label(String.valueOf(transaction.getId()));
                idLabel.setStyle("-fx-pref-width: 40;");
                Label custLabel = new Label(customerName);
                custLabel.setStyle("-fx-pref-width: 150;");
                Label accLabel = new Label(String.valueOf(transaction.getAccountId()));
                accLabel.setStyle("-fx-pref-width: 60;");
                Label typeLabel = new Label(transaction.getTransactionType());
                typeLabel.setStyle("-fx-pref-width: 100; -fx-font-weight: bold; -fx-text-fill: " +
                        (transaction.getTransactionType().equals("DEPOSIT") ? "#27AE60" :
                                transaction.getTransactionType().equals("WITHDRAW") ? "#E74C3C" : "#2980B9") + ";");
                Label amountLabel = new Label(transaction.getAmount() + " RWF");
                amountLabel.setStyle("-fx-pref-width: 120;");
                Label refLabel = new Label(transaction.getReferenceId());
                refLabel.setStyle("-fx-pref-width: 200;");
                Label dateLabel = new Label(transaction.getCreatedAt());
                dateLabel.setStyle("-fx-pref-width: 180;");
                row.getChildren().addAll(idLabel, custLabel, accLabel, typeLabel, amountLabel, refLabel, dateLabel);
                transactionsTableRows.getChildren().add(row);
                alternate = !alternate;
            }
        } catch (SQLException e) {
            messageLabel.setText("Error loading data: " + e.getMessage());
        }
    }

    @FXML
    protected void handleTransactionStatement() {
        hideAllSections();
        statementSection.setVisible(true);
        statementSection.setManaged(true);
    }

    @FXML
    protected void handleExportStatement() {
        String customerIdText = statementCustomerIdField.getText().trim();
        String fileName = statementFileName.getText().trim();
        if (customerIdText.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Customer ID is required!");
            return;
        }
        if (fileName.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("File name is required!");
            return;
        }
        if (!fileName.endsWith(".csv")) {
            fileName = fileName + ".csv";
        }
        try {
            int customerId = Integer.parseInt(customerIdText);
            List<Account> accounts = accountDAO.getAccountsByCustomerId(customerId);
            if (accounts.isEmpty()) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("No accounts found for this customer!");
                return;
            }
            for (Account account : accounts) {
                reportService.exportToCSV(account.getId(), fileName.replace(".csv", "_account" + account.getId() + ".csv"));
            }
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Statement exported successfully!");
            statementCustomerIdField.clear();
            statementFileName.clear();
        } catch (NumberFormatException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Please enter a valid customer ID!");
        } catch (SQLException e) {
            messageLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    protected void handleDeleteAccount() {
        hideAllSections();
        deleteAccountSection.setVisible(true);
        deleteAccountSection.setManaged(true);
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
            if (account.getBalance() > 0) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Cannot delete account with balance! Balance must be 0.");
                return;
            }
            accountDAO.deleteAccount(accountId);
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Account deleted successfully!");
            deleteAccountIdField.clear();
        } catch (NumberFormatException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Please enter a valid account ID!");
        } catch (SQLException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    protected void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Stage stage = (Stage) messageLabel.getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 700, 500));
        } catch (Exception e) {
            messageLabel.setText("Error logging out!");
        }
    }
}