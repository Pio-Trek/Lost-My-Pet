package graded.unit.lostmypetrestapi.controller;

import graded.unit.lostmypetrestapi.entity.pets.CatBreed;
import graded.unit.lostmypetrestapi.entity.pets.DogBreed;
import graded.unit.lostmypetrestapi.service.CatBreedService;
import graded.unit.lostmypetrestapi.service.DogBreedService;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.pojo.ApiStage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller layer class for {@link DogBreed} and {@link CatBreed} objects which exposes resources,
 * read operations and business logic for {@link DogBreedService} and the {@link CatBreedService}.
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@RestController
@RequestMapping("/api/breeds")
@Api(
        name = "Pet's Breed API",
        description = "Provides a list of methods that manage pet's breed.",
        stage = ApiStage.GA)
public class PetBreedController {

    // URI address for testing purpose
    static String URI = "/api/breeds/";

    private final DogBreedService dogBreedService;
    private final CatBreedService catBreedService;

    @Autowired
    public PetBreedController(DogBreedService dogBreedService, CatBreedService catBreedService) {
        this.dogBreedService = dogBreedService;
        this.catBreedService = catBreedService;
    }

    /**
     * HTTP GET request method.
     * Fetch all dog breeds from the database.
     *
     * @return List of all {@link DogBreed} objects.
     */
    @GetMapping(path = "/dogs",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Fetch all dog breeds.")
    public List<DogBreed> fetchAllDogBreeds() {
        return dogBreedService.getAllDogBreeds();
    }

    /**
     * HTTP GET request method.
     * Fetch all cat breeds from the database.
     *
     * @return List of all {@link CatBreed} objects.
     */
    @GetMapping(path = "/cats",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Fetch all cat breeds.")
    public List<CatBreed> fetchAllCatBreeds() {
        return catBreedService.getAllCatBreeds();
    }
}
