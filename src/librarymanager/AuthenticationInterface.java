package librarymanager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AuthenticationInterface extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel errorLabel;

    public AuthenticationInterface() {
        setTitle("Library Manager - Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 1)); // Increased rows for padding

        // Set background color
        getContentPane().setBackground(new Color(240, 240, 240));

        // Create form components with custom font
        Font font = new Font("Arial", Font.PLAIN, 14);
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(font);
        usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(font);
        passwordField = new JPasswordField();

        loginButton = new JButton("Login");
        loginButton.setFont(font);
        loginButton.setBackground(new Color(100, 149, 237)); // Cornflower Blue
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);

        errorLabel = new JLabel("", JLabel.CENTER);
        errorLabel.setForeground(Color.RED);

        // Add components to the frame
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(loginButton);
        add(errorLabel);

        // Add action listener for login button
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                authenticateUser();
            }
        });

        setVisible(true);
    }

    // Authenticate user against the database
    private void authenticateUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/LibraryManager", "root", "root")) {
            // Query to check if the username and password match
            String query = "SELECT Username, Role FROM USERS WHERE Username = ? AND Password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String role = rs.getString("Role");
                // Successfully authenticated, open Dashboard
                new DashboardInterface(role);  // Pass role to the Dashboard
                dispose();  // Close the login window
            } else {
                // Incorrect credentials
                errorLabel.setText("Invalid username or password.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            errorLabel.setText("Error connecting to database.");
        }
    }

    public static void main(String[] args) {
        new AuthenticationInterface();
    }
}