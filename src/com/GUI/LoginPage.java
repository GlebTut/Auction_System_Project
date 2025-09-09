package com.GUI;

import com.Entities.Admin;
import com.Entities.User;
import com.Services.AdminService;
import com.Services.UserService;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * LoginPage provides a user interface for logging into the Auction System.
 * It supports both admin and user logins and transitions to the appropriate dashboard upon successful authentication.
 */
public class LoginPage extends JFrame {

    // GUI components
    private JTextField emailField;  // Used for entering email
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel messageLabel;    // Displays error or success messages

    /**
     * Constructs a LoginPage and initializes the UI components.
     */
    public LoginPage() {
        super("Auction System - Login");
        setLayout(new FlowLayout());

        // -------- MAIN PANEL SETUP --------
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBackground(new Color(250, 250, 250));
        main.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        main.add(createHeaderPanel());
        main.add(Box.createRigidArea(new Dimension(0, 20)));
        main.add(createFormPanel());
        main.add(Box.createRigidArea(new Dimension(0, 20)));
        main.add(createFooterPanel());

        add(main, BorderLayout.CENTER);

        // Frame settings.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);  // Center the window

        // Attach event listeners separately for modularization.
        attachEventListeners();
    }

    /**
     * Attaches event listeners to the login and register buttons.
     */
    private void attachEventListeners() {
        loginButton.addActionListener(e -> onLoginAction());
        registerButton.addActionListener(e -> onRegisterAction());
    }

    /**
     * Event handler for the login action.
     * Delegates the process to perform login.
     */
    private void onLoginAction() {
        performLogin();
    }

    /**
     * Event handler for the register action.
     * Opens the registration page.
     */
    private void onRegisterAction() {
        new RegistrationPage().setVisible(true);
    }

    /**
     * Creates the header panel with a title and subheading.
     *
     * @return the header JPanel.
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(250, 250, 250));

        JLabel headerLabel = new JLabel("Login to Auction System", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(new Color(63, 81, 181));
        headerLabel.setAlignmentX(CENTER_ALIGNMENT);
        headerPanel.add(headerLabel);

        JLabel subHeader = new JLabel("Welcome back to the Auction", SwingConstants.CENTER);
        subHeader.setFont(new Font("Arial", Font.PLAIN, 20));
        subHeader.setForeground(new Color(100, 100, 100));
        subHeader.setAlignmentX(CENTER_ALIGNMENT);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        headerPanel.add(subHeader);

        return headerPanel;
    }

    /**
     * Creates the form panel containing email and password input fields.
     *
     * @return the form JPanel.
     */
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setPreferredSize(new Dimension(400, 200));
        formPanel.setMaximumSize(new Dimension(500, 300));
        formPanel.setBorder(new EmptyBorder(20, 50, 20, 50));

        formPanel.add(createInputRow("Email Address:", "Enter your email address", emailField = new JTextField()));
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(createInputRow("Password:", "Enter your Password", passwordField = new JPasswordField()));
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        return formPanel;
    }

    /**
     * Creates an input row containing a label and an associated text field.
     *
     * @param labelText text for the label.
     * @param toolTip tooltip for the text field.
     * @param field the JTextField component.
     * @return the JPanel representing the input row.
     */
    private JPanel createInputRow(String labelText, String toolTip, JTextField field) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setBackground(Color.WHITE);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setPreferredSize(new Dimension(120, 30));
        row.add(label);
        row.add(Box.createRigidArea(new Dimension(5, 0)));

        field.setColumns(20);
        field.setToolTipText(toolTip);
        field.setFont(new Font("Arial", Font.PLAIN, 16));
        field.setMaximumSize(new Dimension(220, 30));
        row.add(field);
        row.setAlignmentX(CENTER_ALIGNMENT);

        return row;
    }

    /**
     * Creates the footer panel containing the login and register buttons along with a message label.
     *
     * @return the footer JPanel.
     */
    private JPanel createFooterPanel() {
        JPanel footer = new JPanel();
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
        footer.setBackground(new Color(250, 250, 250));
        footer.setBorder(new EmptyBorder(10, 20, 10, 20));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(new Color(250, 250, 250));

        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(32, 178, 170));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 18));
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        registerButton = new JButton("Register");
        registerButton.setBackground(new Color(233, 30, 99));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 18));
        registerButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(loginButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        buttonPanel.add(registerButton);
        buttonPanel.add(Box.createHorizontalGlue());

        footer.add(buttonPanel);
        footer.add(Box.createRigidArea(new Dimension(0, 10)));

        messageLabel = new JLabel("Please enter your Auction login credentials", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        messageLabel.setForeground(new Color(117, 117, 117));
        messageLabel.setAlignmentX(CENTER_ALIGNMENT);
        footer.add(messageLabel);

        return footer;
    }

    /**
     * Performs the login process.
     * Retrieves user input and attempts to authenticate as an admin first and then as a user.
     * Navigates to the appropriate dashboard upon successful login.
     */
    private void performLogin() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            messageLabel.setText("All fields are required");
            messageLabel.setForeground(new Color(244, 67, 54));
            return;
        }

        try {
            Admin admin = AdminService.loginAdmin(email, password);
            if (admin != null) {
                messageLabel.setText("Admin login successful");
                messageLabel.setForeground(new Color(76, 175, 80));
                new AdminDashboard().setVisible(true);
                dispose();
                return;
            }
            User user = UserService.loginUser(email, password);
            if (user != null) {
                messageLabel.setText("Login Successful");
                messageLabel.setForeground(new Color(76, 175, 80));
                SwingUtilities.invokeLater(() -> new MainMenuGUI(user.getUserId()).setVisible(true));
                dispose();
            } else {
                messageLabel.setText("Invalid credentials");
                messageLabel.setForeground(new Color(244, 67, 54));
            }
        } catch (Exception e) {
            messageLabel.setText("Login failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
