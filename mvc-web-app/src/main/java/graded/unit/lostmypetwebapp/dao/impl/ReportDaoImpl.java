package graded.unit.lostmypetwebapp.dao.impl;

import graded.unit.lostmypetwebapp.client.WebClient;
import graded.unit.lostmypetwebapp.dao.ReportDao;
import graded.unit.lostmypetwebapp.model.reports.Report;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Implementation of {@link ReportDao} interface
 *
 * @author Piotr Przechodzki
 * @since 20/05/2018
 */
@Repository
public class ReportDaoImpl implements ReportDao {

    private final String REPORT_SERVICE_URL = "/reports/";

    private final WebClient client;

    public ReportDaoImpl(WebClient client) {
        this.client = client;
    }

    @Override
    public List<Report> fetchAllReports() {
        return client.getRestTemplate()
                .exchange(
                        REPORT_SERVICE_URL,
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Report>>() {
                        }).getBody();
    }

    @Override
    public void insertReport(Report report) {
        this.client.getRestTemplate()
                .exchange(REPORT_SERVICE_URL, HttpMethod.POST, new HttpEntity<>(report), Report.class);

    }

    @Override
    public void removeReport(Long id) {
        this.client.getRestTemplate()
                .exchange(REPORT_SERVICE_URL + id, HttpMethod.DELETE, null, String.class);
    }
}
