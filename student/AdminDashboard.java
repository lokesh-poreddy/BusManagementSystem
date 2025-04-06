package student;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {
    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JButton manageBusesButton = new JButton("Manage Buses");
        manageBusesButton.addActionListener(e -> new ManageBusesWindow().setVisible(true));

        JButton manageRoutesButton = new JButton("Manage Routes");
        manageRoutesButton.addActionListener(e -> new ManageRoutesWindow().setVisible(true));

        JButton manageSchedulesButton = new JButton("Manage Schedules");
        manageSchedulesButton.addActionListener(e -> new ManageSchedulesWindow().setVisible(true));

        JButton managePlacesButton = new JButton("Manage Important Places");
        managePlacesButton.addActionListener(e -> new ManagePlacesWindow().setVisible(true));

        panel.add(manageBusesButton);
        panel.add(manageRoutesButton);
        panel.add(manageSchedulesButton);
        panel.add(managePlacesButton);

        add(panel);
    }
}