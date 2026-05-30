package com.igirepay.lab3.service;

import com.igirepay.lab1.model.Account;
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

    public String deposit(int accountId, double amount, int customerId) throws SQLException {
        try {
            if (amount <= 0) {
                return "Deposit amount must be greater than zero!";
            }
            String referenceId = generateReferenceId();
            if (processedRequestDao.isRequestProcessed(referenceId)) {
                return "Duplicate transaction detected!";
            }
            Account account = accountDao.getAccountById(accountId);
            if (account == null) {
                return "Account not found!";
            }
            if (account.getCustomerId() != customerId) {
                return "You can only deposit to your own account!";
            }
            double newBalance = account.getBalance() + amount;
            accountDao.updateBalance(accountId, newBalance);
            Transaction transaction = new Transaction(0, referenceId, amount, "DEPOSIT", accountId, LocalDateTime.now().toString());
            transactionDao.addTransaction(transaction);
            processedRequestDao.addProcessedRequest(referenceId);
            return "";
        } catch (SQLException e) {
            throw e;
        }
    }

    public String withdraw(int accountId, double amount, int customerId) throws SQLException {
        try {
            if (amount <= 0) {
                return "Withdrawal amount must be greater than zero!";
            }
            String referenceId = generateReferenceId();
            if (processedRequestDao.isRequestProcessed(referenceId)) {
                return "Duplicate transaction detected!";
            }
            Account account = accountDao.getAccountById(accountId);
            if (account == null) {
                return "Account not found!";
            }
            if (account.getCustomerId() != customerId) {
                return "You can only withdraw from your own account!";
            }
            if (amount > account.getBalance()) {
                return "Insufficient balance!";
            }
            if (account.getAccountType().equals("SAVINGS")) {
                if (amount < 100) {
                    return "Minimum withdrawal for savings account is 100 RWF!";
                }
                double fee = amount * 2 / 100;
                double totalDeduction = amount + fee;
                if (totalDeduction > account.getBalance()) {
                    return "Insufficient balance to cover amount and fee!";
                }
                double newBalance = account.getBalance() - totalDeduction;
                accountDao.updateBalance(accountId, newBalance);
                Transaction transaction = new Transaction(0, referenceId, amount, "WITHDRAW", accountId, LocalDateTime.now().toString());
                transactionDao.addTransaction(transaction);
                processedRequestDao.addProcessedRequest(referenceId);
                return "SUCCESS:Withdrawn successfully! Fee charged: " + fee + " RWF. New balance: " + newBalance + " RWF";
            } else {
                double newBalance = account.getBalance() - amount;
                accountDao.updateBalance(accountId, newBalance);
                Transaction transaction = new Transaction(0, referenceId, amount, "WITHDRAW", accountId, LocalDateTime.now().toString());
                transactionDao.addTransaction(transaction);
                processedRequestDao.addProcessedRequest(referenceId);
                return "";
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    public String transfer(int fromAccountId, int toAccountId, double amount, int customerId) throws SQLException {
        try {
            if (amount <= 0) {
                return "Transfer amount must be greater than zero!";
            }
            String referenceId = generateReferenceId();
            if (processedRequestDao.isRequestProcessed(referenceId)) {
                return "Duplicate transaction detected!";
            }
            Account fromAccount = accountDao.getAccountById(fromAccountId);
            Account toAccount = accountDao.getAccountById(toAccountId);
            if (fromAccount == null) {
                return "Your account not found!";
            }
            if (toAccount == null) {
                return "Recipient account not found!";
            }
            if (fromAccount.getCustomerId() != customerId) {
                return "You can only transfer from your own account!";
            }
            if (amount > fromAccount.getBalance()) {
                return "Insufficient balance!";
            }
            if (fromAccount.getAccountType().equals("SAVINGS")) {
                double fee = amount * 2 / 100;
                double totalDeduction = amount + fee;
                if (totalDeduction > fromAccount.getBalance()) {
                    return "Insufficient balance to cover amount and fee!";
                }
                double newFromBalance = fromAccount.getBalance() - totalDeduction;
                double newToBalance = toAccount.getBalance() + amount;
                accountDao.updateBalance(fromAccountId, newFromBalance);
                accountDao.updateBalance(toAccountId, newToBalance);
                Transaction transaction = new Transaction(0, referenceId, amount, "TRANSFER", fromAccountId, LocalDateTime.now().toString());
                transactionDao.addTransaction(transaction);
                processedRequestDao.addProcessedRequest(referenceId);
                return "SUCCESS: Money transferred successfully! Fee charged: " + fee + " RWF. New balance: " + newFromBalance + " RWF";
            } else {
                double newFromBalance = fromAccount.getBalance() - amount;
                double newToBalance = toAccount.getBalance() + amount;
                accountDao.updateBalance(fromAccountId, newFromBalance);
                accountDao.updateBalance(toAccountId, newToBalance);
                Transaction transaction = new Transaction(0, referenceId, amount, "TRANSFER", fromAccountId, LocalDateTime.now().toString());
                transactionDao.addTransaction(transaction);
                processedRequestDao.addProcessedRequest(referenceId);
                return "";
            }
        } catch (SQLException e) {
            throw e;
        }
    }
}