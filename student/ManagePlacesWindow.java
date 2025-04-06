package student;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ManagePlacesWindow extends JFrame {
    private JTextField placeNameField, addressField, latitudeField, longitudeField;
    private JComboBox<String> placeTypeCombo;

    public ManagePlacesWindow() {
        setTitle("Manage Important Places");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));

        panel.add(new JLabel("Place Name:"));
        placeNameField = new JTextField();
        panel.add(placeNameField);

        panel.add(new JLabel("Address:"));
        addressField = new JTextField();
        panel.add(addressField);

        panel.add(new JLabel("Latitude:"));
        latitudeField = new JTextField();
        panel.add(latitudeField);

        panel.add(new JLabel("Longitude:"));
        longitudeField = new JTextField();
        panel.add(longitudeField);

        panel.add(new JLabel("Place Type:"));
        String[] placeTypes = {"College", "Hospital", "Shopping Mall", "Bus Station", "Other"};
        placeTypeCombo = new JComboBox<>(placeTypes);
        panel.add(placeTypeCombo);

        JButton addButton = new JButton("Add Place");
        addButton.addActionListener(e -> addPlace());
        panel.add(addButton);

        add(panel);
    }

    private void addPlace() {
        String placeName = placeNameField.getText();
        String address = addressField.getText();
        String latitude = latitudeField.getText();
        String longitude = longitudeField.getText();
        String placeType = (String) placeTypeCombo.getSelectedItem();

        try (Connection conn = BusManagementSystem.getConnection()) {
            String query = "INSERT INTO ImportantPlaces (place_name, address, latitude, longitude, place_type) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, placeName);
            ps.setString(2, address);
            ps.setDouble(3, Double.parseDouble(latitude));
            ps.setDouble(4, Double.parseDouble(longitude));
            ps.setString(5, placeType);
            ps.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Place added successfully!");
            clearFields();
        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error adding place: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void clearFields() {
        placeNameField.setText("");
        addressField.setText("");
        latitudeField.setText("");
        longitudeField.setText("");
        placeTypeCombo.setSelectedIndex(0);
    }
}