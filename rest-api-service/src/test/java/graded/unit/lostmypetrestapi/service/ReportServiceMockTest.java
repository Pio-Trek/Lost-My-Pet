package graded.unit.lostmypetrestapi.service;

import graded.unit.lostmypetrestapi.entity.posts.Found;
import graded.unit.lostmypetrestapi.entity.reports.Report;
import graded.unit.lostmypetrestapi.entity.users.User;
import graded.unit.lostmypetrestapi.exception.CustomResponse;
import graded.unit.lostmypetrestapi.repository.ReportRepository;
import graded.unit.lostmypetrestapi.service.impl.ReportServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ReportServiceMockTest {

    @Mock
    private ReportRepository repository;

    @InjectMocks
    private ReportServiceImpl service;

    private Report report = new Report(new User(), new Found(), "Test message in the report");

    @Before
    public void setUp() {
        //create mock
        MockitoAnnotations.initMocks(this);
        report.setId(99L);
    }

    @Test
    public void testGetAllReports() {
        //given
        given(repository.findAll()).willReturn(Collections.singletonList(report));

        //calling method under the test
        List<Report> allReports = service.getAllReports();

        //assert respond has 1 objects
        assertThat(allReports).hasSize(1);

        //assert fields
        assertReportFields(report);


        //verify that repository was called
        verify(repository, times(1)).findAll();
    }

    @Test
    public void testGetReportById() {
        //given
        given(repository.findById(99L)).willReturn(Optional.ofNullable(report));

        //calling method under the test
        Optional<Report> optReport = service.getReportById(99L);

        //assert respond has 1 objects
        assertThat(optReport.isPresent()).isTrue();

        //assert fields
        assertReportFields(optReport.orElseGet(null));

        //verify that repository was called
        verify(repository, times(1)).findById(99L);
    }

    @Test
    public void testAddReport() {
        //given
        given(repository.save(report)).willReturn(report);

        //calling method under the test
        ResponseEntity<CustomResponse> response = service.addReport(report);

        //assert that HTTP code is 200 and body is not null
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).contains("Report sent by user with ID: ");

        //verify that repository was called
        verify(repository, times(1)).save(report);
    }

    @Test
    public void testDeleteReport() {
        ResponseEntity<CustomResponse> response = service.deleteReport(99L);

        //assert that HTTP code is 200 and body is not null
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).contains("Report deleted. (ID: 99)");
    }

    private void assertReportFields(Report report) {
        assertThat(report.getId()).isEqualTo(99L);
        assertThat(report.getMessage()).isEqualTo("Test message in the report");
    }
}