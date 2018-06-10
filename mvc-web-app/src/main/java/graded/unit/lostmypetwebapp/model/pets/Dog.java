package graded.unit.lostmypetwebapp.model.pets;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import java.util.Set;

/**
 * Data model class for Dog object which inherit from {@link Pet}
 */
public class Dog extends Pet {

    /**
     * This is the dog breed field.
     */
    private DogBreed dogBreed;


    // Constructors

    public Dog() {
    }

    public Dog(Long id, @Length(max = 15) String name, PetGender gender, @Max(30) Integer age, Boolean chipped, Boolean collar, byte[] image, @Length(max = 255) String description, Set<Colour> colours, DogBreed dogBreed) {
        super(id, name, gender, age, chipped, collar, image, description, colours);
        this.dogBreed = dogBreed;
    }

    // Getters and setters

    public DogBreed getBreed() {
        return dogBreed;
    }

    public void setBreed(DogBreed breed) {
        this.dogBreed = breed;
    }

}
