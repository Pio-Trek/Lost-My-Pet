package graded.unit.lostmypetwebapp.model.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import graded.unit.lostmypetwebapp.model.locations.Location;
import graded.unit.lostmypetwebapp.model.messages.Conversation;
import graded.unit.lostmypetwebapp.model.messages.Message;
import graded.unit.lostmypetwebapp.model.notifications.Notification;
import graded.unit.lostmypetwebapp.model.posts.Announcement;
import graded.unit.lostmypetwebapp.model.reports.Report;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * Superclass for User object.
 */
public class User {

    /**
     * This is an user id number field.
     */
    private String id;

    /**
     * This is the member 'email' field use to sign in.
     */
    @Email(message = "Please provide a valid e-mail")
    @NotBlank(message = "Please provide an e-mail")
    private String email;

    /**
     * This is the user password field
     */
    @Size(min = 6, max = 30)
    private String password;

    /**
     * This is the member first name field.
     */
    @NotBlank(message = "Please provide your first name")
    @Size(min = 3, max = 20)
    private String firstName;

    /**
     * This is the member last name field.
     */
    @Size(max = 20)
    private String lastName;

    /**
     * This field determines whether the user account is activated.
     * It is used when the member is creating a new account.
     */
    private Boolean enabled;

    /**
     * This is the confirmation token string that is used when user activates the account.
     */
    private String confirmationToken;

    /**
     * This is the user location field.
     */
    private Location location;

    /**
     * This is the user location helper field used only when user register a new account or wants to update
     * the account details. It is used in the post HTML form, because it is not possible to send two model
     * using one form.
     */
    @JsonIgnore
    private String locationForm;

    /**
     * This is a set of user announcements.
     */
    private Set<Announcement> announcements = new HashSet<>();

    /**
     * This is the user role field that describes the authorization role.
     */
    private Set<UserRole> roles;

    /**
     * One to many association to {@link Notification} that specifies notifications
     * about new lost/found pets in the area where the Member lives
     * Refers to {@link Notification#user} property
     */
    @JsonIgnore
    private Set<Notification> notifications = new HashSet<>();

    /**
     * This is the set of the users' report.
     */
    private Set<Report> reports = new HashSet<>();

    /**
     * This is the set of the user's conversation.
     */
    private Set<Conversation> conversations;

    /**
     * This is the set of user's received messages.
     */
    private Set<Message> messagesReceived;

    /**
     * This is the set of user's messages send.
     */
    private Set<Message> messagesSend;


    // Constructors

    public User() {
    }

    public User(String id, @NotBlank @Email String email, String password, @NotBlank @Size(min = 3, max = 20) String firstName, @Size(max = 20) String lastName, Location location) {
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

    public Set<Announcement> getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(Set<Announcement> announcements) {
        this.announcements = announcements;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    public Set<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(Set<Notification> notifications) {
        this.notifications = notifications;
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

    public String getLocationForm() {
        return locationForm;
    }

    public void setLocationForm(String locationForm) {
        this.locationForm = locationForm;
    }

    public Set<Report> getReports() {
        return reports;
    }

    public void setReports(Set<Report> reports) {
        this.reports = reports;
    }

    public Set<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(Set<Conversation> conversations) {
        this.conversations = conversations;
    }

    public Set<Message> getMessagesReceived() {
        return messagesReceived;
    }

    public void setMessagesReceived(Set<Message> messagesReceived) {
        this.messagesReceived = messagesReceived;
    }

    public Set<Message> getMessagesSend() {
        return messagesSend;
    }

    public void setMessagesSend(Set<Message> messagesSend) {
        this.messagesSend = messagesSend;
    }

}
