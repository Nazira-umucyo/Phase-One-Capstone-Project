## IgirePay Payment Gateway

A JavaFX desktop digital wallet system inspired by MTN Mobile Money, built as a Phase One Backend Capstone Project.

## Project Structure

The project is organized into three labs:

### Lab 1 - Object Oriented Programming
- Account (parent class)
- WalletAccount (extends Account)
- SavingsAccount (extends Account - 2% fee, min 100 RWF withdrawal)
- Customer
- Transaction
- PaymentManager (Java Collections - List, Set, Map)

### Lab 2 - Database & DAO
- PostgreSQL database integration
- DAO Pattern (AccountDao, CustomerDao, TransactionDao, ProcessedRequestDao)
- Singleton Pattern (DatabaseConnection)
- PreparedStatement (SQL Injection prevention)
- Idempotency (reference IDs prevent duplicate transactions)

### Lab 3 - Full Application
- Console application (MainMenu, AccountMenu, CustomerMenu, TransactionMenu, ReportMenu)
- JavaFX UI (Login, Register, Dashboard, Account Management, Transaction Management, Reports, Change PIN)
- Admin Dashboard (view all customers, accounts, transactions, search, export statements)
- Role based access (ADMIN and USER roles)
- Service layer (AuthService, TransactionService, ReportService)

## Technologies Used
- Java 25
- JavaFX 21
- PostgreSQL
- Maven
- IntelliJ IDEA

## Features
- User registration and login
- Wallet and Savings account management
- Deposit, Withdraw and Transfer with validations
- Savings account 2% transaction fee
- Minimum 100 RWF withdrawal for savings accounts
- Transaction history with table UI
- Daily summary reports
- CSV export
- Admin dashboard with stats
- Role based access control

## Database Setup
1. Create PostgreSQL database named `igirepay`
2. Run the application — tables are created automatically by DatabaseInitializer

## Author
Nazira Umucyo

SheCanCode

Backend Phase 1 Capstone Project 2026
