package graded.unit.lostmypetrestapi.repository;

import graded.unit.lostmypetrestapi.entity.pets.CatBreed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * JPA repository that provides data access layer and query
 * methods for {@link CatBreed} object
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
public interface CatBreedRepository extends JpaRepository<CatBreed, Integer> {

    Optional<CatBreed> findByName(String catBreed);
}
