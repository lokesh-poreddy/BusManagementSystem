package student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewRoutesWindow extends JFrame {
    private JTable routeTable;
    private DefaultTableModel tableModel;

    public ViewRoutesWindow() {
        setTitle("Available Routes");
        setSize(700, 500);
        setLocationRelativeTo(null);
        
        // Create table model with columns
        String[] columns = {"Route ID", "Source", "Destination", "Distance (km)", "Fare (₹)"};
        tableModel = new DefaultTableModel(columns, 0);
        routeTable = new JTable(tableModel);
        routeTable.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Style the table
        routeTable.getTableHeader().setBackground(new Color(41, 128, 185));
        routeTable.getTableHeader().setForeground(Color.WHITE);
        routeTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        routeTable.setRowHeight(25);
        
        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(routeTable);
        add(scrollPane);
        
        loadRouteData();
    }

    private void loadRouteData() {
        try (Connection conn = BusManagementSystem.getConnection()) {
            String query = "SELECT * FROM Routes";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("route_id"),
                    rs.getString("source"),
                    rs.getString("destination"),
                    rs.getDouble("distance"),
                    rs.getDouble("fare")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error loading route data: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}