package graded.unit.lostmypetrestapi.entity.pets;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import java.util.Set;

/**
 * Data model class for Dog object which inherit from {@link Pet}
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@Entity
public class Dog extends Pet {

    /**
     * Many to one association with {@link DogBreed} entity
     * Refers to {@link DogBreed#id} property
     */
    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "dog_breed_id")
    private DogBreed dogBreed;


    // Constructors

    public Dog() {
    }

    public Dog(PetGender gender, Boolean chipped, Boolean collar, byte[] image, String description, Set<Colour> colours, DogBreed dogBreed) {
        super(gender, chipped, collar, image, description, colours);
        this.dogBreed = dogBreed;
    }

    public Dog(String name, PetGender gender, @Max(30) Integer age, Boolean chipped, Boolean collar, byte[] image, String description, Set<Colour> colours, DogBreed dogBreed) {
        super(name, gender, age, chipped, collar, image, description, colours);
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
