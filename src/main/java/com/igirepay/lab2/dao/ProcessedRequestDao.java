package com.igirepay.lab2.dao;

import com.igirepay.lab2.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProcessedRequestDao {

    public void addProcessedRequest(String referenceId) throws SQLException {
        try {
            String sql = "INSERT INTO processed_requests (reference_id) VALUES (?)";
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, referenceId);
            preparedStatement.executeUpdate();
            System.out.println("Processed request added: " + referenceId);
        } catch (SQLException e) {
            System.out.println("Error adding processed request: " + e.getMessage());
            throw e;
        }
    }
    public boolean isRequestProcessed(String referenceId) throws SQLException {
        try {
            String sql = "SELECT * FROM processed_requests WHERE reference_id = ?";
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, referenceId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            System.out.println("Error checking processed request: " + e.getMessage());
            throw e;
        }
    }
}
