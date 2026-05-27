package com.igirepay.lab3.service;

import com.igirepay.lab1.model.Transaction;
import com.igirepay.lab2.dao.AccountDao;
import com.igirepay.lab2.dao.TransactionDao;
import com.igirepay.lab2.dao.ProcessedRequestDao;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransactionService {
    private TransactionDao transactionDao;
    private AccountDao accountDao;
    private ProcessedRequestDao processedRequestDao;

    public TransactionService() {
        this.transactionDao = new TransactionDao();
        this.accountDao = new AccountDao();
        this.processedRequestDao = new ProcessedRequestDao();
    }
    private String generateReferenceId() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return "IGP-" + now.format(formatter);
    }
    public void deposit(int accountId, double amount) throws SQLException {
        try {
            if (amount <= 0) {
                System.out.println("Deposit amount must be greater than zero.");
                return;
            }
            String referenceId = generateReferenceId();
            if (processedRequestDao.isRequestProcessed(referenceId)) {
                System.out.println("Duplicate transaction detected: " + referenceId);
                return;
            }
            com.igirepay.lab1.model.Account account = accountDao.getAccountById(accountId);
            if (account == null) {
                System.out.println("Account not found!");
                return;
            }
            double newBalance = account.getBalance() + amount;
            accountDao.updateBalance(accountId, newBalance);
            Transaction transaction = new Transaction(0, referenceId, amount, "DEPOSIT", accountId, LocalDateTime.now().toString());
            transactionDao.addTransaction(transaction);
            processedRequestDao.addProcessedRequest(referenceId);
            System.out.println("Deposit successful! New balance: " + newBalance);
        } catch (SQLException e) {
            System.out.println("Error during deposit: " + e.getMessage());
            throw e;
        }
    }
    public void withdraw(int accountId, double amount) throws SQLException {
        try {
            if (amount <= 0) {
                System.out.println("Withdrawal amount must be greater than zero.");
                return;
            }
            String referenceId = generateReferenceId();
            if (processedRequestDao.isRequestProcessed(referenceId)) {
                System.out.println("Duplicate transaction detected: " + referenceId);
                return;
            }
            com.igirepay.lab1.model.Account account = accountDao.getAccountById(accountId);
            if (account == null) {
                System.out.println("Account not found!");
                return;
            }
            if (amount > account.getBalance()) {
                System.out.println("Insufficient balance!");
                return;
            }
            if (account.getAccountType().equals("SAVINGS")) {
                if (amount < 100) {
                    System.out.println("Minimum withdrawal for savings account is 100 RWF!");
                    return;
                }
                double fee = amount * 2 / 100;
                double totalDeduction = amount + fee;
                if (totalDeduction > account.getBalance()) {
                    System.out.println("Insufficient balance to cover amount and fee!");
                    return;
                }
                double newBalance = account.getBalance() - totalDeduction;
                accountDao.updateBalance(accountId, newBalance);
                Transaction transaction = new Transaction(0, referenceId, amount, "WITHDRAW", accountId, LocalDateTime.now().toString());
                transactionDao.addTransaction(transaction);
                processedRequestDao.addProcessedRequest(referenceId);
                System.out.println("Fee charged: " + fee + " RWF");
                System.out.println("Withdrawal successful! New balance: " + newBalance);
            } else {
                double newBalance = account.getBalance() - amount;
                accountDao.updateBalance(accountId, newBalance);
                Transaction transaction = new Transaction(0, referenceId, amount, "WITHDRAW", accountId, LocalDateTime.now().toString());
                transactionDao.addTransaction(transaction);
                processedRequestDao.addProcessedRequest(referenceId);
                System.out.println("Withdrawal successful! New balance: " + newBalance);
            }
        } catch (SQLException e) {
            System.out.println("Error during withdrawal: " + e.getMessage());
            throw e;
        }
    }
    public void transfer(int fromAccountId, int toAccountId, double amount) throws SQLException {
        try {
            if (amount <= 0) {
                System.out.println("Transfer amount must be greater than zero.");
                return;
            }
            String referenceId = generateReferenceId();
            if (processedRequestDao.isRequestProcessed(referenceId)) {
                System.out.println("Duplicate transaction detected: " + referenceId);
                return;
            }
            com.igirepay.lab1.model.Account fromAccount = accountDao.getAccountById(fromAccountId);
            com.igirepay.lab1.model.Account toAccount = accountDao.getAccountById(toAccountId);
            if (fromAccount == null || toAccount == null) {
                System.out.println("One or both accounts not found!");
                return;
            }
            if (fromAccount.getAccountType().equals("SAVINGS")) {
                double fee = amount * 2 / 100;
                double totalDeduction = amount + fee;
                if (totalDeduction > fromAccount.getBalance()) {
                    System.out.println("Insufficient balance to cover amount and fee!");
                    return;
                }
                double newFromBalance = fromAccount.getBalance() - totalDeduction;
                double newToBalance = toAccount.getBalance() + amount;
                accountDao.updateBalance(fromAccountId, newFromBalance);
                accountDao.updateBalance(toAccountId, newToBalance);
                Transaction transaction = new Transaction(0, referenceId, amount, "TRANSFER", fromAccountId, LocalDateTime.now().toString());
                transactionDao.addTransaction(transaction);
                processedRequestDao.addProcessedRequest(referenceId);
                System.out.println("Fee charged: " + fee + " RWF");
                System.out.println("Transfer successful! Your new balance: " + newFromBalance);
            } else {
                double newFromBalance = fromAccount.getBalance() - amount;
                double newToBalance = toAccount.getBalance() + amount;
                accountDao.updateBalance(fromAccountId, newFromBalance);
                accountDao.updateBalance(toAccountId, newToBalance);
                Transaction transaction = new Transaction(0, referenceId, amount, "TRANSFER", fromAccountId, LocalDateTime.now().toString());
                transactionDao.addTransaction(transaction);
                processedRequestDao.addProcessedRequest(referenceId);
                System.out.println("Transfer successful! Your new balance: " + newFromBalance);
            }
        } catch (SQLException e) {
            System.out.println("Error during transfer: " + e.getMessage());
            throw e;
        }
    }
}
