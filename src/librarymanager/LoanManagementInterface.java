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

public class LoanManagementInterface extends JFrame {
    private JTable loanTable;
    private JTextField bookIDField, subscriberIDField, borrowDateField, returnDateField, subscriberSearchField;
    private JButton recordLoanButton, returnBookButton, viewAllButton, viewOverdueButton, viewSubscriberLoansButton;

    public LoanManagementInterface() {
        setTitle("Library Manager - Loan Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Table
        loanTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(loanTable);
        add(scrollPane, BorderLayout.CENTER);

        // Input Fields
        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        inputPanel.add(new JLabel("Book ID:"));
        bookIDField = new JTextField();
        inputPanel.add(bookIDField);

        inputPanel.add(new JLabel("Subscriber ID:"));
        subscriberIDField = new JTextField();
        inputPanel.add(subscriberIDField);

        inputPanel.add(new JLabel("Borrowing Date (YYYY-MM-DD):"));
        borrowDateField = new JTextField();
        inputPanel.add(borrowDateField);

        inputPanel.add(new JLabel("Return Date (YYYY-MM-DD):"));
        returnDateField = new JTextField();
        inputPanel.add(returnDateField);

        inputPanel.add(new JLabel("Search Subscriber ID:"));
        subscriberSearchField = new JTextField();
        inputPanel.add(subscriberSearchField);

        add(inputPanel, BorderLayout.NORTH);

        // Buttons
        JPanel buttonPanel = new JPanel();
        recordLoanButton = new JButton("Record Loan");
        returnBookButton = new JButton("Return Book");
        viewAllButton = new JButton("View All Loans");
        viewOverdueButton = new JButton("View Overdue Loans");
        viewSubscriberLoansButton = new JButton("View Subscriber Loans");

        buttonPanel.add(recordLoanButton);
        buttonPanel.add(returnBookButton);
        buttonPanel.add(viewAllButton);
        buttonPanel.add(viewOverdueButton);
        buttonPanel.add(viewSubscriberLoansButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Event Listeners
        recordLoanButton.addActionListener(e -> recordLoan());
        returnBookButton.addActionListener(e -> returnBook());
        viewAllButton.addActionListener(e -> loadAllLoans());
        viewOverdueButton.addActionListener(e -> viewOverdueLoans());
        viewSubscriberLoansButton.addActionListener(e -> viewSubscriberLoans());

        setVisible(true);
    }

    // Record a new Loan
    private void recordLoan() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/LibraryManager", "root", "password")) {
            String query = "INSERT INTO Borrowing (Book_ID, Subscriber_ID, Borrowing_Date, Return_Date) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(bookIDField.getText()));
            pstmt.setInt(2, Integer.parseInt(subscriberIDField.getText()));
            pstmt.setDate(3, Date.valueOf(borrowDateField.getText()));
            pstmt.setDate(4, Date.valueOf(returnDateField.getText()));
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Loan recorded successfully!");
            loadAllLoans();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Return a Book and update the Return Date
    private void returnBook() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/LibraryManager", "root", "password")) {
            int selectedRow = loanTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Select a loan to return!");
                return;
            }

            int borrowingID = (int) loanTable.getValueAt(selectedRow, 0);
            String query = "UPDATE Borrowing SET Return_Date = ? WHERE Borrowing_ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setDate(1, Date.valueOf(returnDateField.getText()));
            pstmt.setInt(2, borrowingID);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Book returned successfully!");
            loadAllLoans();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Load All Loans
    private void loadAllLoans() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/LibraryManager", "root", "password")) {
            String query = "SELECT b.Title, s.Last_Name, s.First_Name, bo.Borrowing_Date, bo.Return_Date FROM Borrowing bo JOIN Books b ON bo.Book_ID = b.Book_ID JOIN Subscribers s ON bo.Subscriber_ID = s.Subscriber_ID";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            // Implement loading results into JTable
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // View Overdue Loans
    private void viewOverdueLoans() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/LibraryManager", "root", "password")) {
            String query = "SELECT b.Title, s.Last_Name, s.First_Name, bo.Borrowing_Date, bo.Return_Date FROM Borrowing bo JOIN Books b ON bo.Book_ID = b.Book_ID JOIN Subscribers s ON bo.Subscriber_ID = s.Subscriber_ID WHERE bo.Return_Date < CURDATE()";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            // Implement loading results into JTable
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // View Loans of a Subscriber
    private void viewSubscriberLoans() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/LibraryManager", "root", "root")) {
            String query = "SELECT b.Title, bo.Borrowing_Date, bo.Return_Date FROM Borrowing bo JOIN Books b ON bo.Book_ID = b.Book_ID WHERE bo.Subscriber_ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(subscriberSearchField.getText()));
            ResultSet rs = pstmt.executeQuery();
            // Implement loading results into JTable
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new LoanManagementInterface();
    }
}


