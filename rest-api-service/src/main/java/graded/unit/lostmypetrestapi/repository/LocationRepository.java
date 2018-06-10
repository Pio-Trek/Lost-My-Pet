package graded.unit.lostmypetrestapi.repository;

import graded.unit.lostmypetrestapi.entity.locations.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * JPA repository that provides data access layer and query
 * methods for {@link Location} object
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
public interface LocationRepository extends JpaRepository<Location, Integer> {

    Optional<Location> findByName(String name);

}
