package com.igirepay.lab1.model;

public class WalletAccount extends Account {

    private boolean instantTransfer;

    public WalletAccount(int id, int customerId, String accountType, double balance, String createdAt, boolean instantTransfer) {
        super(id, customerId, "WALLET", balance, createdAt);
        this.instantTransfer = instantTransfer;
    }

    public boolean isInstantTransfer() {
        return instantTransfer;
    }

    public void setInstantTransfer(boolean instantTransfer) {
        this.instantTransfer = instantTransfer;
    }

    @Override
    public String toString() {
        return "WalletAccount{" +
                "instantTransfer=" + instantTransfer +
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
        setBalance(getBalance() - amount);
        System.out.println("Withdrawn " + amount);
    }

    @Override
    public void processTransaction(double amount, String type) {
        if (instantTransfer) {
            System.out.println("Processing Transaction");
        }
        super.processTransaction(amount, type);
    }
}
