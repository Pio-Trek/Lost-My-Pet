package graded.unit.lostmypetrestapi.controller;

import graded.unit.lostmypetrestapi.entity.reports.Report;
import graded.unit.lostmypetrestapi.exception.CustomResponse;
import graded.unit.lostmypetrestapi.service.ReportService;
import javassist.NotFoundException;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiPathParam;
import org.jsondoc.core.pojo.ApiStage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller layer class for {@link Report} which exposes resources,
 * CRUD operations and business logic for the {@link ReportService}.
 *
 * @author Piotr Przechodzki
 * @since 20/05/2018
 */
@RestController
@RequestMapping("api/reports")
@Api(
        name = "Report System API",
        description = "Provides a list of methods that manage announcements member reports.",
        stage = ApiStage.GA)
public class ReportController {

    // URI address for testing purpose
    static String URI = "/api/reports/";

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * HTTP GET request method.
     * Fetch all members reports from the database.
     *
     * @return List of all {@link Report} objects.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Fetch all members reports.")
    public List<Report> fetchReports() {
        return reportService.getAllReports();
    }

    /**
     * HTTP POST request method.
     * Save a new members' report to the database.
     *
     * @param report This is a new object of the {@link Report} to be save.
     * @return HTTP response with {@link Report} object.
     */
    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Save a new members' report.")
    public ResponseEntity<Report> insertNotification(@RequestBody @Validated Report report) {
        this.reportService.addReport(report);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * HTTP DELETE request method.
     * Delete from the database a single report.
     *
     * @param id This is an id number of the {@link Report} object to be delete/
     * @return HTTP response with confirmation message.
     * @throws NotFoundException When {@link Report#id} is not valid.
     */
    @DeleteMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Delete a single report.")
    public ResponseEntity<CustomResponse> removeReport(@ApiPathParam(description = "The id of the report") @PathVariable("id") Long id) throws NotFoundException {
        validateReportId(id);
        return reportService.deleteReport(id);
    }

    /**
     * Method responsible for check if report ID number exists in the database
     *
     * @param id This is an id number of the users {@link Report} to be validate
     * @throws NotFoundException When {@link Report#id} is not valid.
     */
    private void validateReportId(Long id) throws NotFoundException {
        reportService.getReportById(id)
                .orElseThrow(() -> new NotFoundException
                        ("Report with ID: '" + id + "' not found."));
    }
}
