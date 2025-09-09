package com.DAO;

import com.Database.DBConnector;
import com.Entities.Bid;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for performing CRUD operations on Bid records.
 */
public class BidDAO {

    /**
     * Creates a new bid record in the database.
     *
     * <p>This method inserts a new bid record with details such as bidAmount, bidTime,
     * auctionID, and buyerID into the bid table.</p>
     *
     * @param bid the Bid object containing bid details
     * @throws SQLException if a database access error occurs during insertion
     */
    public static void createBid(Bid bid) throws SQLException {
        Connection con = null;
        PreparedStatement pstat = null;
        String sql = "INSERT INTO bid (bidAmount, bidTime, auctionID, buyerID) VALUES (?, ?, ?, ?)";
        
        try {
            con = DBConnector.getConnection();
            pstat = con.prepareStatement(sql);
            
            pstat.setDouble(1, bid.getBidAmount());
            pstat.setTimestamp(2, Timestamp.valueOf(bid.getBidTime()));
            pstat.setInt(3, bid.getAuctionID());
            pstat.setInt(4, bid.getBuyerID());
            
            int i = pstat.executeUpdate();
            System.out.println(i + " Bid successfully added to the table");
        } catch (Exception e) {
            System.err.println("Error creating bid: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (pstat != null) pstat.close();
                // if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }
    
    /**
     * Updates an existing bid record in the database.
     *
     * <p>This method updates bid details such as bidAmount, bidTime, auctionID, and buyerID
     * for the specified bidID.</p>
     *
     * @param bid the Bid object containing updated bid details
     * @throws SQLException if a database access error occurs during the update
     */
    public static void updateBid(Bid bid) throws SQLException {
        Connection con = null;
        PreparedStatement pstat = null;
        String sql = "UPDATE bid SET bidAmount = ?, bidTime = ?, auctionID = ?, buyerID = ? WHERE bidID = ?";
        
        try {
            con = DBConnector.getConnection();
            pstat = con.prepareStatement(sql);
            
            pstat.setDouble(1, bid.getBidAmount());
            pstat.setTimestamp(2, Timestamp.valueOf(bid.getBidTime()));
            pstat.setInt(3, bid.getAuctionID());
            pstat.setInt(4, bid.getBuyerID());
            pstat.setInt(5, bid.getBidID());
            
            int i = pstat.executeUpdate();
            System.out.println(i + " Bid successfully updated in the table.");
        } catch (Exception e) {
            System.err.println("Error updating bid: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (pstat != null) pstat.close();
                // if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }
    
    /**
     * Retrieves a bid record by its ID.
     *
     * <p>This method executes a SQL query to fetch a bid record using the provided bidID,
     * and returns the corresponding Bid object if found.</p>
     *
     * @param bidID the unique identifier of the bid
     * @return the Bid object if found; null otherwise
     * @throws SQLException if a database access error occurs during the query
     */
    public static Bid getBidByID(int bidID) throws SQLException {
        Bid bid = null;
        Connection con = null;
        PreparedStatement pstat = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM bid WHERE bidID = ?";
        
        try {
            con = DBConnector.getConnection();
            pstat = con.prepareStatement(sql);
            pstat.setInt(1, bidID);
            rs = pstat.executeQuery();
            
            if (rs.next()) {
                bid = new Bid();
                bid.setBidID(rs.getInt("bidID"));
                bid.setBidAmount(rs.getDouble("bidAmount"));
                bid.setBidTime(rs.getTimestamp("bidTime").toLocalDateTime());
                bid.setAuctionID(rs.getInt("auctionID"));
                bid.setBuyerID(rs.getInt("buyerID"));
            }
        } catch (Exception e) {
            System.err.println("Error retrieving bid: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstat != null) pstat.close();
                // if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return bid;
    }
    
    /**
     * Deletes a bid record from the database by its ID.
     *
     * @param bidID the unique identifier of the bid to be deleted
     * @throws SQLException if a database access error occurs during deletion
     */
    public static void deleteBid(int bidID) throws SQLException {
        Connection con = null;
        PreparedStatement pstat = null;
        String sql = "DELETE FROM bid WHERE bidID = ?";
        
        try {
            con = DBConnector.getConnection();
            pstat = con.prepareStatement(sql);
            pstat.setInt(1, bidID);
            int i = pstat.executeUpdate();
            System.out.println(i + " Bid successfully deleted from the table.");
        } catch (Exception e) {
            System.err.println("Error deleting bid: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (pstat != null) pstat.close();
                // if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }
    
    /**
     * Retrieves all bid records from the database.
     *
     * <p>This method executes a SQL query to retrieve all bid records from the bid table,
     * and returns them as a list.</p>
     *
     * @return a List of Bid objects containing details of all bids
     * @throws SQLException if a database access error occurs during the query
     */
    public List<Bid> findAll() throws SQLException {
        Connection con = null;
        PreparedStatement pstat = null;
        ResultSet rs = null;
        List<Bid> bids = new ArrayList<>();
        String sql = "SELECT * FROM bid";
        
        try {
            con = DBConnector.getConnection();
            pstat = con.prepareStatement(sql);
            rs = pstat.executeQuery();
            
            while (rs.next()) {
                Bid bid = new Bid();
                bid.setBidID(rs.getInt("bidID"));
                bid.setBidAmount(rs.getDouble("bidAmount"));
                bid.setBidTime(rs.getTimestamp("bidTime").toLocalDateTime());
                bid.setAuctionID(rs.getInt("auctionID"));
                bid.setBuyerID(rs.getInt("buyerID"));
                bids.add(bid);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving bids: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstat != null) pstat.close();
                // if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return bids;
    }
    
    /**
     * Retrieves all bid records associated with a specific auction.
     *
     * <p>This method executes a SQL query to fetch all bids associated with the specified auctionID,
     * and returns them as a list.</p>
     *
     * @param auctionID the ID of the auction for which bids are to be retrieved
     * @return a List of Bid objects for the given auctionID
     * @throws SQLException if a database access error occurs during the query
     */
    public static List<Bid> getAllBidsByAuctionID(int auctionID) throws SQLException {
        Connection con = null;
        PreparedStatement pstat = null;
        ResultSet rs = null;
        List<Bid> bids = new ArrayList<>();
        String sql = "SELECT * FROM bid WHERE auctionID = ?";
        
        try {
            con = DBConnector.getConnection();
            pstat = con.prepareStatement(sql);
            pstat.setInt(1, auctionID);
            rs = pstat.executeQuery();
            
            while (rs.next()) {
                Bid bid = new Bid();
                bid.setBidID(rs.getInt("bidID"));
                bid.setBidAmount(rs.getDouble("bidAmount"));
                bid.setBidTime(rs.getTimestamp("bidTime").toLocalDateTime());
                bid.setAuctionID(rs.getInt("auctionID"));
                bid.setBuyerID(rs.getInt("buyerID"));
                bids.add(bid);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving bids for auction: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstat != null) pstat.close();
                // if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return bids;
    }
}
