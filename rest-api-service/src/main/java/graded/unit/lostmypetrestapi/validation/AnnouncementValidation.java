package graded.unit.lostmypetrestapi.validation;

import graded.unit.lostmypetrestapi.entity.locations.Location;
import graded.unit.lostmypetrestapi.entity.pets.*;
import graded.unit.lostmypetrestapi.entity.posts.Announcement;
import graded.unit.lostmypetrestapi.exception.CustomException;
import graded.unit.lostmypetrestapi.service.CatBreedService;
import graded.unit.lostmypetrestapi.service.ColourService;
import graded.unit.lostmypetrestapi.service.DogBreedService;
import graded.unit.lostmypetrestapi.service.LocationService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Announcement object validation helper class.
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@Service
public class AnnouncementValidation {

    private final DogBreedService dogBreedService;
    private final CatBreedService catBreedService;
    private final LocationService locationService;
    private final ColourService colourService;

    @Autowired
    protected AnnouncementValidation(DogBreedService dogBreedService, CatBreedService catBreedService, LocationService locationService, ColourService colourService) {
        this.dogBreedService = dogBreedService;
        this.catBreedService = catBreedService;
        this.locationService = locationService;
        this.colourService = colourService;
    }

    /**
     * Method that checks if given string is empty or null.
     *
     * @param str This is the string variable to be checked.
     * @return Boolean value.
     */
    private boolean isEmptyField(final String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Method that validate the dog breed by checking if it exists in the database.
     *
     * @param breed This is the {@link DogBreed} object to be checked.
     * @return Return optional {@link DogBreed} object.
     * @throws CustomException When {@link DogBreed#name} is not valid.
     */
    private Optional<DogBreed> validateDogBreed(DogBreed breed) throws CustomException {
        return Optional.of(dogBreedService.getDogBreedByName(breed.getName()))
                .orElseThrow(() -> new CustomException
                        ("Dog breed: '" + breed.getName() + "' not found"));
    }


    /**
     * Method that validate the cat breed by checking if it exists in the database.
     *
     * @param breed This is the {@link CatBreed} object to be checked.
     * @return Return optional {@link CatBreed} object.
     * @throws CustomException When {@link CatBreed#name} is not valid.
     */
    private Optional<CatBreed> validateCatBreed(CatBreed breed) throws CustomException {
        return Optional.of(catBreedService.getCatBreedByName(breed.getName()))
                .orElseThrow(() -> new CustomException
                        ("Cat breed: '" + breed.getName() + "' not found"));
    }

    /**
     * Method that check if given {@link Pet} object is an instance of {@link Dog} or {@link Cat}.
     * Uses {@link AnnouncementValidation#validateCatBreed(CatBreed)} and
     * {@link AnnouncementValidation#validateDogBreed(DogBreed)} to validate Cat/Dog breed.
     *
     * @param announcement This is the {@link Announcement} object to be checked.
     * @return Return {@link Dog} or {@link Cat} object.
     * @throws CustomException When no any pet breed is not valid.
     */
    public Pet getPetTypeAndBreed(Announcement announcement) throws CustomException {
        if (announcement.getPet() instanceof Dog) {
            Dog dog = (Dog) announcement.getPet();
            if (dog.getBreed() != null && !dog.getBreed().getName().isEmpty()) {
                validateDogBreed(dog.getBreed()).ifPresent(dog::setBreed);
            } else {
                dog.setBreed(null);
            }

            return dog;

        } else if (announcement.getPet() instanceof Cat) {
            Cat cat = (Cat) announcement.getPet();
            if (cat.getBreed() != null && !cat.getBreed().getName().isEmpty()) {
                validateCatBreed(cat.getBreed()).ifPresent(cat::setBreed);
            } else {
                cat.setBreed(null);
            }

            return cat;

        } else {
            throw new RuntimeException("Pet object must be an instance of 'dog' or 'cat' class. Found instance of '" + announcement.getPet().getClass().getName() + "' class.");
        }
    }

    /**
     * Method validate {@link Location} object in the announcement post.
     *
     * @param post This is the {@link Announcement} post to be checked.
     * @return Return optional {@link Location} object.
     * @throws NotFoundException When given {@link Announcement#location} name is not valid.
     */
    public Optional<Location> validateLocation(Announcement post) throws NotFoundException {
        return Optional.of(locationService.getLocationByName(post.getLocation().getName())
                .orElseThrow(() -> new NotFoundException
                        ("Location name: '" + post.getLocation().getName() + "' not found.")));
    }

    /**
     * Iterate over a collection to validate {@link PetColour} name using an Optional class
     * and a method reference add every valid colour to a new set of colours.
     *
     * @param announcement This is the {@link Announcement} object to be checked.
     * @return Set of pet {@link Colour} objects.
     */
    public Set<Colour> getPetColours(Announcement announcement) {
        Set<Colour> colours = new HashSet<>();
        announcement.getPet().getColours()
                .forEach(c -> validateColour(c.getColour())
                        .ifPresent(colours::add));
        return colours;
    }

    /**
     * Validate {@link PetColour} name with colours saved in the database.
     *
     * @param colour This is the {@link PetColour} object to be checked.
     * @return Optional {@link Colour} object.
     */
    private Optional<Colour> validateColour(PetColour colour) {
        return Optional.of(colourService.getColourByName(colour))
                .orElseThrow(() -> new RuntimeException("Pet colour: '" + colour + "' not found."));
    }

    /**
     * Method that validate if {@link Pet} object have no more then 4 {@link PetColour} objects.
     *
     * @param announcement This is the {@link Announcement} object to be checked.
     */
    public void validateNumberOfColours(Announcement announcement) {
        if (announcement.getPet().getColours().size() >= 5) {
            throw new RuntimeException("Maximum of 4 colours allowed.");
        }
    }

    /**
     * Validate if the {@link Pet#name} is not empty for the lost announcement.
     *
     * @param name This is the string with pet name to be checked.
     */
    public void validatePetName(String name) {
        if (isEmptyField(name)) {
            throw new RuntimeException("Pet name cannot be empty, blank or null.");
        }
    }
}
