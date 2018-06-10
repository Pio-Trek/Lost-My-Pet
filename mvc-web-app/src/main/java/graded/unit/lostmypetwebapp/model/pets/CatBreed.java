package graded.unit.lostmypetwebapp.model.pets;

import javax.validation.constraints.NotBlank;

/**
 * Data model class for Cat Breed object
 */
public class CatBreed {

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

    public CatBreed() {
    }


    // Getters and setters

    public CatBreed(@NotBlank String name) {
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

    @Override
    public String toString() {
        return name;
    }
}
