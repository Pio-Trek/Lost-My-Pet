package graded.unit.lostmypetrestapi.service.impl;

import graded.unit.lostmypetrestapi.entity.pets.DogBreed;
import graded.unit.lostmypetrestapi.repository.DogBreedRepository;
import graded.unit.lostmypetrestapi.service.DogBreedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link DogBreedService} interface
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@Service
public class DogBreedServiceImpl implements DogBreedService {

    private final DogBreedRepository repository;

    @Autowired
    public DogBreedServiceImpl(DogBreedRepository repository) {
        this.repository = repository;
    }


    @Override
    public List<DogBreed> getAllDogBreeds() {
        return repository.findAll();
    }

    @Override
    public Optional<DogBreed> getDogBreedByName(String dogBreed) {
        return repository.findByName(dogBreed);
    }
}
