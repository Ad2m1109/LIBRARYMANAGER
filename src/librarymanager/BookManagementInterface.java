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

public class BookManagementInterface extends JFrame {
    private JTable bookTable;
    private JTextField titleField, authorField, genreField, quantityField, searchField;
    private JButton addButton, modifyButton, deleteButton, viewAllButton, filterButton, outOfStockButton;

    public BookManagementInterface() {
        setTitle("Library Manager - Book Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Table
        bookTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(bookTable);
        add(scrollPane, BorderLayout.CENTER);

        // Input Fields
        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        inputPanel.add(new JLabel("Title:"));
        titleField = new JTextField();
        inputPanel.add(titleField);

        inputPanel.add(new JLabel("Author:"));
        authorField = new JTextField();
        inputPanel.add(authorField);

        inputPanel.add(new JLabel("Genre:"));
        genreField = new JTextField();
        inputPanel.add(genreField);

        inputPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        inputPanel.add(quantityField);

        inputPanel.add(new JLabel("Search by Author:"));
        searchField = new JTextField();
        inputPanel.add(searchField);

        add(inputPanel, BorderLayout.NORTH);

        // Buttons
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add");
        modifyButton = new JButton("Modify");
        deleteButton = new JButton("Delete");
        viewAllButton = new JButton("View All");
        filterButton = new JButton("Filter by Author");
        outOfStockButton = new JButton("Out of Stock");

        buttonPanel.add(addButton);
        buttonPanel.add(modifyButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewAllButton);
        buttonPanel.add(filterButton);
        buttonPanel.add(outOfStockButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Event Listeners
        addButton.addActionListener(e -> addBook());
        modifyButton.addActionListener(e -> modifyBook());
        deleteButton.addActionListener(e -> deleteBook());
        viewAllButton.addActionListener(e -> loadAllBooks());
        filterButton.addActionListener(e -> filterBooksByAuthor());
        outOfStockButton.addActionListener(e -> showOutOfStockBooks());

        setVisible(true);
    }

    // Add Book
    private void addBook() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/LibraryManager", "root", "root")) {
            String query = "INSERT INTO Books (Title, Author, Genre, Quantity_Available) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, titleField.getText());
            pstmt.setString(2, authorField.getText());
            pstmt.setString(3, genreField.getText());
            pstmt.setInt(4, Integer.parseInt(quantityField.getText()));
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Book added successfully!");
            loadAllBooks();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Modify Book
    private void modifyBook() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/LibraryManager", "root", "password")) {
            int selectedRow = bookTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Select a book to modify!");
                return;
            }

            int bookID = (int) bookTable.getValueAt(selectedRow, 0);
            String query = "UPDATE Books SET Title = ?, Author = ?, Genre = ?, Quantity_Available = ? WHERE Book_ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, titleField.getText());
            pstmt.setString(2, authorField.getText());
            pstmt.setString(3, genreField.getText());
            pstmt.setInt(4, Integer.parseInt(quantityField.getText()));
            pstmt.setInt(5, bookID);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Book modified successfully!");
            loadAllBooks();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Delete Book
    private void deleteBook() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/LibraryManager", "root", "password")) {
            int selectedRow = bookTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Select a book to delete!");
                return;
            }

            int bookID = (int) bookTable.getValueAt(selectedRow, 0);
            String query = "DELETE FROM Books WHERE Book_ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, bookID);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Book deleted successfully!");
            loadAllBooks();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Load All Books
    private void loadAllBooks() {
        // Implement method to load books into JTable
    }

    // Filter Books by Author
    private void filterBooksByAuthor() {
        // Implement method to filter books by author
    }

    // Show Out-of-Stock Books
    private void showOutOfStockBooks() {
        // Implement method to show out-of-stock books
    }

    public static void main(String[] args) {
        new BookManagementInterface();
    }
}
