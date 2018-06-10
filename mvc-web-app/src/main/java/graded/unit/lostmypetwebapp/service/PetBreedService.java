package graded.unit.lostmypetwebapp.service;

import graded.unit.lostmypetwebapp.dao.PetBreedDao;
import graded.unit.lostmypetwebapp.model.pets.CatBreed;
import graded.unit.lostmypetwebapp.model.pets.DogBreed;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer which manages the data of the {@link DogBreed} and {@link CatBreed} domain object
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@Service
public class PetBreedService {

    private final PetBreedDao petBreedDao;

    public PetBreedService(PetBreedDao petBreedDao) {
        this.petBreedDao = petBreedDao;
    }

    /**
     * Fetch a dog breeds.
     *
     * @return List of all {@link DogBreed} objects.
     */
    public List<DogBreed> getDogBreeds() {
        return petBreedDao.fetchDogBreeds();
    }

    /**
     * Fetch a cat breeds.
     *
     * @return List of all {@link CatBreed} objects.
     */
    public List<CatBreed> getCatBreeds() {
        return petBreedDao.fetchCatBreeds();
    }
}
