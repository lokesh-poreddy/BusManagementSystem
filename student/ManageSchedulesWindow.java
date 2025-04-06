package student;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ManageSchedulesWindow extends JFrame {
    private JComboBox<String> busSelect, routeSelect;
    private JTextField departureTimeField, arrivalTimeField, frequencyField;
    private JCheckBox[] daysCheckboxes;
    private final String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    public ManageSchedulesWindow() {
        setTitle("Manage Schedules");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(9, 2, 5, 5));

        panel.add(new JLabel("Select Bus:"));
        busSelect = new JComboBox<>();
        loadBuses();
        panel.add(busSelect);

        panel.add(new JLabel("Select Route:"));
        routeSelect = new JComboBox<>();
        loadRoutes();
        panel.add(routeSelect);

        panel.add(new JLabel("Departure Time (HH:MM:SS):"));
        departureTimeField = new JTextField();
        panel.add(departureTimeField);

        panel.add(new JLabel("Arrival Time (HH:MM:SS):"));
        arrivalTimeField = new JTextField();
        panel.add(arrivalTimeField);

        panel.add(new JLabel("Frequency (minutes):"));
        frequencyField = new JTextField();
        panel.add(frequencyField);

        panel.add(new JLabel("Operating Days:"));
        JPanel daysPanel = new JPanel(new FlowLayout());
        daysCheckboxes = new JCheckBox[days.length];
        for (int i = 0; i < days.length; i++) {
            daysCheckboxes[i] = new JCheckBox(days[i]);
            daysPanel.add(daysCheckboxes[i]);
        }
        panel.add(daysPanel);

        JButton addButton = new JButton("Add Schedule");
        addButton.addActionListener(e -> addSchedule());
        panel.add(addButton);

        add(panel);
    }

    private void loadBuses() {
        try (Connection conn = BusManagementSystem.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT bus_name FROM Buses");
            while (rs.next()) {
                busSelect.addItem(rs.getString("bus_name"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void loadRoutes() {
        try (Connection conn = BusManagementSystem.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT route_name FROM Routes");
            while (rs.next()) {
                routeSelect.addItem(rs.getString("route_name"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void addSchedule() {
        String bus = (String) busSelect.getSelectedItem();
        String route = (String) routeSelect.getSelectedItem();
        String departureTime = departureTimeField.getText();
        String arrivalTime = arrivalTimeField.getText();
        String frequency = frequencyField.getText();

        StringBuilder operatingDays = new StringBuilder();
        for (int i = 0; i < days.length; i++) {
            if (daysCheckboxes[i].isSelected()) {
                if (operatingDays.length() > 0) operatingDays.append(",");
                operatingDays.append(days[i]);
            }
        }

        try (Connection conn = BusManagementSystem.getConnection()) {
            String query = "INSERT INTO Schedules (bus_name, route_name, departure_time, arrival_time, frequency, operating_days) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, bus);
            ps.setString(2, route);
            ps.setTime(3, Time.valueOf(departureTime));
            ps.setTime(4, Time.valueOf(arrivalTime));
            ps.setInt(5, Integer.parseInt(frequency));
            ps.setString(6, operatingDays.toString());
            ps.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Schedule added successfully!");
            clearFields();
        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error adding schedule: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void clearFields() {
        departureTimeField.setText("");
        arrivalTimeField.setText("");
        frequencyField.setText("");
        for (JCheckBox box : daysCheckboxes) {
            box.setSelected(false);
        }
    }
}