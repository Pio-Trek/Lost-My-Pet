package graded.unit.lostmypetrestapi.entity.messages;

import com.fasterxml.jackson.annotation.JsonBackReference;
import graded.unit.lostmypetrestapi.entity.users.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * User message system data model class.
 *
 * @author Piotr Przechodzki
 * @since 21/05/2018
 */
@Entity
@Table(name = "messages")
public class Message {

    /**
     * Message id number field.
     */
    @Id
    @Column(name = "message_id")
    private String id;

    /**
     * Message last update date field.
     */
    private Date lastUpdate;

    /**
     * Message subject field.
     */
    @NotBlank
    private String subject;

    /**
     * Many to one association to {@link User} that specifies sender id.
     * Refers to {@link User#id} property.
     */
    @ManyToOne
    @JoinColumn(name = "sender_id", updatable = false)
    private User sender;

    /**
     * Many to one association to {@link User}  that specifies recipient id.
     * Refers to {@link User#id} property.
     */
    @ManyToOne
    @JoinColumn(name = "recipient_id", updatable = false)
    private User recipient;

    /**
     * One to many association with {@link Conversation} entity
     * Refers to {@link Conversation#message} property
     */
    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<Conversation> conversations = new HashSet<>();

    // Constructors

    public Message() {
    }

    public Message(@NotBlank String subject, User sender, User recipient) {
        this.subject = subject;
        this.sender = sender;
        this.recipient = recipient;
    }

    // Getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

}
