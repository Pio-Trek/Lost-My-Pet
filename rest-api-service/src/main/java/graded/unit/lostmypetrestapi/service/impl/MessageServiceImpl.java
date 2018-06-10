package graded.unit.lostmypetrestapi.service.impl;

import graded.unit.lostmypetrestapi.entity.messages.Message;
import graded.unit.lostmypetrestapi.exception.CustomResponse;
import graded.unit.lostmypetrestapi.repository.MessageRepository;
import graded.unit.lostmypetrestapi.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of {@link MessageService} interface
 *
 * @author Piotr Przechodzki
 * @since 21/05/2018
 */
@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository repository;

    @Autowired
    public MessageServiceImpl(MessageRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Message> getAllMessagesBySenderId(String senderId) {
        return repository.findAllBySenderId(senderId);
    }

    @Override
    public List<Message> getAllMessagesByRecipientId(String recipientId) {
        return repository.findAllByRecipientId(recipientId);
    }

    @Override
    public Optional<Message> getMessageById(String id) {
        return repository.findById(id);
    }

    @Override
    public ResponseEntity<Message> addMessage(Message message) {
        // Generate random ID number based on UUID
        message.setId(UUID.randomUUID().toString().replace("-", ""));
        return new ResponseEntity<>(repository.save(message), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CustomResponse> deleteMessage(String messageId) {
        this.repository.deleteById(messageId);
        return new ResponseEntity<>(
                new CustomResponse(new Date(), HttpStatus.OK.value(),
                        "Message deleted. (ID: " + messageId + ")"), HttpStatus.OK);
    }
}
