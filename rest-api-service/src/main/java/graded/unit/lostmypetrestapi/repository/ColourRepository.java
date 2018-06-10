package graded.unit.lostmypetrestapi.repository;

import graded.unit.lostmypetrestapi.entity.pets.Colour;
import graded.unit.lostmypetrestapi.entity.pets.PetColour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * JPA repository that provides data access layer and query
 * methods for pet {@link Colour} object
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
public interface ColourRepository extends JpaRepository<Colour, Integer> {

    Optional<Colour> findByColour(PetColour colour);
}
