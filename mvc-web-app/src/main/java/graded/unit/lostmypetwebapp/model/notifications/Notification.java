package graded.unit.lostmypetwebapp.model.notifications;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import graded.unit.lostmypetwebapp.model.posts.Announcement;
import graded.unit.lostmypetwebapp.model.users.User;

/**
 * Join table class created for many-to-many association with extra column.
 * Holds user notifications about new lost/found pets in the area where the Member lives.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Notification {

    /**
     * This is the user notification id number field.
     */
    private Long id;

    /**
     * This is the {@link User} field that specifies receiver of the notification.
     */
    private User user;

    /**
     * This is the {@link Announcement} field that specifies about which announcement is the current notification.
     */
    private Announcement announcement;

    // Constructors

    public Notification() {
    }

    public Notification(Long id, User user, Announcement announcement) {
        this.id = id;
        this.user = user;
        this.announcement = announcement;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Announcement getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(Announcement announcement) {
        this.announcement = announcement;
    }

}
