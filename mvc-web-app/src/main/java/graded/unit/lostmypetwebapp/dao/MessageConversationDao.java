package graded.unit.lostmypetwebapp.dao;

import graded.unit.lostmypetwebapp.model.messages.Conversation;
import graded.unit.lostmypetwebapp.model.messages.Message;
import graded.unit.lostmypetwebapp.model.users.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Data Access Object interface which manages the data of the {@link Message}
 * and {@link Conversation} domain object from the consume Service.
 *
 * @author Piotr Przechodzki
 * @since 21/05/2018
 */
public interface MessageConversationDao {

    /**
     * Fetch a single message by id.
     *
     * @param messageId This is an id number of the {@link Message} object to be fetched.
     * @return HTTP response with {@link Message} object
     */
    ResponseEntity<Message> fetchMessageById(String messageId);

    /**
     * Fetch a list of messages by sender id.
     *
     * @param senderId This is an sender id number of the {@link User} object.
     * @return HTTP response with a list of {@link Message} objects.
     */
    ResponseEntity<List<Message>> fetchAllMessagesBySenderId(String senderId);

    /**
     * Fetch a list of messages by recipient id.
     *
     * @param recipientId This is an recipient id number of the {@link User} object.
     * @return HTTP response with a list of {@link Message} objects.
     */
    ResponseEntity<List<Message>> fetchAllMessagesByRecipientId(String recipientId);

    /**
     * Fetch a list of conversations that relate to a selected message by message id.
     *
     * @param messageId This is an id number of the {@link Message} object by which conversations will be fetched.
     * @return HTTP response with a list of {@link Conversation} objects.
     */
    ResponseEntity<List<Conversation>> fetchConversationsByMessageId(String messageId);

    /**
     * Count all conversations that relate to a selected message by message id.
     *
     * @param messageId This is an id number of the {@link Message} object by which conversations will be counted.
     * @return Number of selected message conversations.
     */
    int countConversationsByMessageId(String messageId);

    /**
     * Insert a new message.
     *
     * @param message This is the new object of the {@link Message} to be saved.
     * @return HTTP response with {@link Message} object
     */
    ResponseEntity<Message> insertMessage(Message message);

    /**
     * Insert a new conversation.
     *
     * @param conversation This is the new object of the {@link Conversation} to be saved.
     */
    void insertConversation(Conversation conversation);

    /**
     * Delete message by id.
     *
     * @param id This is an id number of the {@link Message} object to be deleted.
     */
    void removeMessage(String id);

}
