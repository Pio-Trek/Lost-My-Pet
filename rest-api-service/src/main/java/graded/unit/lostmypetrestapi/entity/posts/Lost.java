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
 * Data model class for Lost object which inherit from {@link Announcement}
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@Entity
public class Lost extends Announcement {

    /**
     * Date when pet was lost field
     */
    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    @Column(name = "lost_date")
    private Date lostDate;


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
        return "lost";
    }
}
