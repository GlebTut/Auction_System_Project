package com.DAO;

import com.Database.DBConnector;
import com.Entities.Seller;
import java.sql.*;

/**
 * Data Access Object (DAO) for performing CRUD operations on Seller records.
 */
public class SellerDAO {

    /**
     * Creates a new seller record in the database.
     *
     * <p>This method inserts a new record into the Seller table using the Seller object's userId and earnings.</p>
     *
     * @param seller the Seller object containing seller details
     * @throws SQLException if a database access error occurs during insertion
     */
    public static void createSeller(Seller seller) throws SQLException{
        int i = 0;
        Connection con = null;
        PreparedStatement pstat = null;
        String sql = "INSERT INTO Seller (sellerID, earnings) VALUES (?, ?)";

        try {
            // Establish connection to the DB
            con = DBConnector.getConnection();

            // Create a PreparedStatement for adding data into the table
            pstat = con.prepareStatement(sql);

            pstat.setInt(1, seller.getUserId());
            pstat.setDouble(2, seller.getEarnings());

            i = pstat.executeUpdate();
            System.out.println(i + " Seller successfully added to the table");

        } catch (Exception e) {
            System.err.println("Error creating seller: " + e.getMessage());
            e.printStackTrace();
        }
        finally {
            try {
                if (pstat != null)
                    pstat.close();
                if (con != null)
                    con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Updates an existing seller's record in the database.
     *
     * <p>This method updates the Seller table with new earnings for the seller identified by sellerID.</p>
     *
     * @param seller the Seller object containing updated seller details
     * @throws SQLException if a database access error occurs during the update
     */
    public static void updateReport(Seller seller) throws SQLException{
        int i = 0;
        Connection con = null;
        PreparedStatement pstat = null;
        String sql = "UPDATE Seller set earnings = ? WHERE sellerID = ?";

        try {
            // Establish connection to the DB
            con = DBConnector.getConnection();

            // Create a PreparedStatement for updating data in the table
            pstat = con.prepareStatement(sql);

            pstat.setDouble(1, seller.getEarnings());
            pstat.setInt(2, seller.getUserId()); // primary key used to access and identify record

            i = pstat.executeUpdate();
            System.out.println(i + " seller successfully updated in the table.");

        } catch (Exception e) {
            System.err.println("Error updating seller: " + e.getMessage());
            e.printStackTrace();
        }
        finally {
            try {
                if (pstat != null)
                    pstat.close();
                if (con != null)
                    con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Retrieves and prints a seller's record by its ID.
     *
     * <p>This method executes a SQL query to fetch a seller record using the provided sellerID.
     * It prints out the column names and corresponding values to the console.</p>
     *
     * @param sellerID the unique identifier of the seller to be retrieved
     * @throws SQLException if a database access error occurs during retrieval
     */
    public static void getReportByID(int sellerID) throws SQLException {
        String sql = "SELECT * FROM Seller WHERE sellerID = ?";
        PreparedStatement pstat = null;
        Connection con = null;
        ResultSet rs = null;
        
        try {
            // Establish connection to the DB
            con = DBConnector.getConnection();
            // Prepare statement for retrieving data
            pstat = con.prepareStatement(sql);
            pstat.setInt(1, sellerID);

            // Execute query
            rs = pstat.executeQuery();

            // Print column names
            ResultSetMetaData metaData = rs.getMetaData();
            int numOfColumns = metaData.getColumnCount();
            for (int i = 1; i <= numOfColumns; i++) {
                System.out.print(metaData.getColumnName(i) + "\t");
            }
            System.out.println();

            // Print row data
            while (rs.next()) {
                for (int i = 1; i <= numOfColumns; i++) {
                    System.out.print(rs.getObject(i) + "\t");
                }
                System.out.println();
            }

        } catch (SQLException sqlException) {
            System.err.println("Error retrieving seller: " + sqlException.getMessage());
            sqlException.printStackTrace();
        }
        finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstat != null)
                    pstat.close();
                if (con != null)
                    con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Deletes a seller record from the database by its ID.
     *
     * <p>This method removes the seller record from the Seller table using the provided sellerID.</p>
     *
     * @param sellerID the unique identifier of the seller to be deleted
     * @throws SQLException if a database access error occurs during deletion
     */
    public static void deleteReport(int sellerID) throws SQLException{
        int i = 0;
        String sql = "DELETE FROM Seller WHERE sellerID = ?";

        Connection con = null;
        PreparedStatement pstat = null;

        try {
            con = DBConnector.getConnection();
            pstat = con.prepareStatement(sql);
            pstat.setInt(1, sellerID);

            i = pstat.executeUpdate();
            System.out.println(i + " seller successfully deleted from the table.");

        } catch (SQLException sqle) {
            System.err.println("Error deleting seller: " + sqle.getMessage());
            sqle.printStackTrace();
        }
        finally {
            try {
                if (pstat != null)
                    pstat.close();
                if (con != null)
                    con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}