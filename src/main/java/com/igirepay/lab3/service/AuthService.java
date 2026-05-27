package com.igirepay.lab3.service;

import com.igirepay.lab1.model.Customer;
import com.igirepay.lab2.dao.CustomerDao;

import java.sql.SQLException;
import java.util.List;

public class AuthService {

    private CustomerDao customerDao;

    public AuthService() {
        this.customerDao = new CustomerDao();
    }
    public Customer login(String phoneNumber, String pin) throws SQLException {
        try {
            List<Customer> customers = customerDao.getAllCustomers();
            for (Customer customer : customers) {
                if (customer.getPhoneNumber().equals(phoneNumber) && customer.getPin().equals(pin)) {
                    System.out.println("Login successful! Welcome " + customer.getFullName());
                    return customer;
                }
            }
            System.out.println("Invalid phone number or PIN!");
            return null;
        } catch (SQLException e) {
            System.out.println("Error during login: " + e.getMessage());
            throw e;
        }
    }
    public boolean changePin(Customer customer, String oldPin, String newPin) throws SQLException {
        try {
            if (customer.getPin().equals(oldPin)) {
                customer.setPin(newPin);
                customerDao.updateCustomer(customer);
                System.out.println("PIN changed successfully!");
                return true;
            } else {
                System.out.println("Old PIN is incorrect!");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error changing PIN: " + e.getMessage());
            throw e;
        }
    }
    public void registerCustomer(Customer customer) throws SQLException {
        try {
            customerDao.addCustomer(customer);
            System.out.println("Customer registered successfully!");
        } catch (SQLException e) {
            System.out.println("Error registering customer: " + e.getMessage());
            throw e;
        }
    }
}
