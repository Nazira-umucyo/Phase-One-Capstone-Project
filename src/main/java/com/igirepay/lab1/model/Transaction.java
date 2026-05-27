package com.igirepay.lab1.model;

public class Transaction {
    private int id;
    private String referenceId;
    private double amount;
    private String transactionType;
    private int accountId;
    private String createdAt;

    public Transaction(int id, String referenceId, double amount, String transactionType, int accountId, String createdAt) {
        this.id = id;
        this.referenceId = referenceId;
        this.amount = amount;
        this.transactionType = transactionType;
        this.accountId = accountId;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", referenceId='" + referenceId + '\'' +
                ", amount=" + amount +
                ", transactionType='" + transactionType + '\'' +
                ", accountId=" + accountId +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
