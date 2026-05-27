package com.igirepay.lab3.ui;

import com.igirepay.lab1.model.Account;
import com.igirepay.lab1.model.Customer;
import com.igirepay.lab2.dao.CustomerDao;
import com.igirepay.lab2.dao.AccountDao;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class CustomerMenu {
    private Scanner scanner;
    private Customer loggedInCustomer;
    private CustomerDao customerDAO;
    private AccountDao accountDAO;

    public CustomerMenu(Scanner scanner, Customer loggedInCustomer) {
        this.scanner = scanner;
        this.loggedInCustomer = loggedInCustomer;
        this.customerDAO = new CustomerDao();
        this.accountDAO = new AccountDao();
    }
    public void show() {
        System.out.println("\nCustomer Management");
        System.out.println("1. View My Profile");
        System.out.println("2. Update My Information");
        System.out.println("3. View My Accounts");
        System.out.println("4. Back to Main Menu");
        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                viewProfile();
                break;
            case "2":
                updateInformation();
                break;
            case "3":
                viewAccounts();
                break;
            case "4":
                break;
            default:
                System.out.println("Invalid option! Try again.");
                show();
        }
    }
    public void viewProfile() {
        System.out.println("\nMy Profile ");
        System.out.println("ID: " + loggedInCustomer.getId());
        System.out.println("Name: " + loggedInCustomer.getFullName());
        System.out.println("Email: " + loggedInCustomer.getEmail());
        System.out.println("Phone: " + loggedInCustomer.getPhoneNumber());
    }
    public void updateInformation() {
        System.out.println("\n===== Update Information =====");
        System.out.print("Enter new full name: ");
        String fullName = scanner.nextLine();
        System.out.print("Enter new email: ");
        String email = scanner.nextLine();
        System.out.print("Enter new phone number: ");
        String phoneNumber = scanner.nextLine();
        try {
            loggedInCustomer.setFullName(fullName);
            loggedInCustomer.setEmail(email);
            loggedInCustomer.setPhoneNumber(phoneNumber);
            customerDAO.updateCustomer(loggedInCustomer);
            System.out.println("Information updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error updating information: " + e.getMessage());
        }
    }
    public void viewAccounts() {
        System.out.println("\n===== My Accounts =====");
        try {
            List<Account> accounts = accountDAO.getAccountsByCustomerId(loggedInCustomer.getId());
            if (accounts.isEmpty()) {
                System.out.println("No accounts found!");
                return;
            }
            for (com.igirepay.lab1.model.Account account : accounts) {
                System.out.println("Account ID: " + account.getId());
                System.out.println("Type: " + account.getAccountType());
                System.out.println("Balance: " + account.getBalance() + " RWF");
                System.out.println("Created At: " + account.getCreatedAt());
            }
        } catch (SQLException e) {
            System.out.println("Error viewing accounts: " + e.getMessage());
        }
    }
}
