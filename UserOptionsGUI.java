import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserOptionsGUI {
    private JFrame frame;
    private String loggedInUser;

    // Constructor accepts username for personalized experience
    public UserOptionsGUI(String username) {
        this.loggedInUser = username;

        frame = new JFrame("User Options");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        // Panel with GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel welcomeLabel = new JLabel("Welcome, " + loggedInUser + "! What would you like to do?");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JButton bookFlightButton = createButton("Book a New Flight", new Color(0, 123, 255), e -> {
            frame.dispose();
            new FlightSelectionGUI(frame); // Open Flight Selection GUI with back navigation
        });

        JButton viewBookingButton = createButton("View My Bookings", new Color(0, 123, 255), e -> {
            frame.dispose();
            new ViewBookingsGUI(loggedInUser, frame); // Pass the username and current frame to ViewBookingsGUI
        });

        JButton cancelBookingButton = createButton("Cancel a Booking", new Color(255, 69, 58), e -> {
            frame.dispose();
            new CancelBookingGUI(loggedInUser, frame); // Open CancelBookingGUI with back navigation
        });

        // Add components to the panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(welcomeLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(bookFlightButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(viewBookingButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(cancelBookingButton, gbc);

        // Add panel to frame
        frame.add(panel);
        frame.setVisible(true);
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
        new UserOptionsGUI("testUser"); // Example for testing
    }
}  