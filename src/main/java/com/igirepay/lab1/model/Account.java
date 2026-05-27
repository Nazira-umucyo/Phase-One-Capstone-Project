package com.igirepay.lab1.model;

public class Account {

    private int id;
    private int customerId;
    private String accountType;
    private double balance;
    private String createdAt;

    public Account(int id, int customerId, String accountType, double balance, String createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.accountType = accountType;
        this.balance = balance;
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", accountType='" + accountType + '\'' +
                ", balance=" + balance +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }

    public void deposit(double amount) {
        balance = balance  + amount;
    }
    public void withdraw(double amount) {
        balance = balance - amount;
    }
    public void processTransaction(double amount, String type) {
        if (type.equals("DEPOSIT")) {
            deposit(amount);
        }  else if (type.equals("WITHDRAW")) {
            withdraw(amount);
            }

    }
}
