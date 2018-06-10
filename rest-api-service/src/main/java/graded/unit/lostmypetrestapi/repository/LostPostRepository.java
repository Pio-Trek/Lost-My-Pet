package graded.unit.lostmypetrestapi.repository;

import graded.unit.lostmypetrestapi.entity.posts.Lost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * JPA repository that provides data access layer and query
 * methods for {@link Lost} pet announcement object
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
public interface LostPostRepository extends JpaRepository<Lost, Long> {

    List<Lost> findAllByMemberId(String memberId);

    int countByEnabledFalse();

}
