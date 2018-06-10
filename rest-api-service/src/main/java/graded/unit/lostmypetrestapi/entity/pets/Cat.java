package graded.unit.lostmypetrestapi.entity.pets;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import java.util.Set;

/**
 * Data model class for Cat object which inherit from {@link Pet}
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@Entity
public class Cat extends Pet {

    /**
     * Many to one association with {@link CatBreed} entity
     * Refers to {@link CatBreed#id} property
     */
    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "cat_breed_id")
    private CatBreed catBreed;

    // Constructors

    public Cat() {
    }

    public Cat(PetGender gender, Boolean chipped, Boolean collar, byte[] image, String description, Set<Colour> colours, CatBreed catBreed) {
        super(gender,chipped, collar, image, description, colours);
        this.catBreed = catBreed;
    }

    public Cat(String name, PetGender gender, @Max(30) Integer age, Boolean chipped, Boolean collar, byte[] image, String description, Set<Colour> colours, CatBreed catBreed) {
        super(name, gender, age, chipped, collar, image, description, colours);
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
