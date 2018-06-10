package graded.unit.lostmypetrestapi.service;

import graded.unit.lostmypetrestapi.entity.messages.Message;
import graded.unit.lostmypetrestapi.exception.CustomResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;


/**
 * Service layer which manages the data of the {@link Message} domain object
 *
 * @author Piotr Przechodzki
 * @since 21/05/2018
 */
public interface MessageService {

    /**
     * Fetch a list of messages by sender id from the database.
     *
     * @param senderId This is an sender id number of the {@link Message} object.
     * @return HTTP response with a list of {@link Message} objects.
     */
    List<Message> getAllMessagesBySenderId(String senderId);

    /**
     * Fetch a list of messages by recipient id from the database.
     *
     * @param recipientId This is an recipient id number of the {@link Message} object.
     * @return HTTP response with a list of {@link Message} objects.
     */
    List<Message> getAllMessagesByRecipientId(String recipientId);

    /**
     * Fetch a single message by id from the database.
     *
     * @param id This is an id number of the {@link Message} object.
     * @return Optional value of {@link Message} object
     */
    Optional<Message> getMessageById(String id);

    /**
     * Insert a new message to the database.
     *
     * @param message This is the user {@link Message} object to be inserted.
     * @return HTTP response with {@link Message} announcement object
     */
    ResponseEntity<Message> addMessage(Message message);

    /**
     * Delete existing message from the database.
     *
     * @param messageId This is an id number of the {@link Message} object to be delete.
     * @return HTTP response with confirmation message
     */
    ResponseEntity<CustomResponse> deleteMessage(String messageId);
}
