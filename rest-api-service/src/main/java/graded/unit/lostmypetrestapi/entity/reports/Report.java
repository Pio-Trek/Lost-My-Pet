package graded.unit.lostmypetrestapi.entity.reports;

import graded.unit.lostmypetrestapi.entity.posts.Announcement;
import graded.unit.lostmypetrestapi.entity.users.User;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Data model class for Report object.
 */
@Entity
@Table(name = "reports")
public class Report {

    /**
     * Report ID number
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    /**
     * Many to one association to {@link User} that specifies report author.
     * Refers to {@link User#id} property.
     */
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Many to one association with {@link Announcement} entity
     * Refers to {@link Announcement#id} property
     */
    @ManyToOne
    @JoinColumn(name = "announcement_id")
    private Announcement announcement;

    /**
     * Date when report was created (send)
     */
    @Column(name = "send_date")
    private Date sendDate;

    /**
     * Report message field.
     */
    @NotNull
    @Length(max = 255, message = "The field must be less than 255 characters")
    @Column(name = "message")
    private String message;

    // Constructors

    public Report() {
    }

    public Report(User user, Announcement announcement, String message) {
        this.user = user;
        this.announcement = announcement;
        this.message = message;
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

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public String getMessage() {
        return message;
    }


}
