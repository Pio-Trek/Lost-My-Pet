package graded.unit.lostmypetrestapi.entity.locations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import graded.unit.lostmypetrestapi.entity.posts.Announcement;
import graded.unit.lostmypetrestapi.entity.users.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

/**
 * Data model class for Location object.
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@Entity
@Table(name = "locations")
public class Location {

    /**
     * Location id number field
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Integer id;

    /**
     * Location 'name' field
     */
    @NotBlank
    @Column(unique = true, name = "name")
    private String name;

    /**
     * One to many association with {@link Announcement} entity
     * Refers to {@link Announcement#location} property
     */
    @OneToMany(mappedBy = "location",
            cascade = {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.REFRESH})
    @JsonIgnore
    private Set<Announcement> announcements = new HashSet<>();

    /**
     * One to many association with {@link User} entity
     * Refers to {@link User#location} property
     */
    @OneToMany(mappedBy = "location",
            cascade = {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.REFRESH})
    @JsonIgnore
    private Set<User> members = new HashSet<>();


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
