import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class PassengerInformationGUI {
    private JFrame frame;
    private String username;
    private String selectedFlight;

    // Form fields for passenger information
    private JTextField nameField, ageField, emailField, contactField;

    public PassengerInformationGUI(String username, String selectedFlight) {
        this.username = username;
        this.selectedFlight = selectedFlight;

        // Create the frame for Passenger Information
        frame = new JFrame("Passenger Information");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);

        // Set a nice look with a modern layout (GridBagLayout)
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Adds space between components

        // Title label for selected flight
        JLabel flightLabel = new JLabel("You have selected: " + selectedFlight);
        flightLabel.setFont(new Font("Arial", Font.BOLD, 16));
        flightLabel.setForeground(new Color(0, 123, 255));
        gbc.gridwidth = 2; // Span across two columns
        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.add(flightLabel, gbc);

        // Name label and field
        JLabel nameLabel = new JLabel("Enter your Name:");
        nameField = new JTextField(20);
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.add(nameLabel, gbc);
        gbc.gridx = 1;
        frame.add(nameField, gbc);

        // Age label and field
        JLabel ageLabel = new JLabel("Enter your Age:");
        ageField = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 2;
        frame.add(ageLabel, gbc);
        gbc.gridx = 1;
        frame.add(ageField, gbc);

        // Email label and field
        JLabel emailLabel = new JLabel("Enter your Email:");
        emailField = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 3;
        frame.add(emailLabel, gbc);
        gbc.gridx = 1;
        frame.add(emailField, gbc);

        // Contact number label and field
        JLabel contactLabel = new JLabel("Enter your Contact Number:");
        contactField = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 4;
        frame.add(contactLabel, gbc);
        gbc.gridx = 1;
        frame.add(contactField, gbc);

        // Submit and Book button
        JButton bookButton = new JButton("Submit and Book");
        bookButton.setFont(new Font("Arial", Font.BOLD, 14));
        bookButton.setBackground(new Color(0, 123, 255));
        bookButton.setForeground(Color.WHITE);
        bookButton.setFocusPainted(false);
        gbc.gridwidth = 2; // Span across two columns
        gbc.gridx = 0;
        gbc.gridy = 5;
        frame.add(bookButton, gbc);

        // Action for Submit and Book button
        bookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Collecting entered data
                String name = nameField.getText();
                String age = ageField.getText();
                String email = emailField.getText();
                String contact = contactField.getText();

                if (name.isEmpty() || age.isEmpty() || email.isEmpty() || contact.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill all fields.");
                } else {
                    // Save booking to a text file
                    saveBookingToFile(name, age, email, contact);
                    JOptionPane.showMessageDialog(frame, "Booking Confirmed!");

                    // Optionally, you can display booking information here
                    frame.dispose();
                }
            }
        });

        // Set the background color for the frame
        frame.getContentPane().setBackground(new Color(242, 242, 242));

        // Display the frame
        frame.setVisible(true);
    }

    // Method to save booking details into a text file
    private void saveBookingToFile(String name, String age, String email, String contact) {
        try {
            FileWriter writer = new FileWriter("bookings.txt", true);
            writer.append("Username: ").append(username).append("\n"); // Save actual username
            writer.append("Selected Flight: ").append(selectedFlight).append("\n");
            writer.append("Name: ").append(name).append("\n");
            writer.append("Age: ").append(age).append("\n");
            writer.append("Email: ").append(email).append("\n");
            writer.append("Contact: ").append(contact).append("\n");
            writer.append("------------\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Test with a sample flight selection and username
        new PassengerInformationGUI("testuser", "Air India - 10:00 AM - Mumbai");
    }
}
