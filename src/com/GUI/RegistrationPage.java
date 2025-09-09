package com.GUI;

import com.Entities.Admin;
import com.Entities.User;
import com.Services.AdminService;
import com.Services.UserService;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * RegistrationPage provides a user interface for new users to register for the Auction System.
 * It collects user details, handles input validation, and delegates registration to the service layer.
 */
public class RegistrationPage extends JFrame {

    // GUI components used for registration
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton registerButton;
    private JButton loginButton;
    private JLabel messageLabel;
    private JCheckBox adminCheckBox;

    /**
     * Constructs the RegistrationPage and initializes the UI.
     */
    public RegistrationPage() {
        super("Auction System - Registration");

        // Use BorderLayout to allow cleaner structure and separation of fields
        setLayout(new BorderLayout());

        // Main panel setup
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBackground(new Color(250, 250, 250));
        main.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Page Header
        JLabel header = new JLabel("Create an account for Auction System", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 24));
        header.setForeground(new Color(63, 81, 181));
        header.setAlignmentX(CENTER_ALIGNMENT);
        main.add(header);
        main.add(Box.createRigidArea(new Dimension(0, 20)));

        // Subheading
        JLabel subHeading = new JLabel("Join our auction community today");
        subHeading.setFont(new Font("Arial", Font.BOLD, 20));
        subHeading.setForeground(new Color(117, 117, 117));
        subHeading.setAlignmentX(CENTER_ALIGNMENT);
        main.add(subHeading);
        main.add(Box.createRigidArea(new Dimension(0, 20)));

        // Input Panel setup
        JPanel input = new JPanel();
        input.setLayout(new BoxLayout(input, BoxLayout.Y_AXIS));
        input.setBackground(Color.WHITE);
        input.setPreferredSize(new Dimension(400, 300));
        input.setMaximumSize(new Dimension(600, 450));
        input.setBorder(new EmptyBorder(60, 20, 20, 50));

        // Name Field
        JPanel name = new JPanel();
        name.setLayout(new BoxLayout(name, BoxLayout.X_AXIS));
        name.setBackground(Color.WHITE);
        name.add(new JLabel("Full Name:          "));
        name.add(Box.createRigidArea(new Dimension(10, 10)));
        nameField = new JTextField(20);
        nameField.setPreferredSize(new Dimension(150, 30));
        nameField.setMaximumSize(new Dimension(240, 30));
        nameField.setToolTipText("Enter your full name");
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        name.add(nameField);
        input.add(name);
        input.add(Box.createRigidArea(new Dimension(0, 20)));

        // Email Field
        JPanel email = new JPanel();
        email.setLayout(new BoxLayout(email, BoxLayout.X_AXIS));
        email.setBackground(Color.WHITE);
        email.add(new JLabel("Email Address:   "));
        email.add(Box.createRigidArea(new Dimension(7, 0)));
        emailField = new JTextField(20);
        emailField.setPreferredSize(new Dimension(150, 30));
        emailField.setMaximumSize(new Dimension(240, 30));
        emailField.setToolTipText("Enter your email address");
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        email.add(emailField);
        input.add(email);
        input.add(Box.createRigidArea(new Dimension(0, 20)));

