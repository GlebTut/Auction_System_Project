package com.DAO;

import com.Database.DBConnector;
import com.Entities.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for performing CRUD operations on User records.
 */
public class UserDAO {

    /**
     * Creates a new user record in the database.
     *
     * <p>This method inserts a new record into the user table with details such as userName,
     * userEmail, and userpassword. It retrieves and returns the generated userID.</p>
     *
     * @param user the User object containing user details
     * @return the generated userID, or -1 if creation fails
     * @throws SQLException if a database access error occurs during insertion
     */
    public static int createUser(User user) throws SQLException {
        int userID = -1;
        String sql = "INSERT INTO user(userName, userEmail, userpassword) VALUES (?, ?, ?)";
        Connection con = null;
        PreparedStatement pstat = null;
        ResultSet generatedKeys = null;
        
        try {
            con = DBConnector.getConnection();
            pstat = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstat.setString(1, user.getName());
            pstat.setString(2, user.getEmail());
            pstat.setString(3, user.getPassword());
            
            int i = pstat.executeUpdate();
            System.out.println(i + " User successfully added to the table");
            
            generatedKeys = pstat.getGeneratedKeys();
            if (generatedKeys.next()) {
                userID = generatedKeys.getInt(1);
            }
        } catch(Exception e) {
            System.err.println("Error creating user: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (generatedKeys != null) generatedKeys.close();
            if (pstat != null) pstat.close();
            if (con != null) con.close();
        }
        return userID;
    }

    /**
     * Updates an existing user record in the database.
     *
     * <p>This method updates the user table with new values for userName, userEmail, and userpassword
     * based on the provided userID.</p>
     *
     * @param user the User object containing updated user details
     * @throws SQLException if a database access error occurs during the update
     */
    public static void updateUser(User user) throws SQLException {
        int i = 0;
        String sql = "UPDATE User set name = ?, email = ?, password = ? WHERE userid = ?";

        Connection con = null;
        PreparedStatement pstat = null;
        
        try {
            // Establish connection to the DB
            con = DBConnector.getConnection();
            // Create a PreparedStatement for updating data in the table
            pstat = con.prepareStatement(sql);
            pstat.setString(1, user.getName());
            pstat.setString(2, user.getEmail());
            pstat.setString(3, user.getPassword());
            pstat.setInt(4, user.getUserId());  // primary key for identifying record

            i = pstat.executeUpdate();
            System.out.println(i + " User successfully updated in the table.");
        } catch (Exception e) {
            System.err.println("Error updating user: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (pstat != null) pstat.close();
            if (con != null) con.close();
        }
    }

    /**
     * Retrieves all user records from the database.
     *
     * <p>This method executes a SQL query to fetch all records from the user table and returns
     * them as a list of User objects.</p>
     *
     * @return a List of User objects containing details of all users
     * @throws SQLException if a database access error occurs during retrieval
     */
    public static List<User> getAllUsers() throws SQLException {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM user";
        Connection con = null;
        PreparedStatement pstat = null;
        ResultSet rs = null;
        
        try {
            con = DBConnector.getConnection();
            pstat = con.prepareStatement(sql);
            rs = pstat.executeQuery();

            while (rs.next()) {
                int userId = rs.getInt("userID");
                String name = rs.getString("userName");
                String email = rs.getString("userEmail");
                String password = rs.getString("userpassword");
                User user = new User(userId, name, email, password);
                userList.add(user);
            }
        } catch (SQLException sqlException) {
            System.err.println("Error retrieving user: " + sqlException.getMessage());
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
        return userList;
    }

    /**
     * Deletes a user record from the database by its ID.
     *
     * <p>This method removes the record from the user table identified by the given userID.</p>
     *
     * @param userId the unique identifier of the user to be deleted
     * @throws SQLException if a database access error occurs during deletion
     */
    public static void deleteUser(int userId) throws SQLException {
        int i = 0;
        String sql = "DELETE FROM User WHERE userID = ?";
        Connection con = null;
        PreparedStatement pstat = null;

        try {
            con = DBConnector.getConnection();
            pstat = con.prepareStatement(sql);
            pstat.setInt(1, userId);

            i = pstat.executeUpdate();
            System.out.println(i + " user successfully deleted from the table.");
        } catch (SQLException sqle) {
            System.err.println("Error deleting user: " + sqle.getMessage());
            sqle.printStackTrace();
        } finally {
            if (pstat != null) pstat.close();
            if (con != null) con.close();
        }
    }

    /**
     * Retrieves a user record by its email.
     *
     * <p>This method executes a SQL query to fetch a user record using the provided email
     * and returns the corresponding User object if found.</p>
     *
     * @param email the email address of the user to be retrieved
     * @return the User object if found; null otherwise
     * @throws SQLException if a database access error occurs during retrieval
     */
    public static User getUserByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM user WHERE userEmail = ?";
        Connection con = null;
        PreparedStatement pstat = null;
        ResultSet rs = null;
        User user = null;

        try {
            con = DBConnector.getConnection();
            pstat = con.prepareStatement(sql);
            pstat.setString(1, email);
            rs = pstat.executeQuery();

            if (rs.next()) {
                user = new User(
                    rs.getInt("userID"),
                    rs.getString("userName"),
                    rs.getString("userEmail"),
                    rs.getString("userpassword")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving user by email: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstat != null) pstat.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return user;
    }

    /**
     * Retrieves a user record by its ID.
     *
     * <p>This method executes a SQL query to fetch a user record using the provided userID and returns
     * a User object populated with the corresponding details. This method expects only one matching user.</p>
     *
     * @param userId the unique identifier of the user to be retrieved
     * @return the User object if found; null otherwise
     * @throws SQLException if a database access error occurs during retrieval
     */
    public static User getUserByID(int userId) throws SQLException {
        String sql = "SELECT * FROM user WHERE UserID = ?";
        Connection con = null;
        PreparedStatement pstat = null;
        ResultSet rs = null;
        User user = null;

        try {
            con = DBConnector.getConnection();
            pstat = con.prepareStatement(sql);
            pstat.setInt(1, userId);
            rs = pstat.executeQuery();

            // Use if instead of while since only one user is expected
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("UserID"));
                user.setName(rs.getString("userName"));
                user.setEmail(rs.getString("userEmail"));
                user.setPassword(rs.getString("userPassword"));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving user by ID: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstat != null) pstat.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return user;
    }
}