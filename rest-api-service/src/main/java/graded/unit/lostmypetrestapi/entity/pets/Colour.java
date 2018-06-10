package graded.unit.lostmypetrestapi.entity.pets;

import javax.persistence.*;

/**
 * Data model class for Colour object.
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@Entity
@Table(name = "colours")
public class Colour {

    /**
     * Colour id number field
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "colour_id")
    private Integer id;

    /**
     * Pet colour 'name' field.
     * Refers to {@link PetColour} enumeration class with colour names
     */
    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private PetColour colour;


    // Constructors

    public Colour() {
    }

    public Colour(PetColour colour) {
        this.colour = colour;
    }


    // Getters and setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PetColour getColour() {
        return colour;
    }

    public void setColour(PetColour colour) {
        this.colour = colour;
    }
}
