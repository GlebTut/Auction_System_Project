package com.GUI;

import com.DAO.PaymentDAO;
import com.Entities.Payment;
import com.Utilities.GUIUtils;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 * MakePaymentGUI provides an interface for users to view and process their pending payments.
 * It displays a table of pending payments for the logged-in user and allows the user to mark a payment as completed.
 */
public class MakePaymentGUI extends BaseGUI {

    private JTable paymentTable;
    private DefaultTableModel tableModel;

    /**
     * Constructs a MakePaymentGUI for the specified user.
     *
     * @param userID the ID of the current user.
     */
    public MakePaymentGUI(int userID) {
        super(userID, "Make Payment", false);

        // Create the payment table panel
        add(createPaymentTablePanel(), BorderLayout.CENTER);
        
        // Create the payment button panel
        add(createButtonPanel(), BorderLayout.SOUTH);

        // Load payment data
        loadPaymentData(userID);

        // Center the frame
        setLocationRelativeTo(null);
    }

    /**
     * Creates and returns the panel containing the payment table.
     *
     * @return the JPanel with the payment table.
     */
    private JPanel createPaymentTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(240, 240, 240));

        // Create the payment table
        String[] columnNames = {"Payment ID", "Amount (€)", "Auction ID", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        
        paymentTable = new JTable(tableModel);
        paymentTable.setRowHeight(30); // Set row height
        paymentTable.setFont(new Font("Arial", Font.PLAIN, 14)); // Set font
        paymentTable.setBackground(Color.WHITE); // Set background color
        paymentTable.setGridColor(new Color(200, 200, 200)); // Set grid color

        // Style the table header
        JTableHeader tableHeader = paymentTable.getTableHeader();
        tableHeader.setFont(new Font("Arial", Font.BOLD, 16)); // Set header font
        tableHeader.setBackground(new Color(70, 130, 180)); // Set header background color
        tableHeader.setForeground(Color.WHITE); // Set header text color

        // Center align table cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < paymentTable.getColumnCount(); i++) {
            paymentTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(paymentTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    /**
     * Creates and returns the panel with the payment processing button.
     *
     * @return the JPanel containing the "Make Payment" button.
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(240, 240, 240));
        
        JButton btnPay = GUIUtils.createStyledButton("Make Payment");
        btnPay.addActionListener(e -> onMakePaymentAction());
        buttonPanel.add(btnPay);
        
        return buttonPanel;
    }

    /**
     * Event handler for the make payment action.
     * Processes the selected payment.
     */
    private void onMakePaymentAction() {
        processPayment();
    }

    /**
     * Loads payment data for the specified user into the payment table.
     *
     * @param userID the ID of the current user.
     */
    private void loadPaymentData(int userID) {
        try {
            List<Payment> payments = PaymentDAO.getAllPayments(); // Fetch all payments
            tableModel.setRowCount(0); // Clear existing rows

            for (Payment payment : payments) {
                if (payment.getBuyerID() == userID && payment.getPaymentStatus().equalsIgnoreCase("PENDING")) {
                    tableModel.addRow(new Object[]{
                        payment.getPaymentID(),
                        String.format("%.2f", payment.getPaymentAmount()),
                        payment.getAuctionID(),
                        payment.getPaymentStatus()
                    });
                }
            }

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No pending payments found.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            setErrorMessage("Error loading payment data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Processes the payment selected by the user.
     * Updates the payment status to "COMPLETED" and refreshes the table.
     */
    private void processPayment() {
        int selectedRow = paymentTable.getSelectedRow();
        if (selectedRow == -1) {
            setErrorMessage("Please select a payment to proceed.");
            return;
        }

        int paymentID = (int) tableModel.getValueAt(selectedRow, 0);
        try {
            Payment payment = PaymentDAO.getPaymentByID(paymentID);
            if (payment != null) {
                payment.setPaymentStatus("COMPLETED");
                PaymentDAO.updatePayment(payment); // Update payment status in the database
                JOptionPane.showMessageDialog(this, "Payment completed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadPaymentData(payment.getBuyerID()); // Refresh the table
                clearErrorMessage();
            } else {
                setErrorMessage("Payment not found.");
            }
        } catch (Exception e) {
            setErrorMessage("Error processing payment: " + e.getMessage());
            e.printStackTrace();
        }
    }
}