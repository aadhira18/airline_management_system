import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class LoginGUI {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, cancelButton, helpButton;
    private JLabel passwordRequirementLabel;

    public LoginGUI() {
        // Create the frame
        frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 350); // Set window size
        frame.setLocationRelativeTo(null); // Center the window on screen

        // Create the custom panel with background
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.Y_AXIS));
        backgroundPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // Padding

        // Username Label and Field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));

        // Password Label and Field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));

        // Password Requirements Message
        passwordRequirementLabel = new JLabel("<html>Password must be at least 8 characters, contain at least one letter, one number, and one special character.</html>");
        passwordRequirementLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        passwordRequirementLabel.setForeground(Color.RED);

        // Login Button
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(0, 123, 255));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setPreferredSize(new Dimension(200, 40));

        // Cancel Button
        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setBackground(Color.RED);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setPreferredSize(new Dimension(200, 40));

        // Help Button
        helpButton = new JButton("Help");
        helpButton.setFont(new Font("Arial", Font.BOLD, 14));
        helpButton.setBackground(new Color(0, 123, 255));
        helpButton.setForeground(Color.WHITE);
        helpButton.setFocusPainted(false);
        helpButton.setPreferredSize(new Dimension(200, 40));

        // Add components to the background panel
        backgroundPanel.add(usernameLabel);
        backgroundPanel.add(Box.createVerticalStrut(10)); // Space between fields
        backgroundPanel.add(usernameField);
        backgroundPanel.add(Box.createVerticalStrut(20)); // Space between username and password
        backgroundPanel.add(passwordLabel);
        backgroundPanel.add(Box.createVerticalStrut(10)); // Space between password label and field
        backgroundPanel.add(passwordField);
        backgroundPanel.add(passwordRequirementLabel); // Password requirement message
        backgroundPanel.add(Box.createVerticalStrut(20)); // Space between password field and login button
        backgroundPanel.add(loginButton);
        backgroundPanel.add(Box.createVerticalStrut(10)); // Space between login and cancel button
        backgroundPanel.add(cancelButton);
        backgroundPanel.add(Box.createVerticalStrut(10)); // Space between cancel and help button
        backgroundPanel.add(helpButton);

        // Set action for the login button
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginAction();
            }
        });

        // Set action for the cancel button
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelAction();
            }
        });

        // Set action for the help button
        helpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                helpAction();
            }
        });

        // Add background panel to the frame
        frame.add(backgroundPanel);
        frame.setVisible(true);
    }

    // Custom JPanel to draw background image
    class BackgroundPanel extends JPanel {
        private ImageIcon backgroundImage;

        public BackgroundPanel() {
            // Load the background image (adjust path as needed)
            backgroundImage = new ImageIcon("airport.jpg");
        }

        // Override paintComponent to draw the background image
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Scale the background image to fill the panel
            g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    }

    // Action to perform when the login button is clicked
    private void loginAction() {
        String username = usernameField.getText().trim(); // Remove leading/trailing spaces
        String password = new String(passwordField.getPassword()).trim(); // Remove leading/trailing spaces

        // Validate password
        if (!isValidPassword(password)) {
            JOptionPane.showMessageDialog(frame, "Password does not meet the required criteria.", "Invalid Password", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill in both username and password.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Read users from file to verify credentials
        try {
            File file = new File("users.txt");
            if (!file.exists()) {
                JOptionPane.showMessageDialog(frame, "Error: users.txt file is missing!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            boolean userFound = false;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                String[] userDetails = line.split(",");
                if (userDetails.length == 2) {
                    String fileUsername = userDetails[0].trim();
                    String filePassword = userDetails[1].trim();

                    if (fileUsername.equals(username) && filePassword.equals(password)) {
                        JOptionPane.showMessageDialog(frame, "Login successful!");
                        userFound = true;
                        new UserOptionsGUI(username); // Pass username to UserOptionsGUI
                        frame.dispose(); // Close the login window
                        break;
                    }
                }
            }

            if (!userFound) {
                JOptionPane.showMessageDialog(frame, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error reading user data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Action to perform when the cancel button is clicked
    private void cancelAction() {
        // Close the login window and exit
        int response = JOptionPane.showConfirmDialog(frame, "Are you sure you want to cancel?", "Cancel", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            frame.dispose(); // Close the login window
            System.exit(0); // Exit the application
        }
    }

    // Action to show the help window
    private void helpAction() {
        // Display help information in a message dialog
        String helpMessage = "Welcome to the Airline Reservation System!\n\n" +
                "To log in, use the username and password combination.\n" +
                "For any further assistance, please contact support.";
        JOptionPane.showMessageDialog(frame, helpMessage, "Help", JOptionPane.INFORMATION_MESSAGE);
    }

    // Password validation method (checks for at least 8 characters, letters, numbers, and special characters)
    private boolean isValidPassword(String password) {
        return password.length() >= 8 && 
               password.matches(".*[A-Za-z].*") && 
               password.matches(".*[0-9].*") && 
               password.matches(".*[!@#$%^&*()].*");
    }

    public static void main(String[] args) {
        new LoginGUI(); // Launch the Login page
    }
}
