/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class SubscriberManagementInterface extends JFrame {
    private JTable subscriberTable;
    private JTextField lastNameField, firstNameField, addressField, phoneField, searchField;
    private JButton addButton, modifyButton, deleteButton, searchButton;

    public SubscriberManagementInterface() {
        setTitle("Library Manager - Subscriber Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Table
        subscriberTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(subscriberTable);
        add(scrollPane, BorderLayout.CENTER);

        // Input Fields
        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        inputPanel.add(new JLabel("Last Name:"));
        lastNameField = new JTextField();
        inputPanel.add(lastNameField);

        inputPanel.add(new JLabel("First Name:"));
        firstNameField = new JTextField();
        inputPanel.add(firstNameField);

        inputPanel.add(new JLabel("Address:"));
        addressField = new JTextField();
        inputPanel.add(addressField);

        inputPanel.add(new JLabel("Phone Number:"));
        phoneField = new JTextField();
        inputPanel.add(phoneField);

        inputPanel.add(new JLabel("Search by Name or Phone:"));
        searchField = new JTextField();
        inputPanel.add(searchField);

        add(inputPanel, BorderLayout.NORTH);

        // Buttons
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add");
        modifyButton = new JButton("Modify");
        deleteButton = new JButton("Delete");
        searchButton = new JButton("Search");

        buttonPanel.add(addButton);
        buttonPanel.add(modifyButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(searchButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Event Listeners
        addButton.addActionListener(e -> addSubscriber());
        modifyButton.addActionListener(e -> modifySubscriber());
        deleteButton.addActionListener(e -> deleteSubscriber());
        searchButton.addActionListener(e -> searchSubscribers());

        setVisible(true);
    }

    // Add Subscriber
    private void addSubscriber() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/LibraryManager", "root", "root")) {
            String query = "INSERT INTO Subscribers (Last_Name, First_Name, Address, Telephone) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, lastNameField.getText());
            pstmt.setString(2, firstNameField.getText());
            pstmt.setString(3, addressField.getText());
            pstmt.setString(4, phoneField.getText());
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Subscriber added successfully!");
            loadAllSubscribers();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Modify Subscriber
    private void modifySubscriber() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/LibraryManager", "root", "password")) {
            int selectedRow = subscriberTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Select a subscriber to modify!");
                return;
            }

            int subscriberID = (int) subscriberTable.getValueAt(selectedRow, 0);
            String query = "UPDATE Subscribers SET Last_Name = ?, First_Name = ?, Address = ?, Telephone = ? WHERE Subscriber_ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, lastNameField.getText());
            pstmt.setString(2, firstNameField.getText());
            pstmt.setString(3, addressField.getText());
            pstmt.setString(4, phoneField.getText());
            pstmt.setInt(5, subscriberID);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Subscriber modified successfully!");
            loadAllSubscribers();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Delete Subscriber
    private void deleteSubscriber() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/LibraryManager", "root", "password")) {
            int selectedRow = subscriberTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Select a subscriber to delete!");
                return;
            }

            int subscriberID = (int) subscriberTable.getValueAt(selectedRow, 0);
            String query = "DELETE FROM Subscribers WHERE Subscriber_ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, subscriberID);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Subscriber deleted successfully!");
            loadAllSubscribers();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Search Subscribers by Name or Phone
    private void searchSubscribers() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/LibraryManager", "root", "password")) {
            String searchQuery = "SELECT * FROM Subscribers WHERE Last_Name LIKE ? OR First_Name LIKE ? OR Telephone LIKE ?";
            PreparedStatement pstmt = conn.prepareStatement(searchQuery);
            String searchTerm = "%" + searchField.getText() + "%";
            pstmt.setString(1, searchTerm);
            pstmt.setString(2, searchTerm);
            pstmt.setString(3, searchTerm);
            ResultSet rs = pstmt.executeQuery();
            // Implement loading search results into JTable
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Load All Subscribers
    private void loadAllSubscribers() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/LibraryManager", "root", "password")) {
            String query = "SELECT * FROM Subscribers";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            // Implement loading results into JTable
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new SubscriberManagementInterface();
    }
}
