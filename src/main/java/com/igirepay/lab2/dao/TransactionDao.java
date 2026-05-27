package com.igirepay.lab2.dao;

import com.igirepay.lab1.model.Transaction;
import com.igirepay.lab2.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionDao {

    public void addTransaction(Transaction transaction) throws SQLException {
        try {
            String sql = "INSERT INTO transactions (account_id, reference_id, transaction_type, amount) VALUES (?, ?, ?, ?)";
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, transaction.getAccountId());
            preparedStatement.setString(2, transaction.getReferenceId());
            preparedStatement.setString(3, transaction.getTransactionType());
            preparedStatement.setDouble(4, transaction.getAmount());
            preparedStatement.executeUpdate();
            System.out.println("Transaction added successfully: " + transaction.getReferenceId());
        } catch (SQLException e) {
            System.out.println("Error adding transaction: " + e.getMessage());
            throw e;
        }
    }
    public List<Transaction> getTransactionsByAccountId(int accountId) throws SQLException {
        try {
            List<Transaction> transactions = new ArrayList<>();
            String sql = "SELECT * FROM transactions WHERE account_id = ?";
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, accountId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                transactions.add(new Transaction(
                        resultSet.getInt("id"),
                        resultSet.getString("reference_id"),
                        resultSet.getDouble("amount"),
                        resultSet.getString("transaction_type"),
                        resultSet.getInt("account_id"),
                        resultSet.getString("created_at")
                ));
            }
            return transactions;
        } catch (SQLException e) {
            System.out.println("Error getting transactions: " + e.getMessage());
            throw e;
        }
    }
    public List<Transaction> getAllTransactions() throws SQLException {
        try {
            List<Transaction> transactions = new ArrayList<>();
            String sql = "SELECT * FROM transactions";
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                transactions.add(new Transaction(
                        resultSet.getInt("id"),
                        resultSet.getString("reference_id"),
                        resultSet.getDouble("amount"),
                        resultSet.getString("transaction_type"),
                        resultSet.getInt("account_id"),
                        resultSet.getString("created_at")
                ));
            }
            return transactions;
        } catch (SQLException e) {
            System.out.println("Error getting all transactions: " + e.getMessage());
            throw e;
        }
    }
    public void deleteTransaction(int id) throws SQLException {
        try {
            String sql = "DELETE FROM transactions WHERE id = ?";
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            System.out.println("Transaction deleted successfully!");
        } catch (SQLException e) {
            System.out.println("Error deleting transaction: " + e.getMessage());
            throw e;
        }
    }
}
