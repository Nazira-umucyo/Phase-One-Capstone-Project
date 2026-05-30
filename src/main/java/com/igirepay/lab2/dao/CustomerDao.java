package com.igirepay.lab2.dao;

import com.igirepay.lab1.model.Customer;
import com.igirepay.lab2.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDao {
    public void addCustomer(Customer customer) throws SQLException {
        try {
            String sql = "INSERT INTO customers (full_name, email, phone_number, pin) VALUES (?, ?, ?, ?)";
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, customer.getFullName());
            preparedStatement.setString(2, customer.getEmail());
            preparedStatement.setString(3, customer.getPhoneNumber());
            preparedStatement.setString(4, customer.getPin());
            preparedStatement.executeUpdate();
            System.out.println("Customer added successfully");
        } catch (SQLException e) {
            System.out.println("error adding customer: " + e.getMessage());
            throw e;
        }
    }
    public Customer getCustomerById(int id) throws SQLException {
        try {
            String sql = "SELECT * FROM customers WHERE id = ?";
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Customer(
                        resultSet.getInt("id"),
                        resultSet.getString("full_name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone_number"),
                        resultSet.getString("pin"),
                        resultSet.getString("role")
                );
            }
            return null;
        }catch (SQLException e) {
            System.out.println("error getting customer: " + e.getMessage());
            throw e;
        }
    }
    public List<Customer> getAllCustomers() throws SQLException {
        try {
            List<Customer> customers = new ArrayList<>();
            String sql = "SELECT * FROM customers";
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                customers.add(new Customer(
                        resultSet.getInt("id"),
                        resultSet.getString("full_name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone_number"),
                        resultSet.getString("pin"),
                        resultSet.getString("role")
                ));
            }
            return customers;
        } catch (SQLException e) {
            System.out.println("Error getting customers: " + e.getMessage());
            throw e;
        }
    }
    public void updateCustomer(Customer customer) throws SQLException {
        try {
            String sql = "UPDATE customers SET full_name = ?, email = ?, phone_number = ? WHERE id = ?";
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, customer.getFullName());
            preparedStatement.setString(2, customer.getEmail());
            preparedStatement.setString(3, customer.getPhoneNumber());
            preparedStatement.setInt(4, customer.getId());
            preparedStatement.executeUpdate();
            System.out.println("Customer updated successfully: " + customer.getFullName());
        } catch (SQLException e) {
            System.out.println("Error updating customer: " + e.getMessage());
            throw e;
        }
    }
    public void updatePin(int customerId, String newPin) throws SQLException {
        try {
            String sql = "UPDATE customers SET pin = ? WHERE id = ?";
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, newPin);
            preparedStatement.setInt(2, customerId);
            preparedStatement.executeUpdate();
            System.out.println("PIN updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error updating PIN: " + e.getMessage());
            throw e;
        }
    }
    public void deleteCustomer(int id) throws SQLException {
        try {
            String sql = "DELETE FROM customers WHERE id = ?";
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            System.out.println("Customer deleted successfully!");
        } catch (SQLException e) {
            System.out.println("Error deleting customer: " + e.getMessage());
            throw e;
        }
    }
}
