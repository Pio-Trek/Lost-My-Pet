package graded.unit.lostmypetrestapi.repository;

import graded.unit.lostmypetrestapi.entity.pets.DogBreed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * JPA repository that provides data access layer and query
 * methods for {@link DogBreed} object
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
public interface DogBreedRepository extends JpaRepository<DogBreed, Integer> {

    Optional<DogBreed> findByName(String dogBreed);
}
