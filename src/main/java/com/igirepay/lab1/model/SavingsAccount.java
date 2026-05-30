package com.igirepay.lab1.model;

public class SavingsAccount extends Account {

    private double interestRate;
    private double withdrawalLimit;
    private double transactionFee;

    public SavingsAccount(int id, int customerId, String accountType, double balance, String createdAt, double interestRate, double withdrawalLimit, double transactionFee) {
        super(id, customerId, "SAVINGS", balance, createdAt);
        this.interestRate = interestRate;
        this.withdrawalLimit = withdrawalLimit;
        this.transactionFee = transactionFee;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public double getWithdrawalLimit() {
        return withdrawalLimit;
    }

    public void setWithdrawalLimit(double withdrawalLimit) {
        this.withdrawalLimit = withdrawalLimit;
    }

    public double getTransactionFee() {
        return transactionFee;
    }

    public void setTransactionFee(double transactionFee) {
        this.transactionFee = transactionFee;
    }

    @Override
    public String toString() {
        return "SavingsAccount{" +
                "interestRate=" + interestRate +
                ", withdrawalLimit=" + withdrawalLimit +
                ", transactionFee=" + transactionFee +
                '}';
    }

    @Override
    public void deposit(double amount) {
        if(amount <= 0){
            System.out.println("Deposit Amount must be greater than 0");
            return;
        }
        setBalance(getBalance() + amount);
        System.out.println("Deposited " + amount);
    }
    @Override
    public void withdraw(double amount) {
        if(amount <= 0){
            System.out.println("Withdrawal Amount must be greater than 0");
            return;
        }
        if (amount > getBalance()) {
            System.out.println("Insufficient balance");
            return;
        }
        if (amount > withdrawalLimit) {
            System.out.println("Amount to withdraw is greater than limit of " + withdrawalLimit);
            return;
        }
        double fee = amount * transactionFee /100;
        double totalDeduction = amount + fee;
        if (totalDeduction > getBalance()) {
            System.out.println("Insufficient balance");
            return;
        }
        setBalance(getBalance() - totalDeduction);
        System.out.println("Withdrawn: " + amount);
        System.out.println("fee charged: " + fee);
        System.out.println("Total deducted: " + totalDeduction);
        System.out.println("new balance: " + getBalance());
    }
    @Override
    public void processTransaction(double amount, String type) {
            System.out.println("Processing saving account transaction");
        super.processTransaction(amount, type);
    }
}
