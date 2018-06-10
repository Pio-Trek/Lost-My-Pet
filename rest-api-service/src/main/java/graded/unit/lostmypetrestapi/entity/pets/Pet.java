package graded.unit.lostmypetrestapi.entity.pets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import graded.unit.lostmypetrestapi.entity.posts.Announcement;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.util.HashSet;
import java.util.Set;

/**
 * Abstract superclass for Pet object.
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "pets")

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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    private Long id;

    /**
     * Pet name field.
     */
    @Column(name = "name", length = 10)
    private String name;

    /**
     * Pet gender field.
     * Refers to {@link PetGender} enumeration class with gender names
     */
    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private PetGender gender;

    /**
     * Pet age field.
     */
    @Column(name = "age")
    @Max(30)
    private Integer age;

    /**
     * Is pet chipped field.
     */
    @Column(name = "chipped")
    private Boolean chipped;

    /**
     * Does a pet have a collar field.
     */
    @Column(name = "collar")
    private Boolean collar;

    /**
     * Pet image field.
     */
    @Lob
    @Column(name = "image")
    private byte[] image;

    /**
     * Pet announcement description
     */
    @Column(name = "description")
    private String description;

    /**
     * Many to many association with {@link Colour} entity
     * Refers to {@link Colour#id} property
     */
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "pet_colours",
            joinColumns = @JoinColumn(name = "pet_id"),
            inverseJoinColumns = @JoinColumn(name = "colour_id"))
    private Set<Colour> colours = new HashSet<>();

    /**
     * One to one association with {@link Announcement} entity
     * Refers to {@link Announcement#pet} property
     */
    @OneToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "pet")
    @JsonIgnore
    private Announcement announcement;


    // Constructors

    public Pet() {
    }

    public Pet(PetGender gender, Boolean chipped, Boolean collar, byte[] image, String description, Set<Colour> colours) {
        this.gender = gender;
        this.chipped = chipped;
        this.collar = collar;
        this.image = image;
        this.description = description;
        this.colours = colours;
    }

    public Pet(String name, PetGender gender, @Max(30) Integer age, Boolean chipped, Boolean collar, byte[] image, String description, Set<Colour> colours) {
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getChipped() {
        return chipped;
    }

    public Boolean getCollar() {
        return collar;
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
}
