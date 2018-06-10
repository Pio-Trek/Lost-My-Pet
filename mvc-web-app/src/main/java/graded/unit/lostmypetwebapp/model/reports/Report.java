package graded.unit.lostmypetwebapp.model.reports;

import graded.unit.lostmypetwebapp.model.posts.Announcement;
import graded.unit.lostmypetwebapp.model.users.User;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Data model class for the report object.
 */
public class Report {

    /**
     * This is an id number of the report.
     */
    private Long id;

    /**
     * This is the {@link User} field that specifies who is the author of the report.
     */
    private User user;

    /**
     * This is the {@link Announcement} field that specifies about which announcement is the current report.
     */
    private Announcement announcement;

    /**
     * This is date when the report was send (created)
     */
    private Date sendDate;

    /**
     * This is the text message of the report.
     */
    @NotNull
    private String message;

    // Constructors

    public Report() {
    }

    public Report(User user, Announcement announcement, @NotNull String message) {
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", user=" + user +
                ", announcement=" + announcement +
                ", sendDate=" + sendDate +
                ", message='" + message + '\'' +
                '}';
    }
}
