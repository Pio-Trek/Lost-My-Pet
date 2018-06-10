package graded.unit.lostmypetwebapp.model.messages;

import graded.unit.lostmypetwebapp.model.users.User;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * Data model class for the conversation object.
 */
public class Conversation {

    /**
     * This is an id number of the conversation.
     */
    private Long id;

    /**
     * This is the date when conversation was written.
     */
    private Date date;

    /**
     * This is a body with a message text field.
     */
    @NotBlank
    private String body;

    /**
     * This is the conversation author field.
     */
    private User author;

    /**
     * This is the message field that specifies to which message the conversation is a part of.
     */
    private Message message;

    // Constructors

    public Conversation() {
    }

    public Conversation(String body, User author, Message message) {
        this.body = body;
        this.author = author;
        this.message = message;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

}
