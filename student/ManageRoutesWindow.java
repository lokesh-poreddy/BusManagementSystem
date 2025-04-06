package student;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ManageRoutesWindow extends JFrame {
    private JTextField routeNameField, startPointField, endPointField, distanceField;
    private JTextArea stopsField;

    public ManageRoutesWindow() {
        setTitle("Manage Routes");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        
        panel.add(new JLabel("Route Name:"));
        routeNameField = new JTextField();
        panel.add(routeNameField);

        panel.add(new JLabel("Start Point:"));
        startPointField = new JTextField();
        panel.add(startPointField);

        panel.add(new JLabel("End Point:"));
        endPointField = new JTextField();
        panel.add(endPointField);

        panel.add(new JLabel("Distance (km):"));
        distanceField = new JTextField();
        panel.add(distanceField);

        panel.add(new JLabel("Stops (one per line):"));
        stopsField = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(stopsField);
        panel.add(scrollPane);

        JButton addButton = new JButton("Add Route");
        addButton.addActionListener(e -> addRoute());
        panel.add(addButton);

        add(panel);
    }

    private void addRoute() {
        String routeName = routeNameField.getText();
        String startPoint = startPointField.getText();
        String endPoint = endPointField.getText();
        String distance = distanceField.getText();
        String stops = stopsField.getText();

        try (Connection conn = BusManagementSystem.getConnection()) {
            String query = "INSERT INTO Routes (route_name, start_point, end_point, distance, stops) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, routeName);
            ps.setString(2, startPoint);
            ps.setString(3, endPoint);
            ps.setDouble(4, Double.parseDouble(distance));
            ps.setString(5, stops);
            ps.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Route added successfully!");
            clearFields();
        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error adding route: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void clearFields() {
        routeNameField.setText("");
        startPointField.setText("");
        endPointField.setText("");
        distanceField.setText("");
        stopsField.setText("");
    }
}