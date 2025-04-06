package student;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class UserRegistrationWindow extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;

    public UserRegistrationWindow() {
        setTitle("User Registration");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        panel.add(new JLabel("Role:"));
        String[] roles = {"User", "Admin"};
        roleComboBox = new JComboBox<>(roles);
        panel.add(roleComboBox);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> registerUser());
        panel.add(registerButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        panel.add(cancelButton);

        add(panel);
    }

    private void registerUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields");
            return;
        }

        try (Connection conn = BusManagementSystem.getConnection()) {
            // Check if username already exists
            String checkQuery = "SELECT username FROM Users WHERE username = ?";
            PreparedStatement checkPs = conn.prepareStatement(checkQuery);
            checkPs.setString(1, username);
            ResultSet rs = checkPs.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Username already exists!");
                return;
            }

            // Insert new user
            String insertQuery = "INSERT INTO Users (username, password, role) VALUES (?, ?, ?)";
            PreparedStatement insertPs = conn.prepareStatement(insertQuery);
            insertPs.setString(1, username);
            insertPs.setString(2, password);
            insertPs.setString(3, role);
            insertPs.executeUpdate();

            JOptionPane.showMessageDialog(this, "Registration successful!");
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error during registration: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}