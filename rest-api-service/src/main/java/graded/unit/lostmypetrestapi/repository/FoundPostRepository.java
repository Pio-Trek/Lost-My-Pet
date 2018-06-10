package graded.unit.lostmypetrestapi.repository;

import graded.unit.lostmypetrestapi.entity.posts.Found;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * JPA repository that provides data access layer and query
 * methods for {@link Found} pet announcement object
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
public interface FoundPostRepository extends JpaRepository<Found, Long> {

    List<Found> findAllByMemberId(String memberId);

    int countByEnabledFalse();
}
