package graded.unit.lostmypetrestapi.entity.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import graded.unit.lostmypetrestapi.entity.locations.Location;
import graded.unit.lostmypetrestapi.entity.messages.Conversation;
import graded.unit.lostmypetrestapi.entity.messages.Message;
import graded.unit.lostmypetrestapi.entity.notifications.Notification;
import graded.unit.lostmypetrestapi.entity.posts.Announcement;
import graded.unit.lostmypetrestapi.entity.reports.Report;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * Superclass for User object.
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@Entity
@Table(name = "users")
public class User {

    /**
     * User id number field.
     */
    @Id
    @Column(name = "user_id")
    private String id;

    /**
     * Member 'email' field use to sign in.
     */
    @NotBlank
    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * User password field
     */
    @Column(name = "password")
    //@JsonProperty(access = WRITE_ONLY)
    private String password;

    /**
     * Member first name field.
     */
    @NotBlank
    @Column(name = "first_name")
    @Size(min = 3, max = 20)
    private String firstName;

    /**
     * Member last name field.
     */
    @Column(name = "last_name")
    @Size(max = 20)
    private String lastName;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "confirmation_token")
    private String confirmationToken;

    /**
     * Many to one association to {@link Location} that specifies area name
     * where member lives.
     * Refers to {@link Location#id} property
     */
    @ManyToOne(cascade = {
            CascadeType.MERGE})
    @JoinColumn(name = "location_id")
    private Location location;

    /**
     * One to many association with {@link Announcement} entity that specifies
     * current user announcements.
     * Refers to {@link Announcement#member} property.
     */
    @OneToMany(mappedBy = "member", cascade = {CascadeType.ALL})
    @JsonIgnore
    private Set<Announcement> announcements = new HashSet<>();

    /**
     * Many to many association with {@link UserRole} entity that specifies
     * current user roles in the system.
     * Refers to {@link UserRole#id} property
     */
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.PERSIST,
                    CascadeType.REFRESH})
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<UserRole> roles;

    /**
     * One to many association to {@link Notification} that specifies notifications
     * about new lost/found pets in the area where the Member lives.
     * Refers to {@link Notification#user} property.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Notification> notifications = new HashSet<>();

    /**
     * One to many association to {@link Report} that specifies user reports
     * about announcements.
     * Refers to {@link Report#user} property.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Report> reports = new HashSet<>();

    /**
     * One to many association to {@link Conversation} that specifies conversations where
     * user is the author.
     * Refers to {@link Conversation#author} property.
     */
    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
    private Set<Conversation> conversations;

    /**
     * One to many association to {@link Message} that specifies messages where
     * user is the recipient.
     * Refers to {@link Message#recipient} property.
     */
    @OneToMany(mappedBy = "recipient", cascade = CascadeType.REMOVE)
    private Set<Message> messagesRecived;

    /**
     * One to many association to {@link Message} that specifies messages where
     * user is the sender.
     * Refers to {@link Message#sender} property.
     */
    @OneToMany(mappedBy = "sender", cascade = CascadeType.REMOVE)
    private Set<Message> messagesSended;

    // Constructors

    public User() {
    }

    public User(String id, @NotBlank @Email String email, @NotBlank @Size(min = 6, max = 30) String password, @NotBlank @Size(min = 3, max = 20) String firstName, @Size(max = 20) String lastName, Location location) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
    }

    // Getters and setters


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

}
