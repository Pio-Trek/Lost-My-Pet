package graded.unit.lostmypetrestapi.service;

import graded.unit.lostmypetrestapi.entity.pets.CatBreed;

import java.util.List;
import java.util.Optional;

/**
 * Service layer which manages the data of the {@link CatBreed} domain object.
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
public interface CatBreedService {

    /**
     * Fetch all cat breeds from the database.
     *
     * @return List of all {@link CatBreed} objects.
     */
    List<CatBreed> getAllCatBreeds();

    /**
     * Fetch a single cat breed by name
     *
     * @param catBreed object name to be fetched.
     * @return Optional value of {@link CatBreed} object.
     */
    Optional<CatBreed> getCatBreedByName(String catBreed);
}
