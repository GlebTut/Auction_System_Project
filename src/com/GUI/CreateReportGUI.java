package com.GUI;

import com.DAO.ReportDAO;
import com.Entities.Report;
import com.Utilities.GUIUtils;
import java.awt.*;
import javax.swing.*;

/**
 * CreateReportGUI provides a user interface for creating and submitting reports.
 * It handles form validation and submission logic.
 */
public class CreateReportGUI extends BaseGUI {
    // GUI components
    private JTextField txtReportTitle;
    private JTextArea txtReportDescription;
    private JButton btnSubmitReport;

    /**
     * Constructs a CreateReportGUI.
     *
     * @param userID the ID of the current user.
     */
    public CreateReportGUI(int userID) {
        super(userID, "Create Report", false);
        add(createReportFormPanel(), BorderLayout.CENTER);
        setLocationRelativeTo(null); // Center the frame
    }

    /**
     * Creates the report form panel containing the input fields and submit button.
     *
     * @return the JPanel containing the report form.
     */
    private JPanel createReportFormPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));

        // Form Panel setup
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(new Color(240, 240, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Report Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(GUIUtils.createStyledLabel("Report Title:", true), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        txtReportTitle = new JTextField(25);
        txtReportTitle.setFont(new Font("Arial", Font.PLAIN, 16));
        formPanel.add(txtReportTitle, gbc);

        // Report Description
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(GUIUtils.createStyledLabel("Report Description:", true), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        txtReportDescription = new JTextArea(6, 25);
        txtReportDescription.setFont(new Font("Arial", Font.PLAIN, 16));
        txtReportDescription.setLineWrap(true);
        txtReportDescription.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(txtReportDescription);
        formPanel.add(scrollPane, gbc);

        panel.add(formPanel, BorderLayout.CENTER);

        // Submit Button Panel setup
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(240, 240, 240));

        btnSubmitReport = GUIUtils.createStyledButton("Submit Report");
        btnSubmitReport.addActionListener(e -> onSubmitReportAction());
        buttonPanel.add(btnSubmitReport);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Event handler for the submit report action.
     * Validates report details and, if valid, submits the report.
     */
    private void onSubmitReportAction() {
        if (validateReportDetails()) {
            submitReport();
        }
    }

    /**
     * Validates the report details entered by the user.
     *
     * @return true if the report title and description are not empty; false otherwise.
     */
    private boolean validateReportDetails() {
        String reportTitle = txtReportTitle.getText().trim();
        String reportDescription = txtReportDescription.getText().trim();

        if (reportTitle.isEmpty()) {
            setErrorMessage("Report Title cannot be empty.");
            return false;
        }

        if (reportDescription.isEmpty()) {
            setErrorMessage("Report Description cannot be empty.");
            return false;
        }

        clearErrorMessage();
        return true;
    }

    /**
     * Submits the report details to the database.
     * Creates a new Report object, saves it via ReportDAO, and navigates to the main menu.
     */
    private void submitReport() {
        try {
            String reportTitle = txtReportTitle.getText().trim();
            String reportDescription = txtReportDescription.getText().trim();

            Report report = new Report(0, reportTitle, reportDescription, getUserID());
            ReportDAO.createReport(report);

            JOptionPane.showMessageDialog(this, "Report submitted successfully!");
            navigateToMainMenu();
        } catch (Exception ex) {
            setErrorMessage("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}