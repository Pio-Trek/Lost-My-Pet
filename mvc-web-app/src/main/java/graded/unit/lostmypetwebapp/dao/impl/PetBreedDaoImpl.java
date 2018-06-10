package graded.unit.lostmypetwebapp.dao.impl;

import graded.unit.lostmypetwebapp.client.WebClient;
import graded.unit.lostmypetwebapp.dao.PetBreedDao;
import graded.unit.lostmypetwebapp.model.pets.CatBreed;
import graded.unit.lostmypetwebapp.model.pets.DogBreed;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Implementation of {@link PetBreedDao} interface
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@Repository
public class PetBreedDaoImpl implements PetBreedDao {

    private final String LOST_SERVICE_URL = "/breeds/";

    private final WebClient client;

    public PetBreedDaoImpl(WebClient client) {
        this.client = client;
    }

    @Override
    public List<DogBreed> fetchDogBreeds() {
        return client.getRestTemplate()
                .exchange(
                        LOST_SERVICE_URL + "dogs",
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<DogBreed>>() {
                        }).getBody();
    }

    @Override
    public List<CatBreed> fetchCatBreeds() {
        return client.getRestTemplate()
                .exchange(
                        LOST_SERVICE_URL + "cats",
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<CatBreed>>() {
                        }).getBody();
    }
}
