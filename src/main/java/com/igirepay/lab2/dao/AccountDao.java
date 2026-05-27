package com.igirepay.lab2.dao;

import com.igirepay.lab1.model.Account;
import com.igirepay.lab2.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDao {

    public int createAccount(Account account) throws SQLException {
        try {
            String sql = "INSERT INTO accounts (customer_id, account_type, balance) VALUES (?, ?, ?) RETURNING id";
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, account.getCustomerId());
            preparedStatement.setString(2, account.getAccountType());
            preparedStatement.setDouble(3, account.getBalance());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
            return -1;
        } catch (SQLException e) {
            System.out.println("Error creating account: " + e.getMessage());
            throw e;
        }
    }
    public Account getAccountById(int id) throws SQLException {
        try {
            String sql = "SELECT * FROM accounts WHERE id = ?";
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Account(
                        resultSet.getInt("id"),
                        resultSet.getInt("customer_id"),
                        resultSet.getString("account_type"),
                        resultSet.getDouble("balance"),
                        resultSet.getString("created_at")
                );
            }
            return null;
        } catch (SQLException e) {
            System.out.println("Error getting account: " + e.getMessage());
            throw e;
        }
    }
    public List<Account> getAccountsByCustomerId(int customerId) throws SQLException {
        try {
            List<Account> accounts = new ArrayList<>();
            String sql = "SELECT * FROM accounts WHERE customer_id = ?";
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                accounts.add(new Account(
                        resultSet.getInt("id"),
                        resultSet.getInt("customer_id"),
                        resultSet.getString("account_type"),
                        resultSet.getDouble("balance"),
                        resultSet.getString("created_at")
                ));
            }
            return accounts;
        } catch (SQLException e) {
            System.out.println("Error getting accounts: " + e.getMessage());
            throw e;
        }
    }
    public void updateBalance(int accountId, double newBalance) throws SQLException {
        try {
            String sql = "UPDATE accounts SET balance = ? WHERE id = ?";
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDouble(1, newBalance);
            preparedStatement.setInt(2, accountId);
            preparedStatement.executeUpdate();
            System.out.println("Balance updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error updating balance: " + e.getMessage());
            throw e;
        }
    }
    public void deleteAccount(int id) throws SQLException {
        try {
            String sql = "DELETE FROM accounts WHERE id = ?";
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            System.out.println("Account deleted successfully!");
        } catch (SQLException e) {
            System.out.println("Error deleting account: " + e.getMessage());
            throw e;
        }
    }
}
