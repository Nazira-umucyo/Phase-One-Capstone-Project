package com.igirepay.lab2.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL =  "jdbc:postgresql://localhost:5432/igirepay";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "Nazira";

    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("database connected successfully");
        }
        return connection;
    }
    public static void closeConnection(){
        try{
            if (connection != null && !connection.isClosed()){
                connection.close();
                System.out.println("database connection is closed");
            }
        }catch(SQLException e){
            System.out.println("error while closing connection: "+e.getMessage());
        }
    }
}
