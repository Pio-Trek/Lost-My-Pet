package graded.unit.lostmypetrestapi.repository;

import graded.unit.lostmypetrestapi.entity.reports.Report;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository that provides data access layer and query
 * methods for {@link Report} object
 *
 * @author Piotr Przechodzki
 * @since 21/05/2018
 */
public interface ReportRepository extends JpaRepository<Report, Long> {
}
