package com.igirepay.lab3.ui;

import com.igirepay.lab1.model.Account;
import com.igirepay.lab1.model.Customer;
import com.igirepay.lab2.dao.AccountDao;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class AccountMenu {

    private Scanner scanner;
    private Customer loggedInCustomer;
    private AccountDao accountDAO;

    public AccountMenu(Scanner scanner, Customer loggedInCustomer) {
        this.scanner = scanner;
        this.loggedInCustomer = loggedInCustomer;
        this.accountDAO = new AccountDao();
    }
    public void show() {
        System.out.println("\nAccount Management");
        System.out.println("1. Create Wallet Account");
        System.out.println("2. Create Savings Account");
        System.out.println("3. View Account Balance");
        System.out.println("4. Delete Inactive Account");
        System.out.println("5. Back to Main Menu");
        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                createWalletAccount();
                break;
            case "2":
                createSavingsAccount();
                break;
            case "3":
                viewBalance();
                break;
            case "4":
                deleteAccount();
                break;
            case "5":
                break;
            default:
                System.out.println("Invalid option! Try again.");
                show();
        }
    }
    public void createWalletAccount() {
        try {
            Account account = new Account(0, loggedInCustomer.getId(), "WALLET", 0.0, "");
            int accountId = accountDAO.createAccount(account);
            System.out.println("Wallet account created successfully!");
            System.out.println("Your account ID is: " + accountId);
        } catch (SQLException e) {
            System.out.println("Error creating wallet account: " + e.getMessage());
        }
    }

    public void createSavingsAccount() {
        try {
            Account account = new Account(0, loggedInCustomer.getId(), "SAVINGS", 0.0, "");
            int accountId = accountDAO.createAccount(account);
            System.out.println("Savings account created successfully!");
            System.out.println("Your account ID is: " + accountId);
        } catch (SQLException e) {
            System.out.println("Error creating savings account: " + e.getMessage());
        }
    }

    public void viewBalance() {
        System.out.print("Enter account ID: ");
        int accountId = Integer.parseInt(scanner.nextLine());
        try {
            Account account = accountDAO.getAccountById(accountId);
            if (account == null) {
                System.out.println("Account not found!");
                return;
            }
            System.out.println("Account Type: " + account.getAccountType());
            System.out.println("Balance: " + account.getBalance() + " RWF");
        } catch (SQLException e) {
            System.out.println("Error viewing balance: " + e.getMessage());
        }
    }

    public void deleteAccount() {
        System.out.print("Enter account ID to delete: ");
        int accountId = Integer.parseInt(scanner.nextLine());
        try {
            accountDAO.deleteAccount(accountId);
        } catch (SQLException e) {
            System.out.println("Error deleting account: " + e.getMessage());
        }
    }
}
