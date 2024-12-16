package librarymanager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DashboardInterface extends JFrame {
    private JButton bookManagementButton;
    private JButton subscriberManagementButton;
    private JButton loanManagementButton;
    private JButton statisticsButton;

    // Constructor accepts the user's role (Admin/User)
    public DashboardInterface(String role) {
        setTitle("Library Manager - Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 1));

        // Create buttons based on user role
        bookManagementButton = new JButton("Manage Books");
        subscriberManagementButton = new JButton("Manage Subscribers");
        loanManagementButton = new JButton("Manage Loans");
        statisticsButton = new JButton("View Statistics");

        // If the role is Admin, allow access to all functionalities
        if (role.equals("Admin")) {
            // Admin can access all features
            add(bookManagementButton);
            add(subscriberManagementButton);
            add(loanManagementButton);
            add(statisticsButton);
        } else {
            // Regular User can only view loans and statistics
            add(loanManagementButton);
            add(statisticsButton);
        }

        // Add event listeners for buttons
        bookManagementButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open book management interface
                JOptionPane.showMessageDialog(DashboardInterface.this, "Book Management");
                new BookManagementInterface();
            }
        });

        subscriberManagementButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open subscriber management interface
                JOptionPane.showMessageDialog(DashboardInterface.this, "Subscriber Management");
                 new SubscriberManagementInterface();
            }
        });

        loanManagementButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open loan management interface
                JOptionPane.showMessageDialog(DashboardInterface.this, "Loan Management");
                new LoanManagementInterface();
    
            }
        });

        statisticsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open statistics interface
                JOptionPane.showMessageDialog(DashboardInterface.this, "Statistics");
                new StatisticsInterface();
            }
        });

        setVisible(true);
    }
}
