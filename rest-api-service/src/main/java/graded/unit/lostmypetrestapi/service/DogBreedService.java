package graded.unit.lostmypetrestapi.service;

import graded.unit.lostmypetrestapi.entity.pets.DogBreed;

import java.util.List;
import java.util.Optional;

/**
 * Service layer which manages the data of the {@link DogBreed} domain object
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
public interface DogBreedService {

    /**
     * Fetch all dog breeds from the database
     *
     * @return List of all {@link DogBreed} objects
     */
    List<DogBreed> getAllDogBreeds();

    /**
     * Fetch a single dog breed by name
     *
     * @param dogBreed object name to be fetched
     * @return Optional value of {@link DogBreed} object
     */
    Optional<DogBreed> getDogBreedByName(String dogBreed);
}
