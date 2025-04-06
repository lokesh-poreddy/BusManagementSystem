package student;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class BookTicketWindow extends JFrame {
    private final String username;
    private JComboBox<String> busSelect, routeSelect;
    private JSpinner dateSpinner;
    private JSpinner.DateEditor dateEditor;
    private JTextField seatsField;

    public BookTicketWindow(String username) {
        this.username = username;
        setTitle("Book Ticket");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Select Bus:"));
        busSelect = new JComboBox<>();
        loadBuses();
        panel.add(busSelect);

        panel.add(new JLabel("Select Route:"));
        routeSelect = new JComboBox<>();
        loadRoutes();
        panel.add(routeSelect);

        panel.add(new JLabel("Travel Date:"));
        dateSpinner = new JSpinner(new SpinnerDateModel());
        dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        panel.add(dateSpinner);

        panel.add(new JLabel("Number of Seats:"));
        seatsField = new JTextField();
        panel.add(seatsField);

        JButton bookButton = new JButton("Book Ticket");
        bookButton.addActionListener(e -> bookTicket());
        panel.add(bookButton);

        add(panel);
    }

    private void loadBuses() {
        try (Connection conn = BusManagementSystem.getConnection()) {
            String query = "SELECT bus_name FROM Buses";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                busSelect.addItem(rs.getString("bus_name"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading buses: " + ex.getMessage());
        }
    }

    private void loadRoutes() {
        try (Connection conn = BusManagementSystem.getConnection()) {
            String query = "SELECT route_name FROM Routes";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                routeSelect.addItem(rs.getString("route_name"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading routes: " + ex.getMessage());
        }
    }

    private void bookTicket() {
        String busName = (String) busSelect.getSelectedItem();
        String routeName = (String) routeSelect.getSelectedItem();
        java.util.Date date = (java.util.Date) dateSpinner.getValue();
        int seats;

        try {
            seats = Integer.parseInt(seatsField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number of seats");
            return;
        }

        try (Connection conn = BusManagementSystem.getConnection()) {
            String query = "INSERT INTO Bookings (username, bus_name, route_name, travel_date, seats) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, busName);
            ps.setString(3, routeName);
            ps.setDate(4, new java.sql.Date(date.getTime()));
            ps.setInt(5, seats);
            ps.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Ticket booked successfully!");
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error booking ticket: " + ex.getMessage());
        }
    }
}