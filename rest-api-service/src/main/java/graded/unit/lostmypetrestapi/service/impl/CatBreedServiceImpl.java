package graded.unit.lostmypetrestapi.service.impl;

import graded.unit.lostmypetrestapi.entity.pets.CatBreed;
import graded.unit.lostmypetrestapi.repository.CatBreedRepository;
import graded.unit.lostmypetrestapi.service.CatBreedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link CatBreedService} interface
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@Service
public class CatBreedServiceImpl implements CatBreedService {

    private final CatBreedRepository repository;

    @Autowired
    public CatBreedServiceImpl(CatBreedRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<CatBreed> getAllCatBreeds() {
        return repository.findAll();
    }

    @Override
    public Optional<CatBreed> getCatBreedByName(String catBreed) {
        return repository.findByName(catBreed);
    }
}
