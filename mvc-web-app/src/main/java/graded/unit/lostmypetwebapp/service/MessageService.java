package graded.unit.lostmypetwebapp.service;

import graded.unit.lostmypetwebapp.dao.MessageConversationDao;
import graded.unit.lostmypetwebapp.model.messages.Conversation;
import graded.unit.lostmypetwebapp.model.messages.Message;
import graded.unit.lostmypetwebapp.model.users.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer which manages the data of the {@link Message} domain object
 *
 * @author Piotr Przechodzki
 * @since 21/05/2018
 */
@Service
public class MessageService {

    private final MessageConversationDao messageConversationDao;

    public MessageService(MessageConversationDao messageConversationDao) {
        this.messageConversationDao = messageConversationDao;
    }

    /**
     * Fetch a single message by id.
     *
     * @param messageId This is an id number of the {@link Message} object to be fetched.
     * @return {@link Message} object.
     */
    public Message getMessageById(String messageId) {
        return messageConversationDao.fetchMessageById(messageId).getBody();
    }

    /**
     * Fetch a list of messages by sender id.
     *
     * @param senderId This is an sender id number of the {@link User} object.
     * @return List of {@link Message} objects.
     */
    public List<Message> getAllMessagesBySenderId(String senderId) {
        return messageConversationDao.fetchAllMessagesBySenderId(senderId).getBody();
    }

    /**
     * Fetch a list of messages by recipient id.
     *
     * @param recipientId This is an recipient id number of the {@link User} object.
     * @return List of {@link Message} objects.
     */
    public List<Message> getAllMessagesByRecipientId(String recipientId) {
        return messageConversationDao.fetchAllMessagesByRecipientId(recipientId).getBody();
    }

    /**
     * Fetch a list of conversations that relate to a selected message by message id.
     *
     * @param messageId This is an id number of the {@link Message} object by which conversations will be fetched.
     * @return List of {@link Conversation} objects.
     */
    public List<Conversation> getConversationsByMessageId(String messageId) {
        return messageConversationDao.fetchConversationsByMessageId(messageId).getBody();
    }

    /**
     * Count all conversations that relate to a selected message by message id.
     *
     * @param messageId This is an id number of the {@link Message} object by which conversations will be counted.
     * @return Number of selected message conversations.
     */
    public int numberOfConversationsByMessageId(String messageId) {
        return messageConversationDao.countConversationsByMessageId(messageId);
    }

    /**
     * Insert a new message.
     *
     * @param message This is the new object of the {@link Message} to be saved.
     * @return {@link Message} object
     */
    public ResponseEntity<Message> saveMessage(Message message) {
        return messageConversationDao.insertMessage(message);
    }

    /**
     * Insert a new conversation.
     *
     * @param conversation This is the new object of the {@link Conversation} to be saved.
     */
    public void saveConversation(Conversation conversation) {
        this.messageConversationDao.insertConversation(conversation);
    }

    /**
     * Delete message by id.
     *
     * @param id This is an id number of the {@link Message} object to be deleted.
     */
    public void deleteMessage(String id) {
        this.messageConversationDao.removeMessage(id);
    }

}
