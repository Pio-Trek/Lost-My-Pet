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
 * Data model class for Found object which inherit from {@link Announcement}
 */
@JsonTypeName("found")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "postType")
public class Found extends Announcement {

    /**
     * Date when pet was found field
     */
    @NotNull
    @Past
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date foundDate;

    // Constructors

    public Found() {
    }

    // Getters and setters

    public Found(Location location, User member, Pet pet, @NotNull @Past Date foundDate) {
        super(location, member, pet);
        this.foundDate = foundDate;
    }

    public Date getFoundDate() {
        return foundDate;
    }

    public void setFoundDate(Date foundDate) {
        this.foundDate = foundDate;
    }
}
