package graded.unit.lostmypetrestapi.service;

import graded.unit.lostmypetrestapi.entity.pets.Colour;
import graded.unit.lostmypetrestapi.entity.pets.PetColour;

import java.util.List;
import java.util.Optional;

/**
 * Service layer which manages the data of the pets {@link Colour} domain object
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
public interface ColourService {

    /**
     * Fetch all pet colours from the database
     *
     * @return List of all pet {@link Colour} objects
     */
    List<Colour> getAllColours();

    /**
     * Fetch a single pet colour by colour name
     *
     * @param colour enumeration name to be fetched
     * @return Optional value of {@link Colour} object
     */
    Optional<Colour> getColourByName(PetColour colour);
}
