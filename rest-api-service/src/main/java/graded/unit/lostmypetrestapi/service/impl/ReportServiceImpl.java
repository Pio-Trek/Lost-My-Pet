package graded.unit.lostmypetrestapi.service.impl;

import graded.unit.lostmypetrestapi.entity.reports.Report;
import graded.unit.lostmypetrestapi.exception.CustomResponse;
import graded.unit.lostmypetrestapi.repository.ReportRepository;
import graded.unit.lostmypetrestapi.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link ReportService} interface
 *
 * @author Piotr Przechodzki
 * @since 20/05/2018
 */
@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository repository;

    @Autowired
    public ReportServiceImpl(ReportRepository repository) {
        this.repository = repository;
    }


    @Override
    public List<Report> getAllReports() {
        return repository.findAll();
    }

    public Optional<Report> getReportById(Long id) {
        return repository.findById(id);
    }

    @Override
    public ResponseEntity<CustomResponse> addReport(Report report) {
        report.setSendDate(new Date());
        this.repository.save(report);
        return new ResponseEntity<>(
                new CustomResponse(
                        new Date(),
                        HttpStatus.OK.value(),
                        "Report sent by user with ID: " + report.getUser().getId() + " added."), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CustomResponse> deleteReport(Long id) {
        this.repository.deleteById(id);
        return new ResponseEntity<>(
                new CustomResponse(new Date(), HttpStatus.OK.value(),
                        "Report deleted. (ID: " + id + ")"), HttpStatus.OK);
    }
}
