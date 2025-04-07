import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ViewBookingsGUI {
    private JFrame frame;
    private String username;
    private DefaultTableModel tableModel;

    public ViewBookingsGUI(String username, JFrame previousFrame) {
        this.username = username;

        frame = new JFrame("View Your Bookings");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);

        // Main Panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        // Top Panel for Title, Back Button, and Search Bar
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        JLabel titleLabel = new JLabel("Your Bookings", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 12));
        backButton.addActionListener(e -> {
            frame.dispose(); // Close the current frame
            previousFrame.setVisible(true); // Show the previous frame
        });

        // Search Bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(searchPanel, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Table Setup
        String[] columnNames = {"Flight", "Name", "Age", "Email", "Contact"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable bookingsTable = new JTable(tableModel);

        // Adjust table appearance
        bookingsTable.setShowGrid(true);
        bookingsTable.setGridColor(Color.LIGHT_GRAY);
        bookingsTable.setRowHeight(25);
        bookingsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        bookingsTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        bookingsTable.getColumnModel().getColumn(1).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(bookingsTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel for Refresh and Export Buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refreshButton = new JButton("Refresh Bookings");
        JButton exportButton = new JButton("Export to File");

        bottomPanel.add(refreshButton);
        bottomPanel.add(exportButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Load initial bookings
        reloadTableData();

        // Search Action
        searchButton.addActionListener(e -> {
            String query = searchField.getText().trim().toLowerCase();
            filterTableData(query);
        });

        // Refresh Button Action
        refreshButton.addActionListener(e -> reloadTableData());

        // Export Button Action
        exportButton.addActionListener(e -> exportBookingsToFile());

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private void reloadTableData() {
        tableModel.setRowCount(0); // Clear table

        try (BufferedReader reader = new BufferedReader(new FileReader("bookings.txt"))) {
            String line;
            String flight = "", name = "", age = "", email = "", contact = "";
            boolean userFound = false;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Username: " + username)) {
                    userFound = true;
                    while ((line = reader.readLine()) != null && !line.startsWith("Username: ")) {
                        if (line.startsWith("Selected Flight: ")) {
                            flight = line.replace("Selected Flight: ", "");
                        } else if (line.startsWith("Name: ")) {
                            name = line.replace("Name: ", "");
                        } else if (line.startsWith("Age: ")) {
                            age = line.replace("Age: ", "");
                        } else if (line.startsWith("Email: ")) {
                            email = line.replace("Email: ", "");
                        } else if (line.startsWith("Contact: ")) {
                            contact = line.replace("Contact: ", "");
                        }
                    }
                    tableModel.addRow(new Object[]{flight, name, age, email, contact});
                }
            }

            if (!userFound && tableModel.getRowCount() == 0) {
                tableModel.addRow(new Object[]{"No bookings available.", "", "", "", ""});
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error loading bookings: " + e.getMessage());
        }
    }

    private void filterTableData(String query) {
        for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
            String flight = tableModel.getValueAt(i, 0).toString().toLowerCase();
            String name = tableModel.getValueAt(i, 1).toString().toLowerCase();
            if (!flight.contains(query) && !name.contains(query)) {
                tableModel.removeRow(i);
            }
        }
    }

    private void exportBookingsToFile() {
        try (FileWriter writer = new FileWriter("exported_bookings.txt")) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                writer.write("Flight: " + tableModel.getValueAt(i, 0) + "\n");
                writer.write("Name: " + tableModel.getValueAt(i, 1) + "\n");
                writer.write("Age: " + tableModel.getValueAt(i, 2) + "\n");
                writer.write("Email: " + tableModel.getValueAt(i, 3) + "\n");
                writer.write("Contact: " + tableModel.getValueAt(i, 4) + "\n");
                writer.write("------------\n");
            }
            JOptionPane.showMessageDialog(frame, "Bookings exported successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error exporting bookings: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        JFrame dummyFrame = new JFrame();
        dummyFrame.setVisible(false);
        SwingUtilities.invokeLater(() -> new ViewBookingsGUI("testuser", dummyFrame));
    }
}
