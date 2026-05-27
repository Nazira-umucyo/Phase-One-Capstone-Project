package com.igirepay.lab1.model;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class PaymentManager {

    private List<Customer> customers;
    private List<Transaction> transactionHistory;
    private Set<String> processedReferenceIds;
    private List<String> failedTransactionLogs;
    private Map<Integer, List<Account>> customerAccounts;

    public PaymentManager(){
        this.customers = new ArrayList<>();
        this.transactionHistory = new ArrayList<>();
        this.processedReferenceIds = new HashSet<>();
        this.failedTransactionLogs = new ArrayList<>();
        this.customerAccounts = new HashMap<>();
    }

    public boolean isTransactionProcessed(String referenceId){
        return processedReferenceIds.contains(referenceId);
    }
    public void addTransaction(Transaction transaction){
        if (isTransactionProcessed(transaction.getReferenceId())) {
            failedTransactionLogs.add("Duplicate transaction detected: " + transaction.getReferenceId());
            System.out.println("Duplicate transaction rejected: " + transaction.getReferenceId());
            return;
        }
            transactionHistory.add(transaction);
            processedReferenceIds.add(transaction.getReferenceId());
            System.out.println("Transaction processed successfully: " + transaction.getReferenceId());

    }
    public void addCustomer(Customer customer){
        customers.add(customer);
        customerAccounts.put(customer.getId(),  new ArrayList<>());
        System.out.println("Customer added successfully: " + customer.getFullName());
    }
    public void addAccountToCustomer(int customerId, Account account){
        if (customerAccounts.containsKey(customerId)){
            customerAccounts.get(customerId).add(account);
            System.out.println("Account added to customer:" + customerId);
        } else { System.out.println("Customer not found: " + customerId); }
    }
    public List<Account> getCustomerAccounts(int customerId){
        return customerAccounts.get(customerId);
    }
    public List<Transaction> getTransactionHistory(){
        return transactionHistory;
    }
    public List<Customer> getCustomers(){
        return customers;
    }
    public List<String> getFailedTransactionLogs(){
        return failedTransactionLogs;
    }
    public void printTransactionHistory(){
        if  (transactionHistory.isEmpty()){
            System.out.println("No transactions found");
        } else {
            System.out.println("Transactions history");
            for (Transaction transaction : transactionHistory) {
                System.out.println(transaction);
            }
        }
    }
    public void exportTransactionHistoryToCsv(String filename){
        try {
            FileWriter fw = new FileWriter(filename);
            PrintWriter printWriter = new PrintWriter(fw);

            printWriter.println("ID,Account ID,Reference ID,Transaction Type,Amount,Created At");

            for (Transaction transaction : transactionHistory) {
                printWriter.println(
                        transaction.getId() + "," +
                                transaction.getReferenceId() + "," +
                                transaction.getAccountId() + "," +
                                transaction.getReferenceId() + "," +
                                transaction.getTransactionType() + "," +
                                transaction.getAmount() + "," +
                                transaction.getCreatedAt() + ","
                );
            }
            printWriter.close();
            System.out.println("exported transaction history to: " + filename);
        }catch (IOException e){
            System.out.println("Error while exporting transaction history" + e.getMessage());
        }
    }
}
