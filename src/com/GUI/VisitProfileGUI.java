package com.GUI;

import com.DAO.UserDAO;
import com.Entities.User;
import com.Utilities.GUIUtils;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * VisitProfileGUI displays the profile details of a user.
 * It fetches user data from the database and presents it in a styled panel.
 */
public class VisitProfileGUI extends BaseGUI {

    /**
     * Constructs the VisitProfileGUI and initializes the profile UI.
     *
     * @param userID the ID of the user whose profile is to be displayed.
     */
    public VisitProfileGUI(int userID) {
        super(userID, "User Profile", false);

        // Create the profile details panel and add it to the frame.
        add(createProfileDetailsPanel(userID), BorderLayout.CENTER);

        // Center the frame.
        setLocationRelativeTo(null);
    }

    /**
     * Creates and returns the profile details panel.
     *
     * @param userID the ID of the user.
     * @return a JPanel containing the user's profile details.
     */
    private JPanel createProfileDetailsPanel(int userID) {
        JPanel profilePanel = new JPanel(new GridBagLayout());
        profilePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        profilePanel.setBackground(new Color(240, 240, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fetch user details from the database.
        User user = null;
        try {
            user = UserDAO.getUserByID(userID);
        } catch (Exception e) {
            setErrorMessage("Error fetching user details: " + e.getMessage());
            e.printStackTrace();
            return profilePanel;
        }

        // If user is null, show an error message.
        if (user == null) {
            setErrorMessage("User not found.");
            return profilePanel;
        }

        // User Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        profilePanel.add(GUIUtils.createStyledLabel("Name:", true), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        profilePanel.add(createStyledValueLabel(user.getName()), gbc);

        // User Email
        gbc.gridx = 0;
        gbc.gridy = 1;
        profilePanel.add(GUIUtils.createStyledLabel("Email:", true), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        profilePanel.add(createStyledValueLabel(user.getEmail()), gbc);

        return profilePanel;
    }

    /**
     * Creates a styled JLabel to display user profile values.
     *
     * @param text the text to display.
     * @return a JLabel with the specified text styled in Arial, plain, size 16.
     */
    private JLabel createStyledValueLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        return label;
    }
}