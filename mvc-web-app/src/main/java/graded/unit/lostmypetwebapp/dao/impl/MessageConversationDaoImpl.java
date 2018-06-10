package graded.unit.lostmypetwebapp.dao.impl;

import graded.unit.lostmypetwebapp.client.WebClient;
import graded.unit.lostmypetwebapp.dao.MessageConversationDao;
import graded.unit.lostmypetwebapp.model.messages.Conversation;
import graded.unit.lostmypetwebapp.model.messages.Message;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Implementation of {@link MessageConversationDao} interface
 *
 * @author Piotr Przechodzki
 * @since 21/05/2018
 */
@Repository
public class MessageConversationDaoImpl implements MessageConversationDao {

    private final String MESSAGE_SERVICE_URL = "/messages/";

    private final WebClient client;

    public MessageConversationDaoImpl(WebClient client) {
        this.client = client;
    }

    @Override
    public ResponseEntity<Message> fetchMessageById(String messageId) {
        return client.getRestTemplate()
                .getForEntity(MESSAGE_SERVICE_URL + messageId, Message.class);
    }

    @Override
    public ResponseEntity<List<Message>> fetchAllMessagesBySenderId(String senderId) {
        return client.getRestTemplate()
                .exchange(
                        MESSAGE_SERVICE_URL + "sender/" + senderId,
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Message>>() {
                        });
    }

    @Override
    public ResponseEntity<List<Message>> fetchAllMessagesByRecipientId(String recipientId) {
        return client.getRestTemplate()
                .exchange(
                        MESSAGE_SERVICE_URL + "recipient/" + recipientId,
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Message>>() {
                        });
    }

    @Override
    public ResponseEntity<List<Conversation>> fetchConversationsByMessageId(String messageId) {
        return client.getRestTemplate()
                .exchange(
                        MESSAGE_SERVICE_URL + "conversations/" + messageId,
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Conversation>>() {
                        });
    }

    @Override
    public int countConversationsByMessageId(String messageId) {
        return client.getRestTemplate()
                .getForEntity(MESSAGE_SERVICE_URL + "conversations/" + messageId + "/count", Integer.class).getBody();
    }

    @Override
    public ResponseEntity<Message> insertMessage(Message message) {
        return client.getRestTemplate()
                .exchange(MESSAGE_SERVICE_URL, HttpMethod.POST, new HttpEntity<>(message), Message.class);
    }

    @Override
    public void insertConversation(Conversation conversation) {
        this.client.getRestTemplate()
                .exchange(MESSAGE_SERVICE_URL + "conversations", HttpMethod.POST, new HttpEntity<>(conversation), Conversation.class);
    }

    @Override
    public void removeMessage(String id) {
        this.client.getRestTemplate()
                .exchange(MESSAGE_SERVICE_URL + id, HttpMethod.DELETE, null, Message.class);
    }
}
