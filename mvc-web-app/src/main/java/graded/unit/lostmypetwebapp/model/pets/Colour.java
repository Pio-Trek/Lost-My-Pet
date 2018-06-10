package graded.unit.lostmypetwebapp.model.pets;

/**
 * Data model class for Colour object
 */
public class Colour {

    /**
     * Colour id number field
     */
    private Integer id;

    /**
     * Pet colour 'name' field
     */
    private String colour;


    // Constructors

    public Colour() {
    }

    public Colour(String colour) {
        this.colour = colour;
    }


    // Getters and setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    @Override
    public String toString() {
        return colour.toString();
    }
}
