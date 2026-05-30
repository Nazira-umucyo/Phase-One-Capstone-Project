package com.igirepay.lab3.ui;

import com.igirepay.lab1.model.Customer;
import com.igirepay.lab3.service.AuthService;

import javax.management.relation.Role;
import java.sql.SQLException;
import java.util.Scanner;

public class MainMenu {

        private Scanner scanner;
        private AuthService authService;
        private Customer loggedInCustomer;

    public MainMenu() {
        this.scanner = new Scanner(System.in);
        this.authService = new AuthService();
    }
    public void start() {
        System.out.println("  Welcome to IgirePay System");
        login();
    }
    public void login() {
        System.out.println("\n===== Welcome =====");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                performLogin();
                break;
            case "2":
                register();
                break;
            default:
                System.out.println("Invalid option!");
                login();
        }
    }
    public void performLogin() {
        System.out.print("Enter phone number: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine();
        try {
            loggedInCustomer = authService.login(phoneNumber, pin);
            if (loggedInCustomer != null) {
                showMainMenu();
            } else {
                System.out.println("Login failed! Try again.");
                performLogin();
            }
        } catch (SQLException e) {
            System.out.println("Error during login: " + e.getMessage());
        }
    }

    public void register() {
        System.out.println("\n===== Register =====");
        System.out.print("Enter full name: ");
        String fullName = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter phone number: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Create PIN: ");
        String pin = scanner.nextLine();
        try {
            Customer customer = new Customer(0, fullName, email, phoneNumber, pin, "USER");
            authService.registerCustomer(customer);
            System.out.println("Registration successful! Please login.");
            performLogin();
        } catch (SQLException e) {
            System.out.println("Error during registration: " + e.getMessage());
        }
    }
    public void showMainMenu() {
        System.out.println("\nMain Menu");
        System.out.println("1. Customer Management");
        System.out.println("2. Account Management");
        System.out.println("3. Transaction Management");
        System.out.println("4. Reports");
        System.out.println("5. Change PIN");
        System.out.println("6. Logout");
        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                new CustomerMenu(scanner, loggedInCustomer).show();
                showMainMenu();
                break;
            case "2":
                new AccountMenu(scanner, loggedInCustomer).show();
                showMainMenu();
                break;
            case "3":
                new TransactionMenu(scanner, loggedInCustomer).show();
                showMainMenu();
                break;
            case "4":
                new ReportMenu(scanner, loggedInCustomer).show();
                showMainMenu();
                break;
            case "5":
                changePin();
                showMainMenu();
                break;
            case "6":
                System.out.println("Logged out.");
                scanner.close();
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option! Try again.");
                showMainMenu();
        }
    }
    public void changePin() {
        System.out.println("\n===== Change PIN =====");
        System.out.print("Enter old PIN: ");
        String oldPin = scanner.nextLine();
        System.out.print("Enter new PIN: ");
        String newPin = scanner.nextLine();
        try {
            authService.changePin(loggedInCustomer, oldPin, newPin);
        } catch (SQLException e) {
            System.out.println("Error changing PIN: " + e.getMessage());
        }
    }
}
