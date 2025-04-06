package student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ViewBusesWindow extends JFrame {
    private JTable busTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    public ViewBusesWindow() {
        setTitle("Available Buses");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setBackground(new Color(240, 240, 240));

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);
        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        JButton searchButton = createStyledButton("Search", new Color(41, 128, 185));
        searchButton.addActionListener(e -> searchBuses());
        
        searchPanel.add(new JLabel("Search Bus: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        mainPanel.add(searchPanel, BorderLayout.NORTH);

        // Create table with enhanced styling
        String[] columns = {"Bus ID", "Bus Number", "Type", "Capacity", "Route ID", "Source", "Destination"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // Make table read-only
            }
        };
        
        // Enhance table styling for better visibility
        busTable = new JTable(tableModel);
        busTable.setFont(new Font("Arial", Font.BOLD, 16));  // Larger, bold font
        busTable.setForeground(new Color(33, 33, 33));  // Dark text color
        busTable.setBackground(new Color(255, 255, 255));
        busTable.setSelectionBackground(new Color(173, 216, 230));  // Light blue selection
        busTable.setSelectionForeground(new Color(0, 0, 0));
        busTable.setGridColor(new Color(100, 100, 100));  // Darker grid lines
        busTable.setRowHeight(35);  // Taller rows
        
        // Add mouse listener for booking
        busTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {  // Double click to book
                    int row = busTable.getSelectedRow();
                    if (row != -1) {
                        bookTicket(row);
                    }
                }
            }
        });

        // Add booking instruction label
        JLabel instructionLabel = new JLabel("Double-click on a bus to book a ticket");
        instructionLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        instructionLabel.setForeground(new Color(100, 100, 100));
        buttonPanel.add(instructionLabel);
        busTable.setFont(new Font("Arial", Font.PLAIN, 14));
        busTable.setForeground(Color.BLACK);  // Ensure text is black
        busTable.setBackground(Color.WHITE);
        busTable.setGridColor(new Color(200, 200, 200));
        busTable.setRowHeight(30);
        busTable.getTableHeader().setBackground(new Color(41, 128, 185));
        busTable.getTableHeader().setForeground(Color.WHITE);
        busTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        // Add table to scroll pane with styling
        JScrollPane scrollPane = new JScrollPane(busTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Add control buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton refreshButton = createStyledButton("Refresh", new Color(46, 204, 113));
        refreshButton.addActionListener(e -> loadBusData());
        
        JButton exportButton = createStyledButton("Export to CSV", new Color(52, 152, 219));
        exportButton.addActionListener(e -> exportToCSV());

        buttonPanel.add(refreshButton);
        buttonPanel.add(exportButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        loadBusData();
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        return button;
    }

    private void loadBusData() {
        tableModel.setRowCount(0);  // Clear existing data
        try (Connection conn = BusManagementSystem.getConnection()) {
            String query = "SELECT b.*, r.source, r.destination FROM Buses b " +
                          "LEFT JOIN Routes r ON b.route_id = r.route_id";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("bus_id"),
                    rs.getString("bus_number"),
                    rs.getString("bus_type"),
                    rs.getInt("capacity"),
                    rs.getInt("route_id"),
                    rs.getString("source"),
                    rs.getString("destination")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error loading bus data: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchBuses() {
        String searchTerm = searchField.getText().toLowerCase();
        tableModel.setRowCount(0);
        try (Connection conn = BusManagementSystem.getConnection()) {
            String query = "SELECT b.*, r.source, r.destination FROM Buses b " +
                          "LEFT JOIN Routes r ON b.route_id = r.route_id " +
                          "WHERE LOWER(b.bus_number) LIKE ? OR LOWER(b.bus_type) LIKE ? " +
                          "OR LOWER(r.source) LIKE ? OR LOWER(r.destination) LIKE ?";
            PreparedStatement ps = conn.prepareStatement(query);
            String searchPattern = "%" + searchTerm + "%";
            for (int i = 1; i <= 4; i++) {
                ps.setString(i, searchPattern);
            }
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("bus_id"),
                    rs.getString("bus_number"),
                    rs.getString("bus_type"),
                    rs.getInt("capacity"),
                    rs.getInt("route_id"),
                    rs.getString("source"),
                    rs.getString("destination")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error searching buses: " + e.getMessage(),
                "Search Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save CSV File");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files", "csv"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                if (!file.getName().toLowerCase().endsWith(".csv")) {
                    file = new File(file.getParentFile(), file.getName() + ".csv");
                }
                
                FileWriter csv = new FileWriter(file);
                // Write headers
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    csv.append(tableModel.getColumnName(i));
                    if (i < tableModel.getColumnCount() - 1) csv.append(",");
                }
                csv.append("\n");
                
                // Write data
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    for (int j = 0; j < tableModel.getColumnCount(); j++) {
                        csv.append(tableModel.getValueAt(i, j).toString());
                        if (j < tableModel.getColumnCount() - 1) csv.append(",");
                    }
                    csv.append("\n");
                }
                
                csv.flush();
                csv.close();
                JOptionPane.showMessageDialog(this, "Data exported successfully!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                    "Error exporting data: " + e.getMessage(),
                    "Export Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void bookTicket(int row) {
        int busId = (int) tableModel.getValueAt(row, 0);
        String busNumber = (String) tableModel.getValueAt(row, 1);
        String source = (String) tableModel.getValueAt(row, 5);
        String destination = (String) tableModel.getValueAt(row, 6);
        
        // Show booking dialog
        String passengerName = JOptionPane.showInputDialog(this, 
            "Enter passenger name:", 
            "Book Ticket", 
            JOptionPane.PLAIN_MESSAGE);
            
        if (passengerName != null && !passengerName.trim().isEmpty()) {
            try (Connection conn = BusManagementSystem.getConnection()) {
                // Insert booking record
                String insertQuery = "INSERT INTO Bookings (bus_id, passenger_name, booking_date) VALUES (?, ?, NOW())";
                PreparedStatement ps = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, busId);
                ps.setString(2, passengerName);
                ps.executeUpdate();
                
                // Get booking ID
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int bookingId = rs.getInt(1);
                    showBill(bookingId, busNumber, passengerName, source, destination);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this,
                    "Error booking ticket: " + e.getMessage(),
                    "Booking Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showBill(int bookingId, String busNumber, String passengerName, String source, String destination) {
        StringBuilder bill = new StringBuilder();
        bill.append("=================================\n");
        bill.append("         BOOKING CONFIRMATION     \n");
        bill.append("=================================\n\n");
        bill.append("Booking ID: ").append(bookingId).append("\n");
        bill.append("Passenger Name: ").append(passengerName).append("\n");
        bill.append("Bus Number: ").append(busNumber).append("\n");
        bill.append("From: ").append(source).append("\n");
        bill.append("To: ").append(destination).append("\n");
        bill.append("Date: ").append(new java.util.Date()).append("\n\n");
        bill.append("=================================\n");
        bill.append("Thank you for choosing our service!\n");
        bill.append("=================================\n");

        JTextArea billArea = new JTextArea(bill.toString());
        billArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        billArea.setEditable(false);
        
        JOptionPane.showMessageDialog(this,
            new JScrollPane(billArea),
            "Booking Confirmation",
            JOptionPane.INFORMATION_MESSAGE);
    }
}