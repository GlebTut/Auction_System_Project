package com.GUI;

import com.DAO.AdminDAO;
import com.DAO.AuctionDAO;
import com.DAO.ItemDAO;
import com.DAO.ReportDAO;
import com.DAO.UserDAO;
import com.Entities.Auction;
import com.Entities.Item;
import com.Entities.Report;
import com.Entities.User;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * AdminDashboard is the main GUI frame for administrators.
 * It provides tabs for reports, user management, and auction management.
 * Key event handling methods and GUI setup are modularized for clarity.
 */
public class AdminDashboard extends JFrame {

    private JTabbedPane tabbedPane;

    // Components for the Reports tab.
    private JTable reportTable;
    private DefaultTableModel reportTableModel;

    // Components for the User Management tab.
    private JTable userTable;
    private DefaultTableModel userTableModel;
    private JButton btnDeleteUser;

    // Components for the Auction Management tab.
    private JTable auctionTable;
    private DefaultTableModel auctionTableModel;

    private List<User> loadedUsers = new ArrayList<>();

    /**
     * Constructs an AdminDashboard and initializes the UI.
     */
    public AdminDashboard() {
        initializeUI();
    }

    /**
     * Initializes the GUI: sets up frame properties and adds the tabbed panels.
     */
    private void initializeUI() {
        setTitle("Admin Dashboard - Auction System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Reports", createReportsPanel());
        tabbedPane.addTab("User Management", createUserManagementPanel());
        tabbedPane.addTab("Auction", createAuctionManagementPanel());

        add(tabbedPane, BorderLayout.CENTER);
        getContentPane().setBackground(new Color(245, 245, 245));
    }

    /**
     * Creates and returns the Reports panel.
     *
     * @return the Reports tab JPanel containing the report table.
     */
    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        reportTableModel = new DefaultTableModel(new Object[]{"Report ID", "Type", "Content", "User ID"}, 0);
        reportTable = new JTable(reportTableModel);
        reportTable.setBackground(new Color(224, 255, 255));
        reportTable.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(new JScrollPane(reportTable), BorderLayout.CENTER);

        loadReports();

        return panel;
    }

    /**
     * Creates and returns the User Management panel.
     *
     * @return the User Management tab JPanel containing the user table and delete button.
     */
    private JPanel createUserManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 255, 240));

        userTableModel = new DefaultTableModel(new Object[]{"User ID", "Name", "Email"}, 0);
        userTable = new JTable(userTableModel);
        userTable.setBackground(new Color(255, 239, 213));
        userTable.setFont(new Font("SansSerif", Font.PLAIN, 13));

        // Load users from the database.
        loadUsers();
        panel.add(new JScrollPane(userTable), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(new Color(240, 255, 240));

        btnDeleteUser = new JButton("Delete User");
        btnDeleteUser.setBackground(new Color(255, 160, 122));
        btnDeleteUser.setForeground(Color.BLACK);
        btnDeleteUser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onDeleteUserAction();
            }
        });

        btnPanel.add(btnDeleteUser);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Creates and returns the Auction Management panel.
     *
     * @return the Auction Management tab JPanel containing the auction table and refresh functionality.
     */
    private JPanel createAuctionManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        auctionTableModel = new DefaultTableModel(
            new Object[]{"Auction ID", "Start Time", "End Time", "Current Bid", "Status"}, 0);
        auctionTable = new JTable(auctionTableModel);
        auctionTable.setBackground(new Color(240, 248, 255));
        auctionTable.setFont(new Font("Arial", Font.PLAIN, 14));

        loadAuctions(auctionTableModel);
        panel.add(new JScrollPane(auctionTable), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnRefresh = new JButton("Refresh Auctions");
        btnRefresh.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                onRefreshAuctionsAction();
            }
        });
        btnPanel.add(btnRefresh);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Event handler for deleting the selected user.
     * Prompts confirmation, deletes the user from the database, and updates the UI accordingly.
     */
    private void onDeleteUserAction() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.");
            return;
        }
        // Get selected user from the list.
        User selectedUser = loadedUsers.get(selectedRow);
        int userId = selectedUser.getUserId();

        // Confirmation Dialog.
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the user with ID: " + userId + "?",
            "Confirm", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            System.out.println("UserDAO.deleteUser executed for user ID: " + userId);

            // Delete linked admin account if any.
            AdminDAO.deleteAdmin(userId);

            // Delete user record.
            UserDAO.deleteUser(userId);

            // Remove from table and list.
            userTableModel.removeRow(selectedRow);
            loadedUsers.remove(selectedRow);
            System.out.println("User removed from the table and loadedUsers list.");
        } catch (Exception e) {
            System.out.println("Error deleting user: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error deleting user: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Event handler for refreshing auctions.
     * Reloads auction data from the database into the auction table.
     */
    private void onRefreshAuctionsAction() {
        loadAuctions(auctionTableModel);
    }

    /**
     * Retrieves reports from the database and populates the report table.
     */
    private void loadReports() {
        try {
            List<Report> reports = ReportDAO.getAllReports();
            reportTableModel.setRowCount(0);
            for (Report report : reports) {
                reportTableModel.addRow(new Object[]{
                    report.getReportID(),
                    report.getType(),
                    report.getContent(),
                    report.getUserId()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading reports: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Retrieves users from the database and populates the user table.
     */
    private void loadUsers() {
        try {
            loadedUsers = UserDAO.getAllUsers();
            userTableModel.setRowCount(0);  // Clear any existing rows.
            for (User user : loadedUsers) {
                userTableModel.addRow(new Object[]{user.getUserId(), user.getName(), user.getEmail()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading users: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Loads auctions from the database and populates the specified table model.
     *
     * @param model the DefaultTableModel to be populated with auction data.
     */
    private void loadAuctions(DefaultTableModel model) {
        // Clear existing rows.
        model.setRowCount(0);
        try {
            // Retrieve auctions.
            List<Auction> auctions = AuctionDAO.getAllAuctions();
            for (Auction auction : auctions) {
                // Retrieve related item (if needed for further processing).
                Item item = ItemDAO.getItemByID(auction.getItemID());
                model.addRow(new Object[]{
                    auction.getAuctionID(),
                    auction.getAuctionStartTime().toString(),
                    auction.getAuctionEndTime().toString(),
                    auction.getAuctionCurrentHighestBid(),
                    auction.getAuctionStatus()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading auctions: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Main method to launch the Admin Dashboard GUI.
     *
     * @param args command-line arguments (not used).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new AdminDashboard().setVisible(true);
            }
        });
    }
}
