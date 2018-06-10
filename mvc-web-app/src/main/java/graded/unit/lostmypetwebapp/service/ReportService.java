package graded.unit.lostmypetwebapp.service;

import graded.unit.lostmypetwebapp.dao.ReportDao;
import graded.unit.lostmypetwebapp.model.reports.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer which manages the data of the {@link Report} domain object
 *
 * @author Piotr Przechodzki
 * @since 20/05/2018
 */
@Service
public class ReportService {

    private final ReportDao reportDao;

    @Autowired
    public ReportService(ReportDao reportDao) {
        this.reportDao = reportDao;
    }

    /**
     * Fetch a list of reports.
     *
     * @return List of all {@link Report} objects.
     */
    public List<Report> getAllReports() {
        return reportDao.fetchAllReports();
    }

    /**
     * Insert a new report.
     *
     * @param report This is the user {@link Report} object that will be saved.
     */
    public void saveReport(Report report) {
        this.reportDao.insertReport(report);
    }

    /**
     * Delete existing report by id.
     *
     * @param id This is an id number of the {@link Report} object to be delete.
     */
    public void deleteReport(Long id) {
        this.reportDao.removeReport(id);
    }
}
