package student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class BusManagementSystem extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private static final String URL = "jdbc:mysql://localhost:3306/BusManagementSystem";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "12345678";

    public BusManagementSystem() {
        setTitle("Bus Management System - Login");
        setSize(500, 300);  // Increased size further
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(44, 62, 80));  // Dark blue-gray background
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title Label
        JLabel titleLabel = new JLabel("Bus Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel loginPanel = new JPanel(new GridLayout(4, 2, 15, 15));
        loginPanel.setBackground(new Color(52, 73, 94));  // Slightly lighter blue-gray
        loginPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(41, 128, 185), 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        
        // Labels and Fields with enhanced visibility
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Buttons with distinct colors
        JButton loginButton = new JButton("Login");
        styleButton(loginButton, new Color(41, 128, 185));  // Blue
        
        JButton registerButton = new JButton("Register");
        styleButton(registerButton, new Color(39, 174, 96));  // Green
        
        // Add components
        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        loginPanel.add(registerButton);
        
        mainPanel.add(loginPanel, BorderLayout.CENTER);
        add(mainPanel);
    }
    
    private void styleButton(JButton button, Color backgroundColor) {
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    private void setThemeColors() {
        UIManager.put("Panel.background", new Color(230, 240, 250));  // Light blue
        UIManager.put("Button.background", new Color(51, 122, 183));  // Dark blue
        UIManager.put("Button.foreground", Color.WHITE);  // White text for buttons
        UIManager.put("Label.foreground", Color.BLACK);   // Black text for labels
        UIManager.put("TextField.background", Color.WHITE);  // White background for text fields
        UIManager.put("TextField.foreground", Color.BLACK);  // Black text for text fields
    }

    private class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            try (Connection conn = getConnection()) {
                String query = "SELECT role FROM Users WHERE username = ? AND password = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, username);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String role = rs.getString("role");
                    if ("admin".equals(role)) {
                        new AdminDashboard().setVisible(true);
                    } else {
                        new UserDashboard(username).setVisible(true);
                    }
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(BusManagementSystem.this,
                            "Invalid username or password",
                            "Login Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(BusManagementSystem.this,
                        "Database error: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openRegisterWindow() {
        new UserRegistrationWindow().setVisible(true);
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new BusManagementSystem().setVisible(true);
        });
    }
}