package student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class BusManagementSystem extends JFrame {
    private static final String URL = "jdbc:mysql://localhost:3306/students";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "12345678";  // Use your actual MySQL password
    
    private JTextField usernameField;
    private JPasswordField passwordField;

    public BusManagementSystem() {
        setTitle("Bus Management System - Login/Register");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setThemeColors();
        
        JPanel loginPanel = new JPanel(new GridLayout(3, 2));
        loginPanel.setBackground(new Color(230, 240, 250));
        
        loginPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        loginPanel.add(usernameField);
        
        loginPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        loginPanel.add(passwordField);
        
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginButtonListener());
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginPanel.add(loginButton);
        
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> openRegisterWindow());
        registerButton.setBackground(new Color(100, 149, 237));
        registerButton.setForeground(Color.WHITE);
        loginPanel.add(registerButton);
        
        add(loginPanel);
    }

    private void setThemeColors() {
        UIManager.put("Panel.background", new Color(245, 245, 250));
        UIManager.put("Button.background", new Color(100, 149, 237));
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Label.foreground", new Color(50, 50, 70));
        UIManager.put("TextField.background", new Color(240, 248, 255));
        UIManager.put("TextField.foreground", new Color(50, 50, 70));
    }

    private void openRegisterWindow() {
        new UserRegistrationWindow().setVisible(true);
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    private class LoginButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            
            try (Connection conn = getConnection()) {
                String query = "SELECT * FROM Users WHERE username = ? AND password = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, username);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();
                
                if (rs.next()) {
                    String role = rs.getString("role");
                    JOptionPane.showMessageDialog(BusManagementSystem.this, "Login successful as " + role);
                    if ("Admin".equals(role)) {
                        new AdminDashboard().setVisible(true);
                    } else {
                        new UserDashboard(username).setVisible(true);
                    }
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(BusManagementSystem.this, "Invalid username or password.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BusManagementSystem().setVisible(true));
    }
}