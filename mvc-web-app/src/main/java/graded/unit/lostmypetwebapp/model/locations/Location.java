package graded.unit.lostmypetwebapp.model.locations;

import javax.validation.constraints.NotBlank;

/**
 * Data model class for the location object.
 */
public class Location {

    /**
     * Location id number field.
     */
    private Integer id;

    /**
     * Location name field.
     */
    @NotBlank
    private String name;

    // Constructors

    public Location() {
    }

    public Location(@NotBlank String name) {
        this.name = name;
    }


    // Getters and setters

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

}
