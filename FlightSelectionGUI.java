import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class FlightSelectionGUI {
    private JFrame frame;
    private JComboBox<String> flightComboBox;
    private JTextArea scheduleArea;
    private JTextField usernameField;

    public FlightSelectionGUI() {
        initializeGUI(null); // If no previous frame is passed, assume this is the first screen
    }

    public FlightSelectionGUI(JFrame previousFrame) {
        initializeGUI(previousFrame); // Pass the previous frame for back navigation
    }

    private void initializeGUI(JFrame previousFrame) {
        frame = new JFrame("Flight Selection and Schedule");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);

        // Set the layout of the main frame
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add space between components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title label for Flight Selection
        JLabel titleLabel = new JLabel("Select a Flight and Enter Username");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(0, 123, 255));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.add(titleLabel, gbc);

        // Username label and field
        JLabel usernameLabel = new JLabel("Enter your username:");
        usernameField = new JTextField(20); // Set a reasonable size for the username field
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.add(usernameLabel, gbc);
        gbc.gridx = 1;
        frame.add(usernameField, gbc);

        // Flight selection label and combo box
        JLabel flightLabel = new JLabel("Select your flight:");
        String[] flights = {
            "Air India - 10:00 AM - Mumbai",
            "SpiceJet - 12:00 PM - Bangalore",
            "IndiGo - 2:00 PM - Goa",
            "GoAir - 4:00 PM - Kolkata"
        };
        flightComboBox = new JComboBox<>(flights);
        gbc.gridx = 0;
        gbc.gridy = 2;
        frame.add(flightLabel, gbc);
        gbc.gridx = 1;
        frame.add(flightComboBox, gbc);

        // Flight schedule label and area (text display)
        JLabel scheduleLabel = new JLabel("Flight Schedule (from Delhi):");
        String schedule = "1. Air India - 10:00 AM - Mumbai\n"
                         + "2. SpiceJet - 12:00 PM - Bangalore\n"
                         + "3. IndiGo - 2:00 PM - Goa\n"
                         + "4. GoAir - 4:00 PM - Kolkata";
        scheduleArea = new JTextArea(schedule);
        scheduleArea.setEditable(false);
        scheduleArea.setFont(new Font("Arial", Font.PLAIN, 14));
        scheduleArea.setLineWrap(true);
        scheduleArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(scheduleArea);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        frame.add(scheduleLabel, gbc);
        gbc.gridy = 4;
        frame.add(scrollPane, gbc);

        // Book Flight button
        JButton bookButton = createButton("Book Flight", new Color(0, 123, 255), e -> bookFlight());

        // View My Bookings button
        JButton viewBookingsButton = createButton("View My Bookings", new Color(0, 123, 255), e -> viewBookings());

        // Back button for navigation
        if (previousFrame != null) {
            JButton backButton = createButton("Back", new Color(192, 192, 192), e -> {
                frame.dispose();
                previousFrame.setVisible(true);
            });
            gbc.gridx = 0;
            gbc.gridy = 6;
            gbc.gridwidth = 1;
            frame.add(backButton, gbc);
        }

        // Add Book and View buttons
        gbc.gridx = previousFrame == null ? 0 : 1; // Adjust position if back button is present
        gbc.gridy = 5;
        gbc.gridwidth = previousFrame == null ? 2 : 1;
        frame.add(bookButton, gbc);

        if (previousFrame != null) {
            gbc.gridx = 1;
            gbc.gridy = 6;
            gbc.gridwidth = 1;
            frame.add(viewBookingsButton, gbc);
        } else {
            gbc.gridy = 6;
            frame.add(viewBookingsButton, gbc);
        }

        // Set the background color for the frame
        frame.getContentPane().setBackground(new Color(242, 242, 242));

        // Display the frame
        frame.setVisible(true);
    }

    private void bookFlight() {
        String username = usernameField.getText(); // Get the entered username
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter a username.");
        } else {
            String selectedFlight = (String) flightComboBox.getSelectedItem();
            JOptionPane.showMessageDialog(frame, "You have selected: " + selectedFlight);
            frame.dispose(); // Close this frame

            // Pass the username and selected flight to the next screen
            new PassengerInformationGUI(username, selectedFlight);
        }
    }

    private void viewBookings() {
        String username = usernameField.getText();
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter a username.");
        } else {
            displayUserBookings(username);
        }
    }

    // Method to display the user's bookings from the bookings file
    private void displayUserBookings(String username) {
        ArrayList<String> userBookings = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("bookings.txt"))) {
            String line;
            boolean isUserBooking = false;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Username: " + username)) {
                    isUserBooking = true;
                    userBookings.add(line); // Add the "Username" line
                } else if (isUserBooking) {
                    userBookings.add(line);
                    if (line.equals("------------")) {
                        isUserBooking = false;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (userBookings.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No bookings found for this username.");
        } else {
            JFrame bookingFrame = new JFrame("Your Bookings");
            JTextArea bookingArea = new JTextArea();
            bookingArea.setEditable(false);
            bookingArea.setFont(new Font("Arial", Font.PLAIN, 14));

            for (String booking : userBookings) {
                bookingArea.append(booking + "\n");
            }

            JScrollPane scrollPane = new JScrollPane(bookingArea);
            bookingFrame.add(scrollPane);
            bookingFrame.setSize(400, 400);
            bookingFrame.setLocationRelativeTo(null);
            bookingFrame.setVisible(true);
        }
    }

    // Helper method to create buttons
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
        new FlightSelectionGUI();
    }
}
