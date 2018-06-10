package graded.unit.lostmypetrestapi.entity.posts;

import graded.unit.lostmypetrestapi.entity.locations.Location;
import graded.unit.lostmypetrestapi.entity.pets.Pet;
import graded.unit.lostmypetrestapi.entity.users.User;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.util.Date;

/**
 * Data model class for Found object which inherit from {@link Announcement}
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@Entity
public class Found extends Announcement {

    /**
     * Date when pet was found field
     */
    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    @Column(name = "found_date")
    private Date foundDate;

    // Constructors

    public Found() {
    }

    public Found(Location location, User member, Pet pet, @NotNull @Past Date foundDate) {
        super(location, member, pet);
        this.foundDate = foundDate;
    }

    // Getters and setters
    

    public Date getFoundDate() {
        return foundDate;
    }

    public void setFoundDate(Date foundDate) {
        this.foundDate = foundDate;
    }

    public String getPostType() {
        return "found";
    }
}
