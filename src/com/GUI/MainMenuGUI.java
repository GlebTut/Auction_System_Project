package com.GUI;

import com.DAO.AuctionDAO;
import com.DAO.ItemDAO;
import com.Entities.Auction;
import com.Entities.Item;
import com.Utilities.GUIUtils;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 * MainMenuGUI displays the main auction interface for the Auction System.
 * It periodically refreshes the auction table, updates the current date and time,
 * and allows users to navigate to detailed auction monitoring.
 */
public class MainMenuGUI extends JFrame {

    private JLabel noAuctionsLabel;
    private JLabel dateTimeLabel;
    private Timer refreshTimer = null;
    private Timer dateTimeTimer = null;
    private final DefaultTableModel tableModel;
    private final Map<Integer, ImageIcon> imageCache = new ConcurrentHashMap<>();
    private Set<Integer> currentAuctionIds = new HashSet<>();
    private final int userID;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Constructs the MainMenuGUI with the specified user ID.
     *
     * @param userID the unique identifier of the current user.
     */
    public MainMenuGUI(int userID) {
        this.userID = userID;
        setTitle("Auction System - Main Page");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 240, 240));

        setupHeader();
        setupMenuBar();

        // Create table model with non-editable cells.
        String[] columnNames = {"Picture", "Item Name", "Starting Price", "Current Bid", "Start Time", "Finish Time", "Status", "Auction ID"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? ImageIcon.class : String.class;
            }
        };

        JTable auctionTable = setupAuctionTable();
        JScrollPane scrollPane = new JScrollPane(auctionTable);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        // "No auctions" message label.
        noAuctionsLabel = new JLabel("No auctions available at the moment.", SwingConstants.CENTER);
        noAuctionsLabel.setForeground(Color.RED);
        noAuctionsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(noAuctionsLabel, BorderLayout.SOUTH);

        // Initial population of auction table.
        boolean hasAuctions = populateAuctionTable();
        noAuctionsLabel.setVisible(!hasAuctions);

        startRefreshTimer();
        setLocationRelativeTo(null); // Center the frame
        
        // Add window listener for cleanup.
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                cleanup();
            }
        });
    }

    /**
     * Sets up the header panel which includes the title and current date/time.
     */
    private void setupHeader() {
        JPanel headerPanel = GUIUtils.createHeaderPanel("Auction System");
        dateTimeLabel = new JLabel();
        dateTimeLabel.setForeground(Color.WHITE);
        dateTimeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        dateTimeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dateTimeLabel.setText(new SimpleDateFormat("HH:mm:ss | dd-MM-yyyy").format(new Date()));
        headerPanel.add(dateTimeLabel);
        add(headerPanel, BorderLayout.NORTH);
        startDateTimeUpdater();
    }

    /**
     * Sets up the menu bar with navigation options.
     */
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(70, 130, 180));
        menuBar.setBorder(new EmptyBorder(5, 5, 5, 5));

        JMenu profileMenu = GUIUtils.createMenu("Profile", Color.WHITE, 16);
        JMenu auctionsMenu = GUIUtils.createMenu("Create Auction", Color.WHITE, 16);
        JMenu reportMenu = GUIUtils.createMenu("Report", Color.WHITE, 16);
        JMenu paymentMenu = GUIUtils.createMenu("Payments", Color.WHITE, 16);

        profileMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                navigateTo(VisitProfileGUI.class);
            }
        });
        auctionsMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                navigateTo(CreateAuctionGUI.class);
            }
        });
        reportMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                navigateTo(CreateReportGUI.class);
            }
        });
        paymentMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                navigateTo(MakePaymentGUI.class);
            }
        });

        menuBar.add(profileMenu);
        menuBar.add(auctionsMenu);
        menuBar.add(reportMenu);
        menuBar.add(paymentMenu);
        setJMenuBar(menuBar);
    }

    /**
     * Configures and returns the auction JTable.
     *
     * @return the configured JTable.
     */
    private JTable setupAuctionTable() {
        JTable auctionTable = new JTable(tableModel);
        auctionTable.setRowHeight(100);
        auctionTable.setFont(new Font("Arial", Font.PLAIN, 14));
        auctionTable.setBackground(Color.WHITE);
        auctionTable.setGridColor(new Color(200, 200, 200));

        JTableHeader tableHeader = auctionTable.getTableHeader();
        tableHeader.setFont(new Font("Arial", Font.BOLD, 16));
        tableHeader.setBackground(new Color(70, 130, 180));
        tableHeader.setForeground(Color.WHITE);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 1; i < auctionTable.getColumnCount(); i++) {
            auctionTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Hide the Auction ID column.
        auctionTable.getColumnModel().getColumn(7).setMinWidth(0);
        auctionTable.getColumnModel().getColumn(7).setMaxWidth(0);
        auctionTable.getColumnModel().getColumn(7).setWidth(0);

        auctionTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int selectedRow = auctionTable.getSelectedRow();
                if (selectedRow != -1) {
                    int auctionID = (int) tableModel.getValueAt(selectedRow, 7);
                    openMonitorAuctionGUI(auctionID);
                }
            }
        });

        return auctionTable;
    }

    /**
     * Starts the date/time updater timer to refresh the dateTimeLabel every second.
     */
    private void startDateTimeUpdater() {
        if (this.dateTimeTimer != null) {
            this.dateTimeTimer.stop();
        }
        this.dateTimeTimer = new Timer(1000, e -> SwingUtilities.invokeLater(() -> {
            String currentDateTime = new SimpleDateFormat("HH:mm:ss | dd-MM-yyyy").format(new Date());
            dateTimeLabel.setText(currentDateTime);
        }));
        this.dateTimeTimer.setInitialDelay(0);
        this.dateTimeTimer.start();
    }

    /**
     * Starts the refresh timer to update the auction table periodically.
     */
    private void startRefreshTimer() {
        if (this.refreshTimer != null) {
            this.refreshTimer.stop();
        }
        this.refreshTimer = new Timer(5000, e -> {
            boolean hasAuctions = populateAuctionTable();
            noAuctionsLabel.setVisible(!hasAuctions);
        });
        this.refreshTimer.setInitialDelay(0);
        this.refreshTimer.start();
    }

    /**
     * Populates the auction table by retrieving auctions, updating statuses as needed,
     * and syncing the table with the latest auction IDs.
     *
     * @return true if there are auctions; false otherwise.
     */
    private boolean populateAuctionTable() {
        Set<Integer> newAuctionIds = new HashSet<>();
        boolean hasAuctions = false;

        try {
            List<Auction> auctions = AuctionDAO.getAllStartedAuctions();
            hasAuctions = !auctions.isEmpty();

            // First pass: update auctions and collect IDs.
            for (Auction auction : auctions) {
                int auctionID = auction.getAuctionID();
                newAuctionIds.add(auctionID);

                // Check if auction should be finished.
                if (auction.getAuctionEndTime().isBefore(LocalDateTime.now()) ||
                    auction.getAuctionEndTime().isEqual(LocalDateTime.now())) {
                    if (!auction.getAuctionStatus().equalsIgnoreCase("FINISHED")) {
                        auction.setAuctionStatus("FINISHED");
                        AuctionDAO.updateAuction(auction);
                    }
                }
            }

            // Determine auctions to remove and add.
            Set<Integer> auctionsToRemove = new HashSet<>(currentAuctionIds);
            auctionsToRemove.removeAll(newAuctionIds);
            Set<Integer> auctionsToAdd = new HashSet<>(newAuctionIds);
            auctionsToAdd.removeAll(currentAuctionIds);
            Set<Integer> auctionsToUpdate = new HashSet<>(newAuctionIds);
            auctionsToUpdate.retainAll(currentAuctionIds);

            // Remove rows for auctions that no longer exist.
            for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
                int rowAuctionId = (int) tableModel.getValueAt(i, 7);
                if (auctionsToRemove.contains(rowAuctionId)) {
                    tableModel.removeRow(i);
                }
            }

            // Process auctions for adding/updating.
            for (Auction auction : auctions) {
                int auctionID = auction.getAuctionID();
                if (!auctionsToAdd.contains(auctionID) && !auctionsToUpdate.contains(auctionID)) {
                    continue;
                }
                Item item = ItemDAO.getItemByID(auction.getItemID());
                if (item == null) continue;

                ImageIcon imageIcon = getItemImage(item);
                String itemName = item.getItemName();
                String startingPrice = String.format("%.2f", item.getItemStartingPrice());
                String currentBid = String.format("%.2f", auction.getAuctionCurrentHighestBid());
                String startTime = auction.getAuctionStartTime().format(formatter);
                String finishTime = auction.getAuctionEndTime().format(formatter);
                String status = auction.getAuctionStatus();

                boolean rowFound = false;
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    if ((int) tableModel.getValueAt(i, 7) == auctionID) {
                        updateTableRow(i, imageIcon, itemName, startingPrice, currentBid, startTime, finishTime, status, auctionID);
                        rowFound = true;
                        break;
                    }
                }
                if (!rowFound) {
                    tableModel.addRow(new Object[]{
                        imageIcon, itemName, startingPrice, currentBid,
                        startTime, finishTime, status, auctionID
                    });
                }
            }
            currentAuctionIds = newAuctionIds;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading auctions: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return hasAuctions;
    }

    /**
     * Updates the specified row in the auction table with new data.
     *
     * @param rowIndex      the index of the row to update.
     * @param imageIcon     the image icon for the item.
     * @param itemName      the name of the item.
     * @param startingPrice the starting price.
     * @param currentBid    the current highest bid.
     * @param startTime     the auction start time.
     * @param finishTime    the auction finish time.
     * @param status        the auction status.
     * @param auctionID     the auction identifier.
     */
    private void updateTableRow(int rowIndex, ImageIcon imageIcon, String itemName,
                                String startingPrice, String currentBid, String startTime,
                                String finishTime, String status, int auctionID) {
        tableModel.setValueAt(imageIcon, rowIndex, 0);
        tableModel.setValueAt(itemName, rowIndex, 1);
        tableModel.setValueAt(startingPrice, rowIndex, 2);
        tableModel.setValueAt(currentBid, rowIndex, 3);
        tableModel.setValueAt(startTime, rowIndex, 4);
        tableModel.setValueAt(finishTime, rowIndex, 5);
        tableModel.setValueAt(status, rowIndex, 6);
        tableModel.setValueAt(auctionID, rowIndex, 7);
    }

    /**
     * Retrieves the item's image and caches it for performance.
     *
     * @param item the Item containing image data.
     * @return a scaled ImageIcon for display.
     */
    private ImageIcon getItemImage(Item item) {
        int itemID = item.getItemID();
        if (imageCache.containsKey(itemID)) {
            return imageCache.get(itemID);
        }
        ImageIcon imageIcon;
        if (item.getItemImage() != null) {
            ImageIcon originalIcon = new ImageIcon(item.getItemImage());
            Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            imageIcon = new ImageIcon(scaledImage);
        } else {
            imageIcon = new ImageIcon(new byte[0]);
        }
        imageCache.put(itemID, imageIcon);
        return imageIcon;
    }

    /**
     * Navigates to the specified destination screen using reflection.
     *
     * @param destinationClass the screen class to navigate to.
     * @param <T>              a type extending JFrame.
     */
    private <T extends JFrame> void navigateTo(Class<T> destinationClass) {
        SwingUtilities.invokeLater(() -> {
            try {
                T destination = destinationClass.getConstructor(int.class).newInstance(userID);
                destination.setVisible(true);
                cleanup();
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error navigating to " + destinationClass.getSimpleName() + ": " + ex.getMessage(),
                        "Navigation Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
    }

    /**
     * Opens the MonitorAuctionGUI for the specified auction.
     *
     * @param auctionID the auction identifier.
     */
    private void openMonitorAuctionGUI(int auctionID) {
        SwingUtilities.invokeLater(() -> {
            MonitorAuctionGUI monitorAuctionGUI = new MonitorAuctionGUI(auctionID, userID);
            monitorAuctionGUI.setVisible(true);
            cleanup();
            dispose();
        });
    }

    /**
     * Cleans up resources, stopping timers and clearing cached images.
     */
    private void cleanup() {
        if (this.refreshTimer != null) {
            this.refreshTimer.stop();
            this.refreshTimer = null;
        }
        if (this.dateTimeTimer != null) {
            this.dateTimeTimer.stop();
            this.dateTimeTimer = null;
        }
        imageCache.clear();
    }
}