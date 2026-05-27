package com.igirepay.lab3.ui;

import com.igirepay.lab1.model.Customer;
import com.igirepay.lab3.service.ReportService;
import java.sql.SQLException;
import java.util.Scanner;

public class ReportMenu {

    private Scanner scanner;
    private Customer loggedInCustomer;
    private ReportService reportService;

    public ReportMenu(Scanner scanner, Customer loggedInCustomer) {
        this.scanner = scanner;
        this.loggedInCustomer = loggedInCustomer;
        this.reportService = new ReportService();
    }

    public void show() {
        System.out.println("\n===== Reports =====");
        System.out.println("1. Export Transaction History to CSV");
        System.out.println("2. View Daily Summary");
        System.out.println("3. Back to Main Menu");
        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                exportToCSV();
                break;
            case "2":
                viewDailySummary();
                break;
            case "3":
                break;
            default:
                System.out.println("Invalid option! Try again.");
                show();
        }
    }

    public void exportToCSV() {
        System.out.print("Enter account ID: ");
        int accountId = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter file name (e.g. transactions.csv): ");
        String fileName = scanner.nextLine();
        try {
            reportService.exportToCSV(accountId, fileName);
        } catch (SQLException e) {
            System.out.println("Error exporting to CSV: " + e.getMessage());
        }
    }

    public void viewDailySummary() {
        try {
            reportService.viewDailySummary(loggedInCustomer.getId());
        } catch (SQLException e) {
            System.out.println("Error viewing daily summary: " + e.getMessage());
        }
    }
}
