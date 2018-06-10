package graded.unit.lostmypetwebapp.model.posts;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import graded.unit.lostmypetwebapp.model.locations.Location;
import graded.unit.lostmypetwebapp.model.pets.Pet;
import graded.unit.lostmypetwebapp.model.users.User;

import javax.validation.constraints.Past;
import java.util.Date;

/**
 * Superclass for User object.
 */
// Per-Class Annotation to include class type information in JSON output
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "postType",
        defaultImpl = Announcement.class)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Found.class, name = "found"),
        @JsonSubTypes.Type(value = Lost.class, name = "lost")
})
public class Announcement {

    /**
     * This is an announcement id number field.
     */
    private Long id;

    /**
     * This is the date when announcement was added field
     */
    @Past
    private Date addedDate;

    /**
     * This is the location where pet was lost or found.
     */
    private Location location;

    /**
     * This the {@link User} field that specifies who is the author of the announcement.
     */
    private User member;

    /**
     * This is the {@link Pet} field that specifies the pet about which is the announcement about.
     */
    private Pet pet;

    /**
     * This field determines whether the announcement is moderated (visible to other users)
     */
    private Boolean enabled;

    // Constructors

    public Announcement() {
    }

    public Announcement(Location location, User member, Pet pet) {
        this.location = location;
        this.member = member;
        this.pet = pet;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public User getMember() {
        return member;
    }

    public void setMember(User member) {
        this.member = member;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

}