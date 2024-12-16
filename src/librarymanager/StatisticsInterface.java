/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanager;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class StatisticsInterface extends JFrame {
    private JTable statsTable;
    private JButton generateStatsButton;
    private JLabel totalBooksLabel, booksAvailableLabel, booksBorrowedLabel;

    public StatisticsInterface() {
        setTitle("Library Manager - Statistics");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Table for statistics
        statsTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(statsTable);
        add(scrollPane, BorderLayout.CENTER);

        // Labels for total books and borrowed books
        JPanel infoPanel = new JPanel(new GridLayout(3, 2));
        infoPanel.add(new JLabel("Total Books:"));
        totalBooksLabel = new JLabel("0");
        infoPanel.add(totalBooksLabel);

        infoPanel.add(new JLabel("Books Available:"));
        booksAvailableLabel = new JLabel("0");
        infoPanel.add(booksAvailableLabel);

        infoPanel.add(new JLabel("Books Borrowed:"));
        booksBorrowedLabel = new JLabel("0");
        infoPanel.add(booksBorrowedLabel);

        add(infoPanel, BorderLayout.NORTH);

        // Button to generate statistics
        generateStatsButton = new JButton("Generate Statistics");
        generateStatsButton.addActionListener(e -> generateStatistics());
        add(generateStatsButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Method to generate and display statistics
    private void generateStatistics() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/LibraryManager", "root", "root")) {
            // Get most borrowed books
            String query1 = "SELECT b.Title, COUNT(*) AS BorrowCount FROM Borrowing bo JOIN Books b ON bo.Book_ID = b.Book_ID GROUP BY bo.Book_ID ORDER BY BorrowCount DESC LIMIT 10";
            Statement stmt1 = conn.createStatement();
            ResultSet rs1 = stmt1.executeQuery(query1);
            loadTableData(rs1);

            // Get total books, available, and borrowed
            String query2 = "SELECT (SELECT COUNT(*) FROM Books) AS TotalBooks, (SELECT SUM(Quantity_Available) FROM Books) AS BooksAvailable, (SELECT SUM(Quantity_Available - b.Quantity_Available) FROM Books b JOIN Borrowing bo ON bo.Book_ID = b.Book_ID WHERE bo.Return_Date IS NULL) AS BooksBorrowed";
            Statement stmt2 = conn.createStatement();
            ResultSet rs2 = stmt2.executeQuery(query2);
            if (rs2.next()) {
                totalBooksLabel.setText(String.valueOf(rs2.getInt("TotalBooks")));
                booksAvailableLabel.setText(String.valueOf(rs2.getInt("BooksAvailable")));
                booksBorrowedLabel.setText(String.valueOf(rs2.getInt("BooksBorrowed")));
            }

            // Get most active subscribers
            String query3 = "SELECT s.First_Name, s.Last_Name, COUNT(*) AS LoanCount FROM Borrowing bo JOIN Subscribers s ON bo.Subscriber_ID = s.Subscriber_ID GROUP BY bo.Subscriber_ID ORDER BY LoanCount DESC LIMIT 10";
            Statement stmt3 = conn.createStatement();
            ResultSet rs3 = stmt3.executeQuery(query3);
            // Handle and load data from rs3 (optional to display in another table or append to statsTable)
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Method to load data into JTable
    private void loadTableData(ResultSet rs) throws SQLException {
        // Get column names
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        String[] columnNames = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            columnNames[i - 1] = metaData.getColumnName(i);
        }

        // Get data rows
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        while (rs.next()) {
            Object[] row = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                row[i - 1] = rs.getObject(i);
            }
            model.addRow(row);
        }

        statsTable.setModel(model);
    }

    public static void main(String[] args) {
        new StatisticsInterface();
    }
}
