package graded.unit.lostmypetwebapp.model.pets;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import java.util.Set;

/**
 * Data model class for Cat object which inherit from {@link Pet}
 */
public class Cat extends Pet {

    /**
     * This is the cat breed object field.
     */
    private CatBreed catBreed;

    // Constructors

    public Cat() {
    }

    public Cat(Long id, @Length(max = 15) String name, PetGender gender, @Max(30) Integer age, Boolean chipped, Boolean collar, byte[] image, @Length(max = 255) String description, Set<Colour> colours, CatBreed catBreed) {
        super(id, name, gender, age, chipped, collar, image, description, colours);
        this.catBreed = catBreed;
    }

// Getters and setters

    public CatBreed getBreed() {
        return catBreed;
    }

    public void setBreed(CatBreed breed) {
        this.catBreed = breed;
    }
}
