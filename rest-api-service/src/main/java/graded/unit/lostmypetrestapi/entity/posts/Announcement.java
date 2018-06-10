package graded.unit.lostmypetrestapi.entity.posts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import graded.unit.lostmypetrestapi.entity.locations.Location;
import graded.unit.lostmypetrestapi.entity.notifications.Notification;
import graded.unit.lostmypetrestapi.entity.pets.Pet;
import graded.unit.lostmypetrestapi.entity.reports.Report;
import graded.unit.lostmypetrestapi.entity.users.User;

import javax.persistence.*;
import javax.validation.constraints.Past;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Abstract superclass for User object.
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "announcements")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "postType",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Found.class, name = "found"),
        @JsonSubTypes.Type(value = Lost.class, name = "lost")
})
public abstract class Announcement {

    /**
     * Announcement id number field.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "announcement_id")
    private Long id;

    /**
     * Date when announcement was added field
     */
    @Past
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "added_date")
    private Date addedDate;

    /**
     * Many to one association to {@link Location} entity.
     * Refers to {@link Location#id} property
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "location_id")
    private Location location;

    /**
     * Many to one association to {@link User} entity.
     * Refers to {@link User#id} property
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id")
    private User member;

    /**
     * One to one association to {@link Pet} entity.
     * Refers to {@link Pet#id} property
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    /**
     * One to many association to {@link Notification} entity.
     * Refers to {@link Notification#announcement} property
     */
    @OneToMany(mappedBy = "announcement", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Notification> notifications = new HashSet<>();

    @Column(name = "enabled")
    private Boolean enabled;

    @OneToMany(mappedBy = "announcement", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Report> reports = new HashSet<>();


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

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public User getMember() {
        return member;
    }

    public void setMember(User member) {
        this.member = member;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}