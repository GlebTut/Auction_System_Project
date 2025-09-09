package com.DAO;

import com.Database.DBConnector;
import com.Entities.Report;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for performing CRUD operations on Report records.
 */
public class ReportDAO {

    /**
     * Creates a new report record in the database.
     *
     * <p>This method inserts a new record into the Report table with details such as report type,
     * report content, and the associated user ID.</p>
     *
     * @param report the Report object containing report details
     * @throws SQLException if a database access error occurs during insertion
     */
    public static void createReport(Report report) throws SQLException {
        int i = 0;
        Connection con = null;
        PreparedStatement pstat = null;
        String sql = "INSERT INTO Report (reportType, reportContent, userID) VALUES (?, ?, ?)";
        
        try {
            // Establish connection to the DB
            con = DBConnector.getConnection();
            // Create a PreparedStatement for inserting data into the table
            pstat = con.prepareStatement(sql);
            
            pstat.setString(1, report.getType());
            pstat.setString(2, report.getContent());
            pstat.setInt(3, report.getUserId());
            
            i = pstat.executeUpdate();
            System.out.println(i + " Report successfully added to the table");
        } catch (Exception e) {
            System.err.println("Error creating report: " + e.getMessage());
            e.printStackTrace();
        } finally {
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
     * Updates an existing report record in the database.
     *
     * <p>This method updates the Report table with a new report type and report content for the 
     * specified reportID.</p>
     *
     * @param report the Report object containing updated report details
     * @throws SQLException if a database access error occurs during the update
     */
    public static void updateReport(Report report) throws SQLException {
        int i = 0;
        Connection con = null;
        PreparedStatement pstat = null;
        String sql = "UPDATE Report SET type = ?, content = ? WHERE reportid = ?";
        
        try {
            // Establish connection to the DB
            con = DBConnector.getConnection();
            // Create a PreparedStatement for updating data in the table
            pstat = con.prepareStatement(sql);
            
            pstat.setString(1, report.getType());
            pstat.setString(2, report.getContent());
            pstat.setInt(3, report.getReportID());
            
            i = pstat.executeUpdate();
            System.out.println(i + " Report successfully updated in the table.");
        } catch (Exception e) {
            System.err.println("Error updating report: " + e.getMessage());
            e.printStackTrace();
        } finally {
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
     * Retrieves all report records from the database.
     *
     * <p>This method executes a SQL query to fetch all records from the Report table and returns them
     * as a list of Report objects.</p>
     *
     * @return a List of Report objects containing details of all reports
     * @throws SQLException if a database access error occurs during the query
     */
    public static List<Report> getAllReports() throws SQLException {
        List<Report> reports = new ArrayList<>();
        String sql = "SELECT * FROM Report";
        PreparedStatement pstat = null;
        Connection con = null;
        ResultSet rs = null;
        
        try {
            // Create connection to the DB
            con = DBConnector.getConnection();
            // Prepare statement for retrieving data
            pstat = con.prepareStatement(sql);
            // Execute query
            rs = pstat.executeQuery();
            
            while (rs.next()) {
                int reportId = rs.getInt("reportID");
                String reportType = rs.getString("reportType");
                String reportContent = rs.getString("reportContent");
                int userId = rs.getInt("userID");
    
                Report report = new Report(reportId, reportType, reportContent, userId);
                reports.add(report);
            }
        } catch (SQLException sqlException) {
            System.err.println("Error retrieving report: " + sqlException.getMessage());
            sqlException.printStackTrace();
        } finally {
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
        return reports;
    }

    /**
     * Deletes a report record from the database by its ID.
     *
     * <p>This method removes the report from the Report table using the provided reportID.</p>
     *
     * @param reportID the unique identifier of the report to be deleted
     * @throws SQLException if a database access error occurs during deletion
     */
    public static void deleteReport(int reportID) throws SQLException {
        int i = 0;
        String sql = "DELETE FROM Report WHERE reportid = ?";
        Connection con = null;
        PreparedStatement pstat = null;
        
        try {
            con = DBConnector.getConnection();
            pstat = con.prepareStatement(sql);
            pstat.setInt(1, reportID);
            
            i = pstat.executeUpdate();
            System.out.println(i + " Report successfully deleted from the table.");
        } catch (SQLException sqle) {
            System.err.println("Error deleting report: " + sqle.getMessage());
            sqle.printStackTrace();
        } finally {
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