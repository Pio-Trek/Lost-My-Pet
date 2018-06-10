package graded.unit.lostmypetrestapi.service.impl;

import graded.unit.lostmypetrestapi.entity.pets.Colour;
import graded.unit.lostmypetrestapi.entity.pets.PetColour;
import graded.unit.lostmypetrestapi.repository.ColourRepository;
import graded.unit.lostmypetrestapi.service.ColourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link ColourService} interface
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@Service
public class ColourServiceImpl implements ColourService {

    private final ColourRepository repository;

    @Autowired
    public ColourServiceImpl(ColourRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Colour> getAllColours() {
        return repository.findAll();
    }

    @Override
    public Optional<Colour> getColourByName(PetColour colour) {
        return repository.findByColour(colour);
    }
}
