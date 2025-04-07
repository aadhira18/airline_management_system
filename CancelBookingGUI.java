import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CancelBookingGUI {
    private JFrame frame;
    private String username;
    private DefaultTableModel tableModel;

    public CancelBookingGUI(String username) {
        this(username, null); // Default constructor with no back navigation
    }

    public CancelBookingGUI(String username, JFrame previousFrame) {
        this.username = username;

        frame = new JFrame("Cancel Booking - " + username);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JLabel titleLabel = new JLabel("Cancel a Booking for " + username);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Column names for the table
        String[] columnNames = {"Flight", "Name", "Age", "Email", "Contact"};

        // Data for the table
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable bookingsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookingsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Load user bookings into the table
        loadUserBookings();

        // Cancel Booking Button
        JButton cancelButton = createButton("Cancel Selected Booking", new Color(255, 69, 58), e -> {
            int selectedRow = bookingsTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(frame, "Please select a booking to cancel.");
            } else {
                String flight = tableModel.getValueAt(selectedRow, 0).toString();
                String name = tableModel.getValueAt(selectedRow, 1).toString();
                if (cancelBooking(flight, name)) {
                    tableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(frame, "Booking canceled successfully.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to cancel the booking.");
                }
            }
        });

        // Back Button
        if (previousFrame != null) {
            JButton backButton = createButton("Back", new Color(192, 192, 192), e -> {
                frame.dispose();
                previousFrame.setVisible(true);
            });
            JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 10, 10));
            bottomPanel.add(backButton);
            bottomPanel.add(cancelButton);
            panel.add(bottomPanel, BorderLayout.SOUTH);
        } else {
            panel.add(cancelButton, BorderLayout.SOUTH);
        }

        frame.add(panel);
        frame.setVisible(true);
    }

    private void loadUserBookings() {
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

            if (!userFound) {
                JOptionPane.showMessageDialog(frame, "No bookings found for user: " + username);
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(frame, "Bookings file not found. Please check the system.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean cancelBooking(String flight, String name) {
        List<String> updatedBookings = new ArrayList<>();
        boolean bookingCanceled = false;

        try (BufferedReader reader = new BufferedReader(new FileReader("bookings.txt"))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Username: " + username)) {
                    StringBuilder bookingBlock = new StringBuilder();
                    boolean isBookingToCancel = false;

                    do {
                        bookingBlock.append(line).append("\n");
                        if (line.startsWith("Selected Flight: " + flight)) {
                            isBookingToCancel = true;
                        }
                        line = reader.readLine();
                    } while (line != null && !line.startsWith("Username: "));

                    if (!isBookingToCancel || !bookingBlock.toString().contains("Name: " + name)) {
                        updatedBookings.add(bookingBlock.toString());
                    } else {
                        bookingCanceled = true;
                    }
                } else {
                    updatedBookings.add(line + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (bookingCanceled) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("bookings.txt"))) {
                for (String booking : updatedBookings) {
                    writer.write(booking);
                }
            } catch (IOException e) {
                e.printStackTrace();
                bookingCanceled = false; // Reset the flag in case of write failure
            }
        }
        return bookingCanceled;
    }

    private JButton createButton(String text, Color bgColor, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.addActionListener(actionListener);
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CancelBookingGUI("testuser"));
    }
}
