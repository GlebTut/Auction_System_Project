package com.DAO;

import com.Database.DBConnector;
import com.Entities.Payment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for performing CRUD operations on Payment records.
 */
public class PaymentDAO {

    /**
     * Creates a new payment record in the database.
     *
     * <p>This method inserts a new record into the payment table with details such as paymentAmount,
     * paymentStatus, auctionID, buyerID, and sellerID.</p>
     *
     * @param payment the Payment object containing payment details
     * @throws SQLException if a database access error occurs during insertion
     */
    public static void createPayment(Payment payment) throws SQLException {
        int i = 0;
        String sql = "INSERT INTO payment (paymentAmount, paymentStatus, auctionID, buyerID, sellerID) VALUES (?, ?, ?, ?, ?)";
        try {
            Connection con = DBConnector.getConnection();
            PreparedStatement pstat = con.prepareStatement(sql);

            pstat.setDouble(1, payment.getPaymentAmount());
            pstat.setString(2, payment.getPaymentStatus());
            pstat.setInt(3, payment.getAuctionID());
            pstat.setInt(4, payment.getBuyerID());
            pstat.setInt(5, payment.getSellerID());

            i = pstat.executeUpdate();
            System.out.println(i + " Payment successfully added to the table");
        } catch (Exception e) {
            System.err.println("Error creating payment: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing payment record in the database.
     *
     * <p>This method updates the payment table with new details for the specified paymentID.</p>
     *
     * @param payment the Payment object containing updated payment details
     * @throws SQLException if a database access error occurs during the update
     */
    public static void updatePayment(Payment payment) throws SQLException {
        int i = 0;
        String sql = "UPDATE payment SET paymentAmount = ?, paymentStatus = ?, auctionID = ?, buyerID = ?, sellerID = ? WHERE paymentID = ?";
        try {
            Connection con = DBConnector.getConnection();
            PreparedStatement pstat = con.prepareStatement(sql);

            pstat.setDouble(1, payment.getPaymentAmount());
            pstat.setString(2, payment.getPaymentStatus());
            pstat.setInt(3, payment.getAuctionID());
            pstat.setInt(4, payment.getBuyerID());
            pstat.setInt(5, payment.getSellerID());
            pstat.setInt(6, payment.getPaymentID());

            i = pstat.executeUpdate();
            System.out.println(i + " Payment successfully updated in the table.");
        } catch (Exception e) {
            System.err.println("Error updating payment: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a payment record by its ID.
     *
     * <p>This method executes a SQL query to fetch a payment record using the provided paymentID,
     * and returns a Payment object populated with the corresponding details if found.</p>
     *
     * @param paymentID the unique identifier of the payment
     * @return the Payment object if found; null otherwise
     * @throws SQLException if a database access error occurs during the query
     */
    public static Payment getPaymentByID(int paymentID) throws SQLException {
        Payment payment = null;
        String sql = "SELECT * FROM payment WHERE paymentID = ?";
        try {
            Connection con = DBConnector.getConnection();
            PreparedStatement pstat = con.prepareStatement(sql);
            pstat.setInt(1, paymentID);

            ResultSet rs = pstat.executeQuery();
            if (rs.next()) {
                payment = new Payment();
                payment.setPaymentID(rs.getInt("paymentID"));
                payment.setPaymentAmount(rs.getDouble("paymentAmount"));
                payment.setPaymentStatus(rs.getString("paymentStatus"));
                payment.setAuctionID(rs.getInt("auctionID"));
                payment.setBuyerID(rs.getInt("buyerID"));
                payment.setSellerID(rs.getInt("sellerID"));
            }
        } catch (Exception e) {
            System.err.println("Error retrieving payment: " + e.getMessage());
            e.printStackTrace();
        }
        return payment;
    }

    /**
     * Deletes a payment record from the database by its ID.
     *
     * <p>This method removes the payment record from the payment table based on the provided paymentID.</p>
     *
     * @param paymentID the unique identifier of the payment to be deleted
     * @throws SQLException if a database access error occurs during deletion
     */
    public static void deletePayment(int paymentID) throws SQLException {
        int i = 0;
        String sql = "DELETE FROM payment WHERE paymentID = ?";
        Connection con = null;
        PreparedStatement pstat = null;
        try {
            con = DBConnector.getConnection();
            pstat = con.prepareStatement(sql);
            pstat.setInt(1, paymentID);

            i = pstat.executeUpdate();
            System.out.println(i + " Payment successfully deleted from the table.");
        } catch (Exception e) {
            System.err.println("Error deleting payment: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (pstat != null) pstat.close();
            if (con != null) con.close();
        }
    }

    /**
     * Retrieves all payment records from the database.
     *
     * <p>This method executes a SQL query to fetch all records from the payment table and returns
     * them as a list of Payment objects.</p>
     *
     * @return a List of Payment objects containing details of all payments
     * @throws SQLException if a database access error occurs during the query
     */
    public static List<Payment> getAllPayments() throws SQLException {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payment";
        try {
            Connection con = DBConnector.getConnection();
            PreparedStatement pstat = con.prepareStatement(sql);

            ResultSet rs = pstat.executeQuery();
            while (rs.next()) {
                Payment payment = new Payment();
                payment.setPaymentID(rs.getInt("paymentID"));
                payment.setPaymentAmount(rs.getDouble("paymentAmount"));
                payment.setPaymentStatus(rs.getString("paymentStatus"));
                payment.setAuctionID(rs.getInt("auctionID"));
                payment.setBuyerID(rs.getInt("buyerID"));
                payment.setSellerID(rs.getInt("sellerID"));

                payments.add(payment);
            }
        } catch (Exception e) {
            System.err.println("Error retrieving all payments: " + e.getMessage());
            e.printStackTrace();
        }
        return payments;
    }
}
