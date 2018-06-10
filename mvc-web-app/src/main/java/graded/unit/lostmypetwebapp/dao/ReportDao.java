package graded.unit.lostmypetwebapp.dao;

import graded.unit.lostmypetwebapp.model.reports.Report;

import java.util.List;

/**
 * Data Access Object interface which manages the data of the {@link Report} domain
 * object from the consume Service.
 *
 * @author Piotr Przechodzki
 * @since 20/05/2018
 */
public interface ReportDao {

    /**
     * Fetch a list of reports.
     *
     * @return List of all {@link Report} objects.
     */
    List<Report> fetchAllReports();

    /**
     * Insert a new report.
     *
     * @param report This is the user {@link Report} object that will be saved.
     */
    void insertReport(Report report);

    /**
     * Delete existing report by id.
     *
     * @param id This is an id number of the {@link Report} object to be delete.
     */
    void removeReport(Long id);
}
