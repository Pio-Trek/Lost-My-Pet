package graded.unit.lostmypetrestapi.entity.messages;

import graded.unit.lostmypetrestapi.entity.users.User;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * Users' message conversation data model class.
 *
 * @author Piotr Przechodzki
 * @since 21/05/2018
 */
@Entity
@Table(name = "conversations")
public class Conversation {

    /**
     * Conversation id number field.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conversation_id")
    private Long id;

    /**
     * Conversation start date.
     */
    private Date date;

    /**
     * Conversation text body field.
     */
    @NotBlank
    @Column(columnDefinition = "TEXT")
    @Length(max = 1000)
    private String body;

    /**
     * Many to one association to {@link User} that specifies conversation author id.
     * Refers to {@link User#id} property.
     */
    @ManyToOne
    @JoinColumn(name = "author_id", updatable = false)
    private User author;

    /**
     * Many to one association to {@link Message} that specifies message id.
     * Refers to {@link Message#id} property.
     */
    @ManyToOne
    @JoinColumn(name = "message_id")
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

    public void setId(Long id) {
        this.id = id;
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
