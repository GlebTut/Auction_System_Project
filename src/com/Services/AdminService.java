package com.Services;

import com.DAO.AdminDAO;
import com.DAO.UserDAO;
import com.Entities.Admin;
import java.sql.SQLException;

/**
 * AdminService provides business-level operations for admin management,
 * including registration and login functionalities. 
 * It delegates database operations to AdminDAO and UserDAO.
 */
public class AdminService {

    /**
     * Registers a new admin in the system.
     *
     * <p>This method validates that the admin privileges (default admin) are provided,
     * creates a corresponding user record, gets the generated user ID,
     * assigns it to the admin, and registers the admin record.</p>
     *
     * @param admin the Admin object containing admin details.
     * @throws IllegalArgumentException if admin privileges are null or empty.
     * @throws SQLException if creating a user fails or the admin registration encounters an error.
     */
    public static void registerAdmin(Admin admin) throws SQLException {
        if (admin.getAdminPrivileges() == null || admin.getAdminPrivileges().isEmpty()) {
            throw new IllegalArgumentException("Admin privileges can't be empty.");
        }
        int userID = UserDAO.createUser(admin);
        if (userID <= 0) {
            throw new SQLException("User creation failed for admin.");
        }
        admin.setUserId(userID);  // Set the foreign key from the user record
        
        // Register the admin record, which generates a separate admin ID.
        AdminDAO.registerAdmin(admin);
    }

    /**
     * Authenticates an admin using the provided email and password.
     *
     * <p>This method retrieves the admin details from the database using AdminDAO.
     * It compares the given password with the stored password and
     * returns the Admin object if check is successful.</p>
     *
     * @param email the email address of the admin.
     * @param password the password provided for authentication.
     * @return the authenticated Admin object if credentials are valid; null otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public static Admin loginAdmin(String email, String password) throws SQLException {
        Admin admin = AdminDAO.getAdminByEmail(email);
        if (admin != null && admin.getPassword().equals(password)) {
            return admin;
        }
        return null;
    }
}
