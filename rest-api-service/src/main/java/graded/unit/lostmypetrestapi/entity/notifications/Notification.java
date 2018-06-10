package graded.unit.lostmypetrestapi.entity.notifications;

import graded.unit.lostmypetrestapi.entity.posts.Announcement;
import graded.unit.lostmypetrestapi.entity.users.User;

import javax.persistence.*;

/**
 * Join table data model class created for many-to-many association with extra column.
 * Holds user notifications about new lost/found pets in the area where the Member lives.
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@Entity
@Table(name = "notifications")
public class Notification {

    /**
     * User Notification id number field
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_notification_id")
    private Long id;

    /**
     * Many to one association with {@link User} entity
     * Refers to {@link User#id} property
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Many to one association with {@link Announcement} entity
     * Refers to {@link Announcement#id} property
     */
    @ManyToOne
    @JoinColumn(name = "announcement_id")
    private Announcement announcement;

    // Constructors

    public Notification() {
    }

    public Notification(User user, Announcement announcement) {
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

    public Announcement getAnnouncement() {
        return announcement;
    }

}
