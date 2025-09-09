package com.GUI;

import com.DAO.AuctionDAO;
import com.DAO.BidDAO;
import com.DAO.PaymentDAO;
import com.Entities.Auction;
import com.Entities.Bid;
import com.Entities.Item;
import com.Entities.Payment;
import com.Utilities.GUIUtils;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 * MonitorAuctionGUI displays detailed information about a running auction.
 * It shows item details, the current highest bid, bid history,
 * and provides functionality to place bids. It also updates auction details
 * and processes payments once the auction ends.
 */
public class MonitorAuctionGUI extends BaseGUI {
    private JLabel lblImage, lblName, lblDescription, lblHighestBid, lblAuctionStatus;
    private JTable bidHistoryTable;
    private JTextField txtBidAmount;
    private JButton btnPlaceBid;
    private Timer updateTimer;
    private Auction auction;
    private Item item;
    private JPanel bidPanel;

    /**
     * Constructs a MonitorAuctionGUI for the given auction and user.
     *
     * @param auctionID the ID of the auction to be monitored.
     * @param userID    the ID of the current user.
     */
    public MonitorAuctionGUI(int auctionID, int userID) {
        super(userID, "Monitor Auction", false);
        
        try {
            // Fetch auction and item details.
            auction = AuctionDAO.getAuctionByID(auctionID);
            item = com.DAO.ItemDAO.getItemByID(auction.getItemID());
            
            // Set up the main panels.
            setupAuctionDetails();
            
            // Start the update timer.
            startUpdateTimer();
            
            // Center the frame.
            setLocationRelativeTo(null);
        } catch (Exception e) {
            setErrorMessage("Error loading auction details: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Sets up all auction detail panels including item details, bid history, and the bid entry panel.
     */
    private void setupAuctionDetails() {
        // Auction Details Panel (Left Side).
        add(createDetailsPanel(), BorderLayout.WEST);
        
        // Bid History Panel (Center).
        add(createBidHistoryPanel(), BorderLayout.CENTER);
        
        // Bid Entry Panel (Bottom).
        bidPanel = createBidPanel();
        add(bidPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Creates and returns the panel displaying item and auction details.
     *
     * @return the details JPanel.
     */
    private JPanel createDetailsPanel() {
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        detailsPanel.setBackground(new Color(240, 240, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Section 1: Item Details.
        JPanel itemDetailsPanel = new JPanel(new GridBagLayout());
        itemDetailsPanel.setBorder(BorderFactory.createTitledBorder("Item Details"));
        itemDetailsPanel.setBackground(new Color(240, 240, 240));

        // Item Image.
        lblImage = new JLabel();
        if (item.getItemImage() != null) {
            lblImage.setIcon(new ImageIcon(new ImageIcon(item.getItemImage()).getImage()
                    .getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
        } else {
            lblImage.setText("No Image Available");
            lblImage.setHorizontalAlignment(SwingConstants.CENTER);
        }
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        itemDetailsPanel.add(lblImage, gbc);

        // Item Name.
        lblName = new JLabel("Name: " + item.getItemName());
        lblName.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        itemDetailsPanel.add(lblName, gbc);

        // Item Description.
        lblDescription = new JLabel("<html>Description: " + item.getItemDescription() + "</html>");
        lblDescription.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 1;
        itemDetailsPanel.add(lblDescription, gbc);

        // Starting Price.
        JLabel lblStartingPrice = new JLabel("Starting Price: €" + item.getItemStartingPrice());
        lblStartingPrice.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 1;
        gbc.gridy = 2;
        itemDetailsPanel.add(lblStartingPrice, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        detailsPanel.add(itemDetailsPanel, gbc);

        // Section 2: Auction Details.
        JPanel auctionDetailsPanel = new JPanel(new GridBagLayout());
        auctionDetailsPanel.setBorder(BorderFactory.createTitledBorder("Auction Details"));
        auctionDetailsPanel.setBackground(new Color(240, 240, 240));

        // Highest Bid.
        lblHighestBid = new JLabel("Highest Bid: €" + auction.getAuctionCurrentHighestBid());
        lblHighestBid.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        auctionDetailsPanel.add(lblHighestBid, gbc);

        // Auction Status.
        lblAuctionStatus = new JLabel("Status: " + auction.getAuctionStatus());
        lblAuctionStatus.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        auctionDetailsPanel.add(lblAuctionStatus, gbc);

        // Auction End Time.
        JLabel lblEndTime = new JLabel("End Time: " + auction.getAuctionEndTime()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        lblEndTime.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        auctionDetailsPanel.add(lblEndTime, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        detailsPanel.add(auctionDetailsPanel, gbc);

        return detailsPanel;
    }
    
    /**
     * Creates and returns the bid history panel with a table of past bids.
     *
     * @return the JScrollPane wrapping the bid history table.
     */
    private JScrollPane createBidHistoryPanel() {
        String[] columnNames = {"Bid Amount", "User ID", "Bid Time"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        bidHistoryTable = new JTable(tableModel);
        bidHistoryTable.setFont(new Font("Arial", Font.PLAIN, 14));
        bidHistoryTable.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(bidHistoryTable);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        loadBidHistory();
        return scrollPane;
    }
    
    /**
     * Creates and returns the panel used to place a bid.
     *
     * @return the bid panel.
     */
    private JPanel createBidPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(240, 240, 240));

        txtBidAmount = new JTextField(10);
        txtBidAmount.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(new JLabel("Your Bid:"));
        panel.add(txtBidAmount);

        btnPlaceBid = GUIUtils.createStyledButton("Place Bid");
        btnPlaceBid.addActionListener(e -> placeBid(getUserID()));
        panel.add(btnPlaceBid);

        return panel;
    }
    
    /**
     * Loads the bid history from the database into the bid history table.
     */
    private void loadBidHistory() {
        try {
            DefaultTableModel tableModel = (DefaultTableModel) bidHistoryTable.getModel();
            tableModel.setRowCount(0); // Clear existing rows
            
            List<Bid> bids = BidDAO.getAllBidsByAuctionID(auction.getAuctionID());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            
            for (Bid bid : bids) {
                tableModel.addRow(new Object[]{
                    bid.getBidAmount(), 
                    bid.getBuyerID(), 
                    bid.getBidTime().format(formatter)
                });
            }
        } catch (Exception ex) {
            setErrorMessage("Error loading bid history: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Starts a timer to periodically update auction details, bid history,
     * and to check whether the auction has ended.
     */
    private void startUpdateTimer() {
        final boolean[] messageShown = {false}; // To ensure message is shown only once
        
        updateTimer = new Timer(1000, e -> {
            try {
                // Refresh auction details.
                auction = AuctionDAO.getAuctionByID(auction.getAuctionID());
                lblHighestBid.setText("Highest Bid: €" + auction.getAuctionCurrentHighestBid());
                lblAuctionStatus.setText("Status: " + auction.getAuctionStatus());
                
                // Refresh bid history.
                loadBidHistory();
                
                // Check if auction end time has passed.
                LocalDateTime currentTime = LocalDateTime.now();
                if (currentTime.isAfter(auction.getAuctionEndTime()) &&
                    !auction.getAuctionStatus().equalsIgnoreCase("FINISHED") &&
                    !auction.getAuctionStatus().equalsIgnoreCase("CLOSED")) {
                    
                    auction.setAuctionStatus("FINISHED");
                    AuctionDAO.updateAuction(auction);
                    lblAuctionStatus.setText("Status: " + auction.getAuctionStatus());
                }
                
                // If auction is closed/finished: notify user, process payment and remove bid panel.
                if ((auction.getAuctionStatus().equalsIgnoreCase("CLOSED") || 
                     auction.getAuctionStatus().equalsIgnoreCase("FINISHED")) && !messageShown[0]) {
                    
                    updateTimer.stop();
                    JOptionPane.showMessageDialog(this, "Auction has ended. Payment will be processed.", 
                        "Auction Closed", JOptionPane.INFORMATION_MESSAGE);
                    messageShown[0] = true;
                    removeBidPanel();
                    processPayment();
                }
            } catch (Exception ex) {
                setErrorMessage("Error updating auction details: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
        updateTimer.start();
    }

    /**
     * Attempts to place a bid for the given user.
     *
     * @param userID the ID of the user placing the bid.
     */
    private void placeBid(int userID) {
        try {
            double bidAmount = Double.parseDouble(txtBidAmount.getText().trim());
            if (bidAmount <= auction.getAuctionCurrentHighestBid()) {
                setErrorMessage("Your bid must be higher than the current highest bid.");
                return;
            } else if (bidAmount < item.getItemStartingPrice()) {
                setErrorMessage("Your bid must be higher than the starting price.");
                return;
            }

            // Create and save the bid.
            Bid bid = new Bid();
            bid.setBidAmount(bidAmount);
            bid.setBidTime(LocalDateTime.now());
            bid.setAuctionID(auction.getAuctionID());
            bid.setBuyerID(userID);
            BidDAO.createBid(bid);

            // Update auction details and UI.
            auction.setAuctionCurrentHighestBid(bidAmount);
            auction.setBuyerID(userID);
            AuctionDAO.updateAuction(auction);
            JOptionPane.showMessageDialog(this, "Bid placed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            txtBidAmount.setText("");
            clearErrorMessage();
        } catch (NumberFormatException ex) {
            setErrorMessage("Please enter a valid bid amount.");
        } catch (Exception ex) {
            setErrorMessage("Error placing bid: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Processes payment for the highest bidder once the auction ends.
     */
    private void processPayment() {
        try {
            if (auction.getBuyerID() == 0) {
                JOptionPane.showMessageDialog(this, "No bids were placed. Auction closed without a winner.", 
                    "No Winner", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Create a payment record.
            Payment payment = new Payment();
            payment.setPaymentAmount(auction.getAuctionCurrentHighestBid());
            payment.setPaymentStatus("PENDING");
            payment.setAuctionID(auction.getAuctionID());
            payment.setBuyerID(auction.getBuyerID());
            payment.setSellerID(auction.getSellerID());
            PaymentDAO.createPayment(payment);

            JOptionPane.showMessageDialog(this, "Payment created for the highest bidder.", 
                "Payment Processed", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            setErrorMessage("Error processing payment: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Removes the bid entry panel from the UI.
     */
    private void removeBidPanel() {
        SwingUtilities.invokeLater(() -> {
            if (bidPanel != null) {
                getContentPane().remove(bidPanel);
                revalidate();
                repaint();
            }
        });
    }
    
    @Override
    protected void cleanup() {
        super.cleanup();
        if (updateTimer != null) {
            updateTimer.stop();
        }
    }
}