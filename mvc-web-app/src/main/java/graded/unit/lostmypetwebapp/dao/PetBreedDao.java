package graded.unit.lostmypetwebapp.dao;

import graded.unit.lostmypetwebapp.model.pets.CatBreed;
import graded.unit.lostmypetwebapp.model.pets.DogBreed;

import java.util.List;

/**
 * Data Access Object interface which manages the data of the {@link DogBreed}
 * and {@link CatBreed} domain object from the consume Service.
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
public interface PetBreedDao {

    /**
     * Fetch a dog breeds.
     *
     * @return List of all {@link DogBreed} objects.
     */
    List<DogBreed> fetchDogBreeds();

    /**
     * Fetch a cat breeds.
     *
     * @return List of all {@link CatBreed} objects.
     */
    List<CatBreed> fetchCatBreeds();

}
