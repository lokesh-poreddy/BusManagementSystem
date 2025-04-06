package student;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ManageBusesWindow extends JFrame {
    private JTextField busNameField, originField, destinationField, departureTimeField, arrivalTimeField;

    public ManageBusesWindow() {
        setTitle("Manage Buses");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(6, 2));
        
        panel.add(new JLabel("Bus Name:"));
        busNameField = new JTextField();
        panel.add(busNameField);

        panel.add(new JLabel("Origin:"));
        originField = new JTextField();
        panel.add(originField);

        panel.add(new JLabel("Destination:"));
        destinationField = new JTextField();
        panel.add(destinationField);

        panel.add(new JLabel("Departure Time (HH:MM:SS):"));
        departureTimeField = new JTextField();
        panel.add(departureTimeField);

        panel.add(new JLabel("Arrival Time (HH:MM:SS):"));
        arrivalTimeField = new JTextField();
        panel.add(arrivalTimeField);

        JButton addButton = new JButton("Add Bus");
        addButton.addActionListener(e -> addBus());
        panel.add(addButton);

        add(panel);
    }

    private void addBus() {
        String busName = busNameField.getText();
        String origin = originField.getText();
        String destination = destinationField.getText();
        String departureTime = departureTimeField.getText();
        String arrivalTime = arrivalTimeField.getText();

        try (Connection conn = BusManagementSystem.getConnection()) {
            String query = "INSERT INTO Buses (bus_name, origin, destination, departure_time, arrival_time) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, busName);
            ps.setString(2, origin);
            ps.setString(3, destination);
            ps.setTime(4, Time.valueOf(departureTime));
            ps.setTime(5, Time.valueOf(arrivalTime));
            ps.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Bus added successfully!");
            clearFields();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error adding bus: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void clearFields() {
        busNameField.setText("");
        originField.setText("");
        destinationField.setText("");
        departureTimeField.setText("");
        arrivalTimeField.setText("");
    }
}