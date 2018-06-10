package graded.unit.lostmypetwebapp.model.posts;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import graded.unit.lostmypetwebapp.model.locations.Location;
import graded.unit.lostmypetwebapp.model.pets.Pet;
import graded.unit.lostmypetwebapp.model.users.User;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.util.Date;

/**
 * Data model class for Lost object which inherit from {@link Announcement}
 */
@JsonTypeName("lost")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "postType")
public class Lost extends Announcement {

    /**
     * Date when pet was lost field
     */
    @NotNull
    @Past
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date lostDate;

    private String postType;

    // Constructors

    public Lost() {
    }

    public Lost(Location location, User member, Pet pet, @NotNull @Past Date lostDate) {
        super(location, member, pet);
        this.lostDate = lostDate;
    }

    // Getters and setters

    public Date getLostDate() {
        return lostDate;
    }

    public void setLostDate(Date lostDate) {
        this.lostDate = lostDate;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }
}