        // Password Field
        JPanel password = new JPanel();
        password.setLayout(new BoxLayout(password, BoxLayout.X_AXIS));
        password.setBackground(Color.WHITE);
        password.add(new JLabel("Password:        "));
        password.add(Box.createRigidArea(new Dimension(15, 0)));
        passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(150, 30));
        passwordField.setMaximumSize(new Dimension(240, 30));
        passwordField.setToolTipText("Enter a strong password");
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        password.add(passwordField);
        input.add(password);
        input.add(Box.createRigidArea(new Dimension(0, 20)));

        // Confirm Password Field
        JPanel confirmPassword = new JPanel();
        confirmPassword.setLayout(new BoxLayout(confirmPassword, BoxLayout.X_AXIS));
        confirmPassword.setBackground(Color.WHITE);
        confirmPassword.add(new JLabel("Confirm Password:"));
        confirmPassword.add(Box.createRigidArea(new Dimension(10, 0)));
        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setPreferredSize(new Dimension(150, 30));
        confirmPasswordField.setMaximumSize(new Dimension(220, 30));
        confirmPasswordField.setToolTipText("Re-enter your password");
        confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 14));
        confirmPassword.add(confirmPasswordField);
        input.add(confirmPassword);
        input.add(Box.createRigidArea(new Dimension(0, 10)));

        // Admin Checkbox
        adminCheckBox = new JCheckBox("Register as Admin");
        adminCheckBox.setBackground(Color.WHITE);
        adminCheckBox.setFont(new Font("Arial", Font.BOLD, 13));
        input.add(adminCheckBox);
        input.add(Box.createRigidArea(new Dimension(0, 10)));

        main.add(input);

        // Bottom Panel for buttons and messages
        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setBackground(new Color(250, 250, 250));
        bottom.setBorder(new EmptyBorder(10, 20, 20, 20));

        // Register Button
        registerButton = new JButton("Register");
        registerButton.setAlignmentX(CENTER_ALIGNMENT);
        registerButton.setBackground(new Color(32, 178, 170));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 18));
        registerButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        bottom.add(Box.createHorizontalGlue());
        bottom.add(registerButton);
        bottom.add(Box.createRigidArea(new Dimension(0, 10)));

        // Login Button
        loginButton = new JButton("Login");
        loginButton.setAlignmentX(CENTER_ALIGNMENT);
        loginButton.setBackground(new Color(255, 64, 129));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 18));
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        bottom.add(Box.createHorizontalGlue());
        bottom.add(loginButton);
        bottom.add(Box.createRigidArea(new Dimension(0, 10)));

        // Message Label
        messageLabel = new JLabel("Please fill in your details", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        messageLabel.setForeground(new Color(117, 117, 117));
        messageLabel.setAlignmentX(CENTER_ALIGNMENT);
        bottom.add(messageLabel);

        main.add(bottom);
        add(main, BorderLayout.CENTER);

        // Frame settings
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Attach event listeners
        attachEventListeners();
    }

    /**
     * Attaches the event listeners for register and login buttons.
     */
    private void attachEventListeners() {
        registerButton.addActionListener(e -> onRegisterAction());
        loginButton.addActionListener(e -> onLoginAction());
    }

    /**
     * Event handler for the register action.
     * Retrieves user inputs, validates them, and calls the service layer to register the user.
     */
    private void onRegisterAction() {
        performRegistration();
    }

    /**
     * Event handler for the login action.
     * Opens the LoginPage and disposes of the current registration page.
     */
    private void onLoginAction() {
        LoginPage loginPage = new LoginPage();
        loginPage.setVisible(true);
        dispose();
    }

    /**
     * Handles user registration by validating inputs and delegating to the service layer.
     */
    private void performRegistration() {
        // Get user inputs
        String name = nameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        boolean isAdmin = adminCheckBox.isSelected();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            messageLabel.setText("All fields are required");
            messageLabel.setForeground(new Color(244, 67, 54));
            return;
        }

        if (!password.equals(confirmPassword)) {
            messageLabel.setText("Passwords don't match, try again");
            messageLabel.setForeground(new Color(244, 67, 54));
            return;
        }

        try {
            if (isAdmin) {
                Admin admin = new Admin(0, 0, name, email, password, "Super Admin");
                AdminService.registerAdmin(admin);
                messageLabel.setText("Admin Registration successful. Return to Login Page");
            } else {
                User user = new User(0, name, email, password);
                UserService.registerUser(user);
                messageLabel.setText("Registration successful. Return to Login Page");
                messageLabel.setForeground(new Color(76, 175, 80));
            }
            // Optionally navigate to Login Page automatically.
        } catch (Exception e) {
            messageLabel.setText("Database error: " + e.getMessage());
            messageLabel.setForeground(new Color(244, 67, 54));
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegistrationPage().setVisible(true));
    }
}

