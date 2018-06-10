package graded.unit.lostmypetrestapi.service;

import graded.unit.lostmypetrestapi.entity.messages.Conversation;
import graded.unit.lostmypetrestapi.entity.messages.Message;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Service layer which manages the data of the {@link Conversation} domain object.
 *
 * @author Piotr Przechodzki
 * @since 21/05/2018
 */
public interface ConversationService {

    /**
     * Fetch all conversation of the message specified by id.
     *
     * @param messageId This is an id number of the {@link Message} object which has conversations.
     * @return List of all {@link Conversation} objects.
     */
    List<Conversation> getAllByMessageId(String messageId);

    /**
     * Insert a new conversation.
     *
     * @param conversation This is a new object of the {@link Conversation} to be insert.
     * @return HTTP response with {@link Conversation} object.
     */
    ResponseEntity<Conversation> addConversation(Conversation conversation);

    /**
     * Number of all conversation in specified message by message id.
     *
     * @param messageId This is an id number of the {@link Message} object which has conversations.
     * @return A number of all conversations.
     */
    int countAllByMessageId(String messageId);
}
