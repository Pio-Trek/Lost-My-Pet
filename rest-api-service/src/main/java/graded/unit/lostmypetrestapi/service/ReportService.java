package graded.unit.lostmypetrestapi.service;

import graded.unit.lostmypetrestapi.entity.reports.Report;
import graded.unit.lostmypetrestapi.exception.CustomResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

/**
 * Service layer which manages the data of the {@link Report} domain object
 *
 * @author Piotr Przechodzki
 * @since 20/05/2018
 */
public interface ReportService {

    /**
     * Fetch a list of reports from the database
     *
     * @return List of all {@link Report} objects.
     */
    List<Report> getAllReports();

    /**
     * Fetch a single report by id.
     *
     * @param id This is an id number of the {@link Report} object to be fetched.
     * @return Optional value of {@link Report} object
     */
    Optional<Report> getReportById(Long id);

    /**
     * Insert a new report.
     *
     * @param report This is the user {@link Report} object to be inserted.
     * @return HTTP response with {@link Report} announcement object
     */
    ResponseEntity<CustomResponse> addReport(Report report);

    /**
     * Delete existing report by id.
     *
     * @param id This is an id number of the {@link Report} object to be delete.
     * @return HTTP response with confirmation message
     */
    ResponseEntity<CustomResponse> deleteReport(Long id);
}
