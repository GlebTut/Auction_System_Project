package com.DAO;

import com.Database.DBConnector;
import com.Entities.Auction;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for performing CRUD operations on Auction records.
 */
public class AuctionDAO {

    /**
     * Creates a new auction record in the database.
     *
     * <p>This method inserts a new auction record with details such as the start time,
     * end time, current highest bid, status, item ID, seller ID, and buyer ID.</p>
     *
     * @param auction the Auction object containing auction details
     * @throws SQLException if a database access error occurs during insertion
     */
    public static void createAuction(Auction auction) throws SQLException {
        Connection con = null;
        PreparedStatement pstat = null;
        String sql = "INSERT INTO auction (auctionStartTime, auctionEndTime, auctionCurrentHighestBid, auctionStatus, itemID, sellerID, buyerID) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            con = DBConnector.getConnection();
            pstat = con.prepareStatement(sql);

            pstat.setTimestamp(1, Timestamp.valueOf(auction.getAuctionStartTime()));
            pstat.setTimestamp(2, Timestamp.valueOf(auction.getAuctionEndTime()));
            pstat.setDouble(3, auction.getAuctionCurrentHighestBid());
            pstat.setString(4, auction.getAuctionStatus());
            pstat.setInt(5, auction.getItemID());
            pstat.setInt(6, auction.getSellerID());
            pstat.setInt(7, auction.getBuyerID());

            int i = pstat.executeUpdate();
            System.out.println(i + " Auction successfully added to the table");
        } catch (Exception e) {
            System.err.println("Error creating auction: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (pstat != null) pstat.close();
                //if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    /**
     * Updates an existing auction record in the database.
     *
     * <p>This method updates an auction record based on the Auction object's auctionID
     * with the new details provided.</p>
     *
     * @param auction the Auction object containing updated auction details
     * @throws SQLException if a database access error occurs during the update
     */
    public static void updateAuction(Auction auction) throws SQLException {
        Connection con = null;
        PreparedStatement pstat = null;
        String sql = "UPDATE auction SET auctionStartTime = ?, auctionEndTime = ?, auctionCurrentHighestBid = ?, auctionStatus = ?, itemID = ?, sellerID = ?, buyerID = ? WHERE auctionID = ?";

        try {
            con = DBConnector.getConnection();
            pstat = con.prepareStatement(sql);

            pstat.setTimestamp(1, Timestamp.valueOf(auction.getAuctionStartTime()));
            pstat.setTimestamp(2, Timestamp.valueOf(auction.getAuctionEndTime()));
            pstat.setDouble(3, auction.getAuctionCurrentHighestBid());
            pstat.setString(4, auction.getAuctionStatus());
            pstat.setInt(5, auction.getItemID());
            pstat.setInt(6, auction.getSellerID());
            pstat.setInt(7, auction.getBuyerID());
            pstat.setInt(8, auction.getAuctionID());

            int i = pstat.executeUpdate();
            System.out.println(i + " Auction successfully updated in the table.");
        } catch (Exception e) {
            System.err.println("Error updating auction: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (pstat != null) pstat.close();
                //if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    /**
     * Retrieves an auction record by its ID.
     *
     * <p>This method executes a SQL query to fetch an auction record using the specified auctionID.</p>
     *
     * @param auctionID the unique identifier of the auction
     * @return an Auction object populated with auction details, or null if not found
     * @throws SQLException if a database access error occurs during the query
     */
    public static Auction getAuctionByID(int auctionID) throws SQLException {
        Auction auction = null;
        Connection con = null;
        PreparedStatement pstat = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM auction WHERE auctionID = ?";

        try {
            con = DBConnector.getConnection();
            pstat = con.prepareStatement(sql);
            pstat.setInt(1, auctionID);

            rs = pstat.executeQuery();
            while (rs.next()) {
                auction = new Auction();
                auction.setAuctionID(rs.getInt("auctionID"));
                auction.setAuctionStartTime(rs.getTimestamp("auctionStartTime").toLocalDateTime());
                auction.setAuctionEndTime(rs.getTimestamp("auctionEndTime").toLocalDateTime());
                auction.setAuctionCurrentHighestBid(rs.getDouble("auctionCurrentHighestBid"));
                auction.setAuctionStatus(rs.getString("auctionStatus"));
                auction.setItemID(rs.getInt("itemID"));
                auction.setSellerID(rs.getInt("sellerID"));
                auction.setBuyerID(rs.getInt("buyerID"));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving auction: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstat != null) pstat.close();
                //if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return auction;
    }

    /**
     * Deletes an auction record from the database by its ID.
     *
     * @param auctionID the unique identifier of the auction to be deleted
     * @throws SQLException if a database access error occurs during deletion
     */
    public static void deleteAuction(int auctionID) throws SQLException {
        Connection con = null;
        PreparedStatement pstat = null;
        String sql = "DELETE FROM auction WHERE auctionID = ?";

        try {
            con = DBConnector.getConnection();
            pstat = con.prepareStatement(sql);
            pstat.setInt(1, auctionID);

            int i = pstat.executeUpdate();
            System.out.println(i + " Auction successfully deleted from the table.");
        } catch (SQLException e) {
            System.err.println("Error deleting auction: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (pstat != null) pstat.close();
                //if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    /**
     * Retrieves all auction records from the database.
     *
     * <p>This method executes a SQL query to retrieve every auction record and returns them as a list.</p>
     *
     * @return a List of Auction objects containing details of all auctions
     * @throws SQLException if a database access error occurs during the query
     */
    public static List<Auction> getAllAuctions() throws SQLException {
        Connection con = null;
        PreparedStatement pstat = null;
        ResultSet rs = null;
        List<Auction> auctions = new ArrayList<>();
        String sql = "SELECT * FROM auction";

        try {
            con = DBConnector.getConnection();
            pstat = con.prepareStatement(sql);
            rs = pstat.executeQuery();

            while (rs.next()) {
                Auction auction = new Auction();
                auction.setAuctionID(rs.getInt("auctionID"));
                auction.setAuctionStartTime(rs.getTimestamp("auctionStartTime").toLocalDateTime());
                auction.setAuctionEndTime(rs.getTimestamp("auctionEndTime").toLocalDateTime());
                auction.setAuctionCurrentHighestBid(rs.getDouble("auctionCurrentHighestBid"));
                auction.setAuctionStatus(rs.getString("auctionStatus"));
                auction.setItemID(rs.getInt("itemID"));
                auction.setSellerID(rs.getInt("sellerID"));
                auction.setBuyerID(rs.getInt("buyerID"));
                auctions.add(auction);
            }
        } catch (Exception e) {
            System.err.println("Error retrieving all auctions: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstat != null) pstat.close();
                //if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return auctions;
    }

    /**
     * Retrieves all auctions with status "STARTED" from the database.
     *
     * <p>This method executes a SQL query to fetch auctions that have a status of "STARTED".</p>
     *
     * @return a List of Auction objects representing all started auctions
     * @throws SQLException if a database access error occurs during the query
     */
    public static List<Auction> getAllStartedAuctions() throws SQLException {
        Connection con = null;
        PreparedStatement pstat = null;
        ResultSet rs = null;
        List<Auction> auctions = new ArrayList<>();
        String sql = "SELECT * FROM auction WHERE auctionStatus = 'STARTED'";

        try {
            con = DBConnector.getConnection();
            pstat = con.prepareStatement(sql);
            rs = pstat.executeQuery();

            while (rs.next()) {
                Auction auction = new Auction();
                auction.setAuctionID(rs.getInt("auctionID"));
                auction.setAuctionStartTime(rs.getTimestamp("auctionStartTime").toLocalDateTime());
                auction.setAuctionEndTime(rs.getTimestamp("auctionEndTime").toLocalDateTime());
                auction.setAuctionCurrentHighestBid(rs.getDouble("auctionCurrentHighestBid"));
                auction.setAuctionStatus(rs.getString("auctionStatus"));
                auction.setItemID(rs.getInt("itemID"));
                auction.setSellerID(rs.getInt("sellerID"));
                auction.setBuyerID(rs.getInt("buyerID"));
                auctions.add(auction);
            }
        } catch (Exception e) {
            System.err.println("Error retrieving all auctions: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstat != null) pstat.close();
                //if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return auctions;
    }

    /**
     * Retrieves all auctions that are finished (status not equal to "STARTED") from the database.
     *
     * <p>This method executes a SQL query to fetch auctions that have finished.</p>
     *
     * @return a List of Auction objects representing all finished auctions
     * @throws SQLException if a database access error occurs during the query
     */
    public static List<Auction> getAllFinishedAuctions() throws SQLException {
        Connection con = null;
        PreparedStatement pstat = null;
        ResultSet rs = null;
        List<Auction> auctions = new ArrayList<>();
        String sql = "SELECT * FROM auction WHERE auctionStatus != 'STARTED'";

        try {
            con = DBConnector.getConnection();
            pstat = con.prepareStatement(sql);
            rs = pstat.executeQuery();

            while (rs.next()) {
                Auction auction = new Auction();
                auction.setAuctionID(rs.getInt("auctionID"));
                auction.setAuctionStartTime(rs.getTimestamp("auctionStartTime").toLocalDateTime());
                auction.setAuctionEndTime(rs.getTimestamp("auctionEndTime").toLocalDateTime());
                auction.setAuctionCurrentHighestBid(rs.getDouble("auctionCurrentHighestBid"));
                auction.setAuctionStatus(rs.getString("auctionStatus"));
                auction.setItemID(rs.getInt("itemID"));
                auction.setSellerID(rs.getInt("sellerID"));
                auction.setBuyerID(rs.getInt("buyerID"));
                auctions.add(auction);
            }
        } catch (Exception e) {
            System.err.println("Error retrieving all auctions: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstat != null) pstat.close();
                //if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return auctions;
    }
}
