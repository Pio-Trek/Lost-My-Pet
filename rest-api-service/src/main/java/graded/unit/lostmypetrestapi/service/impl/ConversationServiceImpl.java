package graded.unit.lostmypetrestapi.service.impl;

import graded.unit.lostmypetrestapi.entity.messages.Conversation;
import graded.unit.lostmypetrestapi.repository.ConversationRepository;
import graded.unit.lostmypetrestapi.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of {@link ConversationService} interface
 *
 * @author Piotr Przechodzki
 * @since 21/05/2018
 */
@Service
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository repository;

    @Autowired
    public ConversationServiceImpl(ConversationRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Conversation> getAllByMessageId(String messageId) {
        return repository.getAllByMessageId(messageId);
    }

    @Override
    public ResponseEntity<Conversation> addConversation(Conversation conversation) {
        return new ResponseEntity<>(repository.save(conversation), HttpStatus.OK);
    }

    @Override
    public int countAllByMessageId(String messageId) {
        return repository.countAllByMessageId(messageId);
    }
}
