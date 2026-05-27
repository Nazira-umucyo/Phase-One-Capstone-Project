package com.igirepay.lab3.ui;

import com.igirepay.lab1.model.Customer;
import com.igirepay.lab1.model.Transaction;
import com.igirepay.lab2.dao.TransactionDao;
import com.igirepay.lab3.service.TransactionService;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class TransactionMenu {
    private Scanner scanner;
    private Customer loggedInCustomer;
    private TransactionService transactionService;
    private TransactionDao transactionDAO;

    public TransactionMenu(Scanner scanner, Customer loggedInCustomer) {
        this.scanner = scanner;
        this.loggedInCustomer = loggedInCustomer;
        this.transactionService = new TransactionService();
        this.transactionDAO = new TransactionDao();
    }
    public void show() {
        System.out.println("\n===== Transaction Management =====");
        System.out.println("1. Deposit Money");
        System.out.println("2. Withdraw Money");
        System.out.println("3. Transfer Money");
        System.out.println("4. View Transaction History");
        System.out.println("5. Back to Main Menu");
        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                deposit();
                break;
            case "2":
                withdraw();
                break;
            case "3":
                transfer();
                break;
            case "4":
                viewHistory();
                break;
            case "5":
                break;
            default:
                System.out.println("Invalid option! Try again.");
                show();
        }
    }
    public void deposit() {
        System.out.print("Enter account ID: ");
        int accountId = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter amount: ");
        double amount = Double.parseDouble(scanner.nextLine());
        try {
            transactionService.deposit(accountId, amount);
        } catch (SQLException e) {
            System.out.println("Error during deposit: " + e.getMessage());
        }
    }

    public void withdraw() {
        System.out.print("Enter account ID: ");
        int accountId = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter amount: ");
        double amount = Double.parseDouble(scanner.nextLine());
        try {
            transactionService.withdraw(accountId, amount);
        } catch (SQLException e) {
            System.out.println("Error during withdrawal: " + e.getMessage());
        }
    }

    public void transfer() {
        System.out.print("Enter your account ID: ");
        int fromAccountId = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter recipient account ID: ");
        int toAccountId = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter amount: ");
        double amount = Double.parseDouble(scanner.nextLine());
        try {
            transactionService.transfer(fromAccountId, toAccountId, amount);
        } catch (SQLException e) {
            System.out.println("Error during transfer: " + e.getMessage());
        }
    }

    public void viewHistory() {
        System.out.print("Enter account ID: ");
        int accountId = Integer.parseInt(scanner.nextLine());
        try {
            List<Transaction> transactions = transactionDAO.getTransactionsByAccountId(accountId);
            if (transactions.isEmpty()) {
                System.out.println("No transactions found!");
                return;
            }
            System.out.println("\n===== Transaction History =====");
            for (Transaction transaction : transactions) {
                System.out.println("ID: " + transaction.getId());
                System.out.println("Reference: " + transaction.getReferenceId());
                System.out.println("Type: " + transaction.getTransactionType());
                System.out.println("Amount: " + transaction.getAmount() + " RWF");
                System.out.println("Date: " + transaction.getCreatedAt());
                System.out.println("-----------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Error viewing history: " + e.getMessage());
        }
    }
}
