package com.Services;

import com.DAO.ReportDAO;
import com.Entities.Report;
import java.sql.SQLException;

/**
 * Provides operations related to report management.
 */
public class ReportService {

    /**
     * Creates a new report.
     *
     * <p>Ensures that the report type is not null or empty, then sends the creation to ReportDAO.</p>
     *
     * @param report the Report object to be created.
     * @throws IllegalArgumentException if the report type is null or empty.
     * @throws SQLException if an error occurs during report creation.
     */
    public static void createReport(Report report) throws SQLException {
        // Ensure that report type is not null or empty 
        if (report.getType() == null || report.getType().isEmpty()) {
            throw new IllegalArgumentException("Report type can't be empty.");
        }
        ReportDAO.createReport(report);
    }
}
