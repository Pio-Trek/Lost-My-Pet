package graded.unit.lostmypetwebapp.model.messages;

import graded.unit.lostmypetwebapp.model.users.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Data model class for the message object.
 */
public class Message {

    /**
     * This is an id number of the message
     */
    private String id;

    /**
     * This is the date of last conversation written by the recipient.
     * The purpose of this is to display then the last time message was updated (new conversation appeared)
     */
    private Date lastUpdate;

    /**
     * This is the message subject field.
     */
    @NotBlank
    private String subject;

    /**
     * This is the {@link User} field that specifies who is the sender of the message.
     */
    @NotNull
    private User sender;

    /**
     * This is the {@link User} field that specifies who is the receiver of the message.
     */
    @NotNull
    private User recipient;

    /**
     * This is a set of all conversations that are a port of current message.
     */
    private Set<Conversation> conversations = new HashSet<>();

    // Constructors

    public Message() {
    }

    public Message(String id) {
        this.id = id;
    }

    public Message(String subject, User sender, User recipient) {
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

    public Set<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(Set<Conversation> conversations) {
        this.conversations = conversations;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }


    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", lastUpdate=" + lastUpdate +
                ", subject='" + subject + '\'' +
                ", sender=" + sender.getId() +
                ", recipient=" + recipient.getId() +
                ", conversations=" + conversations +
                '}';
    }
}
