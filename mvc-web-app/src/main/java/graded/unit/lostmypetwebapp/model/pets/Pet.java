package graded.unit.lostmypetwebapp.model.pets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import java.util.HashSet;
import java.util.Set;

/**
 * Superclass for Pet object data model.
 */

// Per-Class Annotation to include class type information in JSON output
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Dog.class, name = "dog"),
        @JsonSubTypes.Type(value = Cat.class, name = "cat")
})
public abstract class Pet {

    /**
     * Pet id number field.
     */
    private Long id;

    /**
     * Pet name field.
     */
    @Length(max = 15)
    private String name;

    /**
     * Pet gender field.
     * Refers to {@link PetGender} enumeration class with gender names
     */
    private PetGender gender;

    /**
     * Pet age field.
     */
    @Max(30)
    private Integer age;

    /**
     * Is pet chipped field.
     */
    private Boolean chipped;

    /**
     * Does a pet have a collar field.
     */
    private Boolean collar;

    /**
     * Pet image field.
     */
    private byte[] image;

    /**
     * Pet announcement description
     */
    @Length(max = 255)
    private String description;

    @JsonIgnore
    private String[] coloursFromForm;

    private Set<Colour> colours = new HashSet<>();

    // Constructors

    public Pet() {
    }

    public Pet(Long id, @Length(max = 15) String name, PetGender gender, @Max(30) Integer age, Boolean chipped, Boolean collar, byte[] image, @Length(max = 255) String description, Set<Colour> colours) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.chipped = chipped;
        this.collar = collar;
        this.image = image;
        this.description = description;
        this.colours = colours;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PetGender getGender() {
        return gender;
    }

    public void setGender(PetGender gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getChipped() {
        return chipped;
    }

    public void setChipped(Boolean chipped) {
        this.chipped = chipped;
    }

    public Boolean getCollar() {
        return collar;
    }

    public void setCollar(Boolean collar) {
        this.collar = collar;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Colour> getColours() {
        return colours;
    }

    public void setColours(Set<Colour> colours) {
        this.colours = colours;
    }

    public String[] getColoursFromForm() {
        return coloursFromForm;
    }

    public void setColoursFromForm(String[] coloursFromForm) {
        this.coloursFromForm = coloursFromForm;
    }
}
