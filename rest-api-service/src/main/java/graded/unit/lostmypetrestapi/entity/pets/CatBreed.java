package graded.unit.lostmypetrestapi.entity.pets;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

/**
 * Data model class for Cat Breed object.
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@Entity
@Table(name = "cat_breeds")
public class CatBreed {

    /**
     * Location id number field
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cat_id")
    private Integer id;

    /**
     * Location 'name' field
     */
    @NotBlank
    @Column(unique = true, name = "name")
    private String name;

    /**
     * One to many association with {@link Cat} entity
     * Refers to {@link Cat#catBreed} property
     */
    @OneToMany(mappedBy = "catBreed",
            cascade = {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.PERSIST,
                    CascadeType.REFRESH})
    @JsonIgnore
    private Set<Cat> cats = new HashSet<>();


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

}
