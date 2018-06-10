package graded.unit.lostmypetrestapi.entity.pets;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

/**
 * Data model class for Dog Breed object
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@Entity
@Table(name = "dog_breeds")
public class DogBreed {

    /**
     * Location id number field
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "breed_id")
    private Integer id;

    /**
     * Location 'name' field
     */
    @NotBlank
    @Column(unique = true, name = "name")
    private String name;

    /**
     * One to many association with {@link Dog} entity
     * Refers to {@link Dog#dogBreed} property
     */
    @OneToMany(mappedBy = "dogBreed",
            cascade = {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.PERSIST,
                    CascadeType.REFRESH})
    @JsonIgnore
    private Set<Dog> dogs = new HashSet<>();


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

}
