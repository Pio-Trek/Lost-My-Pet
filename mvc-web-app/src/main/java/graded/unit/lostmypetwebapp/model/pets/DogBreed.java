package graded.unit.lostmypetwebapp.model.pets;

import javax.validation.constraints.NotBlank;

/**
 * Data model class for Dog Breed object
 */
public class DogBreed {

    /**
     * Location id number field
     */
    private Integer id;

    /**
     * Location 'name' field
     */
    @NotBlank
    private String name;

    // Constructors

    public DogBreed() {
    }

    // Getters and setters

    public DogBreed(@NotBlank String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }


}
