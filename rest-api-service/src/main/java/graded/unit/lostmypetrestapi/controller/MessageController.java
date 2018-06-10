package graded.unit.lostmypetrestapi.controller;

import graded.unit.lostmypetrestapi.entity.messages.Conversation;
import graded.unit.lostmypetrestapi.entity.messages.Message;
import graded.unit.lostmypetrestapi.entity.users.User;
import graded.unit.lostmypetrestapi.exception.CustomException;
import graded.unit.lostmypetrestapi.exception.CustomResponse;
import graded.unit.lostmypetrestapi.service.ConversationService;
import graded.unit.lostmypetrestapi.service.MessageService;
import graded.unit.lostmypetrestapi.validation.UserValidation;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiPathParam;
import org.jsondoc.core.pojo.ApiStage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Controller layer class for {@link Message} system which exposes resources,
 * CRUD operations and business logic for the {@link MessageService}.
 *
 * @author Piotr Przechodzki
 * @since 21/05/2018
 */
@RestController
@RequestMapping("/api/messages")
@Api(
        name = "Message System API",
        description = "Provides a list of methods that manage messages system.",
        stage = ApiStage.GA)
public class MessageController {

    // URI address for testing purpose
    static String URI = "/api/messages/";

    private final MessageService messageService;
    private final ConversationService conversationService;
    private final UserValidation userValidation;

    @Autowired
    public MessageController(MessageService messageService, ConversationService conversationService, UserValidation userValidation) {
        this.messageService = messageService;
        this.conversationService = conversationService;
        this.userValidation = userValidation;
    }

    /**
     * HTTP GET request method.
     * Fetch a single message by id number.
     *
     * @param id This is an id number of the {@link Message} object to be fetched
     * @return Optional instance of {@link Message} object
     * @throws CustomException When {@link Message#id} is not valid.
     */
    @GetMapping("/{id}")
    @ApiMethod(description = "Fetch a single message by id number.")
    public Optional<Message> fetchMessageById(@ApiPathParam(description = "The id of the message") @PathVariable("id") String id) throws CustomException {
        return validateMessageId(id);
    }

    /**
     * HTTP GET request method.
     * Fetch all messages by sender id number.
     *
     * @param id This is an id number of the {@link User#id} sender object.
     * @return A list of messages specify by sender id.
     * @throws CustomException When {@link User#id} is not valid.
     */
    @GetMapping(path = "/sender/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Fetch all messages by sender id number.")
    public List<Message> fetchMessagesBySenderId(@ApiPathParam(description = "The id of the sender") @PathVariable("id") String id) throws CustomException {
        userValidation.validateById(id);
        return messageService.getAllMessagesBySenderId(id);
    }

    /**
     * HTTP GET request method.
     * Fetch all messages by recipient id number.
     *
     * @param id This is an id number of the {@link User#id} recipient object.
     * @return A list of messages specify by recipient id.
     * @throws CustomException When {@link User#id} is not valid.
     */
    @GetMapping(path = "/recipient/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Fetch all messages by recipient id number.")
    public List<Message> fetchMessagesByRecipientId(@ApiPathParam(description = "The id of the recipient") @PathVariable("id") String id) throws CustomException {
        userValidation.validateById(id);
        return messageService.getAllMessagesByRecipientId(id);
    }

    /**
     * HTTP GET request method.
     * Fetch all conversations that belong to a specific message.
     *
     * @param id This is an id number of the {@link Message} object which has conversations.
     * @return List of all {@link Conversation} objects.
     * @throws CustomException When {@link Message#id} is not valid.
     */
    @GetMapping(path = "/conversations/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Fetch all conversations that belong to a specific message.")
    public List<Conversation> fetchConversationsByMessageId(@ApiPathParam(description = "The id of the message") @PathVariable("id") String id) throws CustomException {
        validateMessageId(id);
        return conversationService.getAllByMessageId(id);
    }

    /**
     * HTTP GET request method.
     * Count all conversations in a specific message.
     *
     * @param id This is an id number of the {@link Message} object which has conversations.
     * @return A number of all conversations.
     * @throws CustomException When {@link Message#id} is not valid.
     */
    @GetMapping(path = "/conversations/{id}/count",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Count all conversations in a specific message.")
    public int countConversationsByMessageId(@ApiPathParam(description = "The id of the message") @PathVariable("id") String id) throws CustomException {
        validateMessageId(id);
        return conversationService.countAllByMessageId(id);
    }

    /**
     * HTTP POST request method.
     * Save a new message to the database.
     *
     * @param message This is a new object of the {@link Message} to be insert.
     * @return HTTP response with {@link Message} object.
     * @throws CustomException When {@link User#email} of {@link Message#sender} and {@link Message#recipient} is not valid.
     */
    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Save a new message.")
    public ResponseEntity<Message> insertMessage(@RequestBody @Validated Message message) throws CustomException {
        message.setLastUpdate(new Date());

        // Validate if sender exists
        userValidation.validateByEmail(message.getSender().getEmail())
                .ifPresent(message::setSender);

        // Validate if recipient exists
        userValidation.validateByEmail(message.getRecipient().getEmail())
                .ifPresent(message::setRecipient);

        this.messageService.addMessage(message);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    /**
     * HTTP POST request method.
     * Save a new conversation to the database.
     *
     * @param conversation This is a new object of the {@link Conversation} to be insert.
     * @return HTTP response with {@link Conversation} object.
     * @throws CustomException When {@link User#email} of {@link Message#sender}, {@link Message#recipient} and {@link Conversation#author} is not valid.
     */
    @PostMapping(path = "/conversations",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Save a new conversation")
    public ResponseEntity<Conversation> insertConversation(@RequestBody @Validated Conversation conversation) throws CustomException {
        conversation.setDate(new Date());

        // Validate if author exists
        userValidation.validateByEmail(conversation.getAuthor().getEmail())
                .ifPresent(conversation::setAuthor);

        // Validate message if exists
        validateMessageId(conversation.getMessage().getId())
                .ifPresent(conversation::setMessage);

        Message message = conversation.getMessage();

        // Validate if sender exists
        userValidation.validateByEmail(message.getSender().getEmail())
                .ifPresent(message::setSender);

        // Validate if recipient exists
        userValidation.validateByEmail(message.getRecipient().getEmail())
                .ifPresent(message::setRecipient);

        if (!message.getSender().getId().equals(conversation.getAuthor().getId())) {
            message.setLastUpdate(new Date());
        }

        return conversationService.addConversation(conversation);
    }

    /**
     * HTTP DELETE request method.
     * Delete from the database a single message.
     *
     * @param id This is an id number of the {@link Message} to be deleted.
     * @return HTTP response with with confirmation message.
     * @throws CustomException When {@link Message#id} is not valid
     */
    @DeleteMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Delete a single message.")
    public ResponseEntity<CustomResponse> removeMessage(@ApiPathParam(description = "The id of the message") @PathVariable("id") String id) throws CustomException {
        validateMessageId(id);
        return messageService.deleteMessage(id);
    }

    /**
     * Method responsible for check if ID number exists in the database.
     *
     * @param id This is an id number of the {@link Message} to be validated.
     * @return Optional instance of {@link Message} object.
     * @throws CustomException When {@link Message#id} is not valid.
     */
    private Optional<Message> validateMessageId(String id) throws CustomException {
        return Optional.of(messageService.getMessageById(id)
                .orElseThrow(() -> new CustomException("Message with ID: '" + id + "' not found.")));
    }
}