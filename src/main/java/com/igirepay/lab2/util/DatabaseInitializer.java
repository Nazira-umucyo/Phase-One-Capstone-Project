package com.igirepay.lab2.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initializeDatabase(){
        try {
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();

            String createCustomersTable = "CREATE TABLE customers (" +
                    "id SERIAL PRIMARY KEY," +
                    "full_name VARCHAR(100) NOT NULL," +
                    "email VARCHAR(100) UNIQUE NOT NULL," +
                    "phone_number VARCHAR(20) NOT NULL," +
                    "pin VARCHAR(10) NOT NULL" +
                    ")";
            String createAccountsTable = "CREATE TABLE accounts (" +
                    "id SERIAL PRIMARY KEY," +
                    "customer_id INT NOT NULL," +
                    "account_type VARCHAR(20) NOT NULL," +
                    "balance DOUBLE PRECISION NOT NULL," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY (customer_id) REFERENCES customers(id)" +
                    ")";
            String createTransactionsTable = "CREATE TABLE transactions (" +
                    "id SERIAL PRIMARY KEY," +
                    "account_id INT NOT NULL," +
                    "reference_id VARCHAR(50) UNIQUE NOT NULL," +
                    "transaction_type VARCHAR(20) NOT NULL," +
                    "amount DOUBLE PRECISION NOT NULL," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY (account_id) REFERENCES accounts(id)" +
                    ")";
            String createProcessedRequestsTable = "CREATE TABLE processed_requests (" +
                    "id SERIAL PRIMARY KEY," +
                    "reference_id VARCHAR(50) UNIQUE NOT NULL," +
                    "processed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")";
            statement.execute(createCustomersTable);
            System.out.println("Customers table created successfully!");

            statement.execute(createAccountsTable);
            System.out.println("Accounts table created successfully!");

            statement.execute(createTransactionsTable);
            System.out.println("Transactions table created successfully!");

            statement.execute(createProcessedRequestsTable);
            System.out.println("Processed requests table created successfully!");

            statement.close();
            System.out.println("Database initialized successfully!");
        } catch (SQLException e) {
        if (!e.getMessage().contains("already exists")) {
            System.out.println("Error initializing database: " + e.getMessage());
        }
    }
    }
}
