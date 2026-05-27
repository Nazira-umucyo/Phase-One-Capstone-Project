package com.igirepay.lab3.service;

import com.igirepay.lab1.model.Transaction;
import com.igirepay.lab2.dao.TransactionDao;
import com.igirepay.lab2.dao.AccountDao;
import com.igirepay.lab1.model.Account;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

public class ReportService {

    private TransactionDao transactionDAO;
    private AccountDao accountDAO;

    public ReportService() {
        this.transactionDAO = new TransactionDao();
        this.accountDAO = new AccountDao();
    }
    public void exportToCSV(int accountId, String fileName) throws SQLException {
        try {
            List<Transaction> transactions = transactionDAO.getTransactionsByAccountId(accountId);
            FileWriter fileWriter = new FileWriter(fileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println("ID,Account ID,Reference ID,Transaction Type,Amount,Created At");
            for (Transaction transaction : transactions) {
                printWriter.println(
                        transaction.getId() + "," +
                                transaction.getAccountId() + "," +
                                transaction.getReferenceId() + "," +
                                transaction.getTransactionType() + "," +
                                transaction.getAmount() + "," +
                                transaction.getCreatedAt()
                );
            }
            printWriter.close();
            System.out.println("Transaction history exported to " + fileName);
        } catch (IOException e) {
            System.out.println("Error exporting to CSV: " + e.getMessage());
        }
    }
    public void viewDailySummary(int customerId) throws SQLException {
        try {
            List<Account> accounts = accountDAO.getAccountsByCustomerId(customerId);
            double totalDeposits = 0;
            double totalWithdrawals = 0;
            double totalTransfers = 0;
            for (Account account : accounts) {
                List<Transaction> transactions = transactionDAO.getTransactionsByAccountId(account.getId());
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
            System.out.println("===== Daily Transaction Summary =====");
            System.out.println("Total Deposits: " + totalDeposits + " RWF");
            System.out.println("Total Withdrawals: " + totalWithdrawals + " RWF");
            System.out.println("Total Transfers: " + totalTransfers + " RWF");
        } catch (SQLException e) {
            System.out.println("Error viewing daily summary: " + e.getMessage());
            throw e;
        }
    }
}

