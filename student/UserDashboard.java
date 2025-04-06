package student;

import javax.swing.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class UserDashboard extends JFrame {
    private final String username;

    public UserDashboard(String username) {
        this.username = username;
        setTitle("User Dashboard - " + username);
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JButton viewSchedulesButton = new JButton("View Bus Schedules");
        viewSchedulesButton.addActionListener(e -> viewSchedules());
        
        JButton viewRoutesButton = new JButton("View Routes");
        viewRoutesButton.addActionListener(e -> viewRoutes());
        
        JButton bookTicketButton = new JButton("Book Ticket");
        bookTicketButton.addActionListener(e -> bookTicket());
        
        JButton viewBookingsButton = new JButton("View My Bookings");
        viewBookingsButton.addActionListener(e -> viewBookings());

        panel.add(Box.createVerticalStrut(20));
        panel.add(viewSchedulesButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(viewRoutesButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(bookTicketButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(viewBookingsButton);

        add(panel);
    }

    private void viewSchedules() {
        try (Connection conn = BusManagementSystem.getConnection()) {
            String query = "SELECT * FROM Schedules";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            // Create a table model to display the results
            JTable table = new JTable(buildTableModel(rs));
            JScrollPane scrollPane = new JScrollPane(table);
            
            JDialog dialog = new JDialog(this, "Bus Schedules", true);
            dialog.add(scrollPane);
            dialog.setSize(600, 400);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error viewing schedules: " + ex.getMessage());
        }
    }

    private void viewRoutes() {
        try (Connection conn = BusManagementSystem.getConnection()) {
            String query = "SELECT * FROM Routes";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            JTable table = new JTable(buildTableModel(rs));
            JScrollPane scrollPane = new JScrollPane(table);
            
            JDialog dialog = new JDialog(this, "Available Routes", true);
            dialog.add(scrollPane);
            dialog.setSize(600, 400);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error viewing routes: " + ex.getMessage());
        }
    }

    private void bookTicket() {
        // Implementation for booking tickets will go here
        new BookTicketWindow(username).setVisible(true);
    }

    private void viewBookings() {
        try (Connection conn = BusManagementSystem.getConnection()) {
            String query = "SELECT * FROM Bookings WHERE username = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            
            JTable table = new JTable(buildTableModel(rs));
            JScrollPane scrollPane = new JScrollPane(table);
            
            JDialog dialog = new JDialog(this, "My Bookings", true);
            dialog.add(scrollPane);
            dialog.setSize(600, 400);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error viewing bookings: " + ex.getMessage());
        }
    }

    private static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        
        // Create column names
        Vector<String> columnNames = new Vector<>();
        for (int i = 1; i <= columnCount; i++) {
            columnNames.add(metaData.getColumnName(i));
        }
        
        // Create data rows
        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                row.add(rs.getObject(i));
            }
            data.add(row);
        }
        
        return new DefaultTableModel(data, columnNames);
    }
}