package com.DAO;

import com.Database.DBConnector;
import com.Entities.Admin;
import java.sql.*;

/**
 * Data Access Object (DAO) for performing CRUD operations on Admin records.
 */
public class AdminDAO {

    /**
     * Registers a new Admin in the database.
     *
     * <p>This method inserts a new record into the Admin table using the provided Admin object's
     * userID and adminPrivileges. It also retrieves and sets the generated adminID for the Admin.</p>
     *
     * @param admin the Admin object to be registered
     * @throws SQLException if a database access error occurs during the process
     */
    public static void registerAdmin(Admin admin) throws SQLException {
        String sql = "INSERT INTO Admin (userID, adminPrivileges) VALUES (?, ?)";
        Connection con = null;
        PreparedStatement pstat = null;
        
        try {
            con = DBConnector.getConnection();
            pstat = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstat.setInt(1, admin.getUserId());
            pstat.setString(2, admin.getAdminPrivileges());
            
            int i = pstat.executeUpdate();
            System.out.println(i + " Admin successfully added to the table");
            
            // Optionally retrieve the generated adminID if needed:
            try (ResultSet rs = pstat.getGeneratedKeys()) {
                if (rs.next()) {
                    admin.setAdminID(rs.getInt(1));
                }
            }
        } catch (Exception e) {
            System.err.println("Error creating admin: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            if (pstat != null) pstat.close();
            if (con != null) con.close();
        }
    }

    /**
     * Updates an existing Admin's privileges in the database.
     *
     * <p>This method updates the adminPrivileges field for the specified Admin record using its adminID.</p>
     *
     * @param admin the Admin object containing updated information
     * @throws SQLException if a database access error occurs
     */
    public static void updateAdmin(Admin admin) throws SQLException {
        int i = 0;
        String sql = "UPDATE Admin set adminPrivileges= ? WHERE adminID = ?";

        try {
            // Establish connection to the DB
            Connection con = DBConnector.getConnection();

            // Create a Prepared statement for adding data into table
            PreparedStatement pstat = con.prepareStatement(sql);

            // Set parameters: note that adminID should be the WHERE clause value
            pstat.setString(1, admin.getAdminPrivileges());
            pstat.setInt(2, admin.getAdminID());

            i = pstat.executeUpdate();
            System.out.println(i + " Admin successfully updated in the table.");
            
            pstat.close();
            con.close();
        } catch (Exception e) {
            System.err.println("Error updating admin: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Retrieves and prints Admin details based on the specified adminID.
     *
     * <p>This method executes a SELECT query to fetch an Admin's details and prints the column names
     * and corresponding values. Note: this method prints directly to the console instead of returning an Admin object.</p>
     *
     * @param adminID the ID of the Admin to retrieve
     * @throws SQLException if a database access error occurs during the query
     */
    public static void getAdminByID(int adminID) throws SQLException {
        String sql = "SELECT * FROM Admin WHERE adminID = ?";
        PreparedStatement pstat = null;
        Connection con = null;
        ResultSet rs = null;
        
        try {
            // Creates a connection to DB
            con = DBConnector.getConnection();
            // Prepared Statement for retrieving data
            pstat = con.prepareStatement(sql);

            pstat.setInt(1, adminID);

            // Execute query
            rs = pstat.executeQuery();

            // Gets metadata to get column information
            ResultSetMetaData metaData = rs.getMetaData();
            int numOfColumns = metaData.getColumnCount();

            for (int i = 1; i <= numOfColumns; i++) {
                System.out.print(metaData.getColumnName(i) + "\t");
            }
            System.out.println();

            while (rs.next()) {
                for (int i = 1; i <= numOfColumns; i++) {
                    System.out.print(rs.getObject(i) + "\t");
                }
                System.out.println();
            }

        } catch (SQLException sqlException) {
            System.err.println("Error retrieving admin: " + sqlException.getMessage());
            sqlException.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstat != null) pstat.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Deletes an Admin record from the database based on the specified userID.
     *
     * @param userID the user ID associated with the Admin record to be deleted
     * @throws SQLException if a database access error occurs during the deletion
     */
    public static void deleteAdmin(int userID) throws SQLException {
        int i = 0;
        String sql = "DELETE FROM Admin WHERE userID = ?";

        Connection con = null;
        PreparedStatement pstat = null;

        try {
            con = DBConnector.getConnection();
            pstat = con.prepareStatement(sql);
            pstat.setInt(1, userID);

            i = pstat.executeUpdate();
            System.out.println(i + " admin successfully deleted form the table.");

        } catch (SQLException sqle) {
            System.err.println("Error deleting admin: " + sqle.getMessage());
            sqle.printStackTrace();
            throw sqle;
        } finally {
            try {
                if (pstat != null) pstat.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Retrieves an Admin object by matching the provided email.
     *
     * <p>This method performs a JOIN between the Admin and User tables to fetch the admin's details 
     * based on the userEmail. If a matching record is found, it returns an Admin object; otherwise, it returns null.</p>
     *
     * @param email the email of the admin to be retrieved
     * @return an Admin object if found; null otherwise
     * @throws SQLException if a database access error occurs during the query
     */
    public static Admin getAdminByEmail(String email) throws SQLException {
        String sql = "SELECT a.adminID, a.adminPrivileges, u.userID, u.userName, u.userEmail, u.userpassword " +
                     "FROM Admin a JOIN User u ON a.userID = u.userID " +
                     "WHERE u.userEmail = ?";
        try (Connection con = DBConnector.getConnection();
             PreparedStatement pstat = con.prepareStatement(sql)) {
            pstat.setString(1, email);
            try (ResultSet rs = pstat.executeQuery()) {
                if (rs.next()) {
                    int adminID = rs.getInt("adminID");
                    int userID = rs.getInt("userID");
                    String name = rs.getString("userName");
                    String userEmail = rs.getString("userEmail");
                    String pwd = rs.getString("userpassword");
                    String privileges = rs.getString("adminPrivileges");
                    Admin admin = new Admin(adminID, userID, name, userEmail, pwd, privileges);
                    return admin;
                }
            }
        }
        return null;
    }
}